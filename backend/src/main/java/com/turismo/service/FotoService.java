package com.turismo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.turismo.dto.FotoResponseDTO;
import com.turismo.model.Foto;
import com.turismo.model.Usuario;
import com.turismo.repository.FotoRepository;
import com.turismo.repository.PontoTuristicoRepository;
import com.turismo.repository.UsuarioRepository;

@Service
public class FotoService {

    private final FotoRepository fotoRepository;
    private final PontoTuristicoRepository pontoTuristicoRepository;
    private final UsuarioRepository usuarioRepository;

    // Diretório para salvar as fotos (configurável via application.yml)
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private static final List<String> TIPOS_PERMITIDOS = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "image/webp"
    );
    private static final long TAMANHO_MAX = 5 * 1024 * 1024; // 5MB
    private static final int MAX_FOTOS_POR_PONTO = 10;

    // Construtor manual
    public FotoService(FotoRepository fotoRepository,
                      PontoTuristicoRepository pontoTuristicoRepository,
                      UsuarioRepository usuarioRepository) {
        this.fotoRepository = fotoRepository;
        this.pontoTuristicoRepository = pontoTuristicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public FotoResponseDTO upload(MultipartFile arquivo, Long pontoId, String titulo, Long usuarioId) {
        // Validações
        validarArquivo(arquivo);
        validarPonto(pontoId);
        validarLimiteFotos(pontoId);

        try {
            // Criar diretório se não existir
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Gerar nome único para o arquivo
            String extensao = obterExtensao(arquivo.getOriginalFilename());
            String nomeArquivo = UUID.randomUUID().toString() + extensao;
            Path caminhoArquivo = uploadPath.resolve(nomeArquivo);

            // Salvar arquivo no filesystem
            Files.copy(arquivo.getInputStream(), caminhoArquivo);

            // Salvar metadados no MongoDB
            Foto foto = new Foto();
            foto.setPontoId(pontoId);
            foto.setUsuarioId(usuarioId);
            foto.setFilename(nomeArquivo);
            foto.setTitulo(titulo != null ? titulo : "Sem título");
            foto.setPath(caminhoArquivo.toString());
            foto.setContentType(arquivo.getContentType());
            foto.setTamanho(arquivo.getSize());
            foto.setCreatedAt(LocalDateTime.now());

            Foto fotoSalva = fotoRepository.save(foto);

            return converterParaDTOComNome(fotoSalva);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    public List<FotoResponseDTO> listarPorPonto(Long pontoId) {
        return fotoRepository.findByPontoIdOrderByCreatedAtDesc(pontoId)
                .stream()
                .map(this::converterParaDTOComNome)
                .collect(Collectors.toList());
    }

    public List<FotoResponseDTO> listarPorUsuario(Long usuarioId) {
        return fotoRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public byte[] obterArquivo(String filename) {
        Foto foto = fotoRepository.findByFilename(filename)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));

        try {
            Path caminhoArquivo = Paths.get(foto.getPath());
            return Files.readAllBytes(caminhoArquivo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo: " + e.getMessage());
        }
    }

    public String obterContentType(String filename) {
        Foto foto = fotoRepository.findByFilename(filename)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));
        return foto.getContentType();
    }

    public void deletar(String fotoId, Long usuarioId) {
        Foto foto = fotoRepository.findById(fotoId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));

        // Verificar se o usuário é dono da foto (ou se é admin - implementar depois)
        if (!foto.getUsuarioId().equals(usuarioId)) {
            throw new RuntimeException("Você não pode deletar a foto de outro usuário");
        }

        try {
            // Deletar arquivo do filesystem
            Path caminhoArquivo = Paths.get(foto.getPath());
            if (Files.exists(caminhoArquivo)) {
                Files.delete(caminhoArquivo);
            }

            // Deletar metadados do MongoDB
            fotoRepository.deleteById(fotoId);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao deletar arquivo: " + e.getMessage());
        }
    }

    private void validarArquivo(MultipartFile arquivo) {
        if (arquivo.isEmpty()) {
            throw new RuntimeException("Arquivo não pode estar vazio");
        }

        if (arquivo.getSize() > TAMANHO_MAX) {
            throw new RuntimeException("Arquivo muito grande. Máximo: 5MB");
        }

        if (!TIPOS_PERMITIDOS.contains(arquivo.getContentType())) {
            throw new RuntimeException("Tipo de arquivo não permitido. Use: JPG, PNG, WEBP");
        }
    }

    private void validarPonto(Long pontoId) {
        if (!pontoTuristicoRepository.existsById(pontoId)) {
            throw new RuntimeException("Ponto turístico não encontrado");
        }
    }

    private void validarLimiteFotos(Long pontoId) {
        long totalFotos = fotoRepository.countByPontoId(pontoId);
        if (totalFotos >= MAX_FOTOS_POR_PONTO) {
            throw new RuntimeException("Máximo de " + MAX_FOTOS_POR_PONTO + " fotos por ponto turístico");
        }
    }

    private String obterExtensao(String nomeArquivo) {
        if (nomeArquivo == null || !nomeArquivo.contains(".")) {
            return ".jpg";
        }
        return nomeArquivo.substring(nomeArquivo.lastIndexOf(".")).toLowerCase();
    }

    private FotoResponseDTO converterParaDTO(Foto foto) {
        String url = "/api/fotos/arquivo/" + foto.getFilename();
        String dataFormatada = foto.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        return new FotoResponseDTO(
                foto.getId(),
                foto.getPontoId(),
                foto.getUsuarioId(),
                null, // sem nome do usuário nesta versão
                foto.getFilename(),
                foto.getTitulo(),
                url,
                foto.getContentType(),
                foto.getTamanho(),
                dataFormatada
        );
    }

    private FotoResponseDTO converterParaDTOComNome(Foto foto) {
        String nomeUsuario = usuarioRepository.findById(foto.getUsuarioId())
                .map(Usuario::getNome)
                .orElse("Usuário desconhecido");

        String url = "/api/fotos/arquivo/" + foto.getFilename();
        String dataFormatada = foto.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        return new FotoResponseDTO(
                foto.getId(),
                foto.getPontoId(),
                foto.getUsuarioId(),
                nomeUsuario,
                foto.getFilename(),
                foto.getTitulo(),
                url,
                foto.getContentType(),
                foto.getTamanho(),
                dataFormatada
        );
    }
}