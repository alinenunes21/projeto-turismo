package com.turismo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.turismo.dto.RespostaDTO;
import com.turismo.dto.ComentarioRequestDTO;
import com.turismo.dto.ComentarioResponseDTO;
import com.turismo.model.Comentario;
import com.turismo.model.Usuario;
import com.turismo.repository.ComentarioRepository;
import com.turismo.repository.PontoTuristicoRepository;
import com.turismo.repository.UsuarioRepository;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final PontoTuristicoRepository pontoTuristicoRepository;
    private final UsuarioRepository usuarioRepository;

    // Construtor manual
    public ComentarioService(ComentarioRepository comentarioRepository,
                           PontoTuristicoRepository pontoTuristicoRepository,
                           UsuarioRepository usuarioRepository) {
        this.comentarioRepository = comentarioRepository;
        this.pontoTuristicoRepository = pontoTuristicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ComentarioResponseDTO criar(ComentarioRequestDTO dto, Long usuarioId) {
        // Verificar se o ponto turístico existe
        if (!pontoTuristicoRepository.existsById(dto.getPontoId())) {
            throw new RuntimeException("Ponto turístico não encontrado");
        }

        // Criar comentário
        Comentario comentario = new Comentario();
        comentario.setPontoId(dto.getPontoId());
        comentario.setUsuarioId(usuarioId);
        comentario.setTexto(dto.getTexto());
        comentario.setCreatedAt(LocalDateTime.now());

        // Adicionar metadados
        Comentario.Metadata metadata = new Comentario.Metadata();
        metadata.setLanguage("pt");
        metadata.setDevice(dto.getDevice());
        metadata.setUserAgent(dto.getUserAgent());
        comentario.setMetadata(metadata);

        Comentario comentarioSalvo = comentarioRepository.save(comentario);

        return converterParaDTOComNome(comentarioSalvo);
    }

    public List<ComentarioResponseDTO> listarPorPonto(Long pontoId) {
        return comentarioRepository.findByPontoIdOrderByCreatedAtDesc(pontoId)
                .stream()
                .map(this::converterParaDTOComNome)
                .collect(Collectors.toList());
    }

    public List<ComentarioResponseDTO> listarPorUsuario(Long usuarioId) {
        return comentarioRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public ComentarioResponseDTO buscarPorId(String id) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));
        
        return converterParaDTOComNome(comentario);
    }

    public ComentarioResponseDTO atualizar(String id, ComentarioRequestDTO dto, Long usuarioId) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));

        // Verificar se o usuário é dono do comentário
        if (!comentario.getUsuarioId().equals(usuarioId)) {
            throw new RuntimeException("Você não pode editar o comentário de outro usuário");
        }

        comentario.setTexto(dto.getTexto());
        Comentario comentarioAtualizado = comentarioRepository.save(comentario);

        return converterParaDTOComNome(comentarioAtualizado);
    }

    public ComentarioResponseDTO adicionarResposta(String comentarioId, String textoResposta, Long usuarioId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));

        // Adicionar resposta
        comentario.adicionarResposta(usuarioId, textoResposta);
        Comentario comentarioAtualizado = comentarioRepository.save(comentario);

        return converterParaDTOComNome(comentarioAtualizado);
    }

    public void deletar(String id, Long usuarioId, boolean isAdmin) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));

        // Verificar permissões: dono do comentário OU admin
        if (!comentario.getUsuarioId().equals(usuarioId) && !isAdmin) {
            throw new RuntimeException("Você não pode deletar este comentário");
        }

        comentarioRepository.deleteById(id);
    }

    public long contarComentariosPonto(Long pontoId) {
        return comentarioRepository.countByPontoId(pontoId);
    }

  public List<ComentarioResponseDTO> pesquisarTexto(String texto) {
    // Por enquanto, implementação simples - busca todos e filtra
    return comentarioRepository.findAll()
            .stream()
            .filter(comentario -> comentario.getTexto() != null && 
                    comentario.getTexto().toLowerCase().contains(texto.toLowerCase()))
            .map(this::converterParaDTOComNome)
            .collect(Collectors.toList());
}

    private ComentarioResponseDTO converterParaDTO(Comentario comentario) {
        String dataFormatada = comentario.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        return new ComentarioResponseDTO(
                comentario.getId(),
                comentario.getPontoId(),
                comentario.getUsuarioId(),
                null, // sem nome do usuário nesta versão
                comentario.getTexto(),
                dataFormatada,
                null // sem respostas nesta versão simples
        );
    }

    private ComentarioResponseDTO converterParaDTOComNome(Comentario comentario) {
        // Buscar nome do usuário
        String nomeUsuario = usuarioRepository.findById(comentario.getUsuarioId())
                .map(Usuario::getNome)
                .orElse("Usuário desconhecido");

        String dataFormatada = comentario.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        // Converter respostas
        List<RespostaDTO> respostasDto = comentario.getRespostas() != null ? 
                comentario.getRespostas().stream()
                    .map(resposta -> {
                        String nomeUsuarioResposta = usuarioRepository.findById(resposta.getUsuarioId())
                                .map(Usuario::getNome)
                                .orElse("Usuário desconhecido");
                        String dataResposta = resposta.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                        return new RespostaDTO(
                            resposta.getUsuarioId(),
                            nomeUsuarioResposta,
                            resposta.getTexto(),
                            dataResposta
                        );
                    })
                    .collect(Collectors.toList()) : null;

        return new ComentarioResponseDTO(
                comentario.getId(),
                comentario.getPontoId(),
                comentario.getUsuarioId(),
                nomeUsuario,
                comentario.getTexto(),
                dataFormatada,
                respostasDto
        );
    }
}