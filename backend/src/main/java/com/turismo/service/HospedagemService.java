package com.turismo.service;

import com.turismo.dto.HospedagemRequestDTO;
import com.turismo.dto.HospedagemResponseDTO;
import com.turismo.model.Hospedagem;
import com.turismo.model.Usuario;
import com.turismo.repository.HospedagemRepository;
import com.turismo.repository.PontoTuristicoRepository;
import com.turismo.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HospedagemService {

    private final HospedagemRepository hospedagemRepository;
    private final PontoTuristicoRepository pontoTuristicoRepository;
    private final UsuarioRepository usuarioRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final List<String> TIPOS_VALIDOS = List.of(
            "Hotel", "Pousada", "Hostel", "Resort", "Airbnb", "Camping"
    );

    public HospedagemService(HospedagemRepository hospedagemRepository,
                             PontoTuristicoRepository pontoTuristicoRepository,
                             UsuarioRepository usuarioRepository) {
        this.hospedagemRepository = hospedagemRepository;
        this.pontoTuristicoRepository = pontoTuristicoRepository;
        this.usuarioRepository = usuarioRepository;
    }


    @Transactional
    public HospedagemResponseDTO criar(HospedagemRequestDTO dto, Long usuarioId) {
        log.info("Criando hospedagem - Ponto: {}, Usuario: {}, Nome: {}",
                dto.getPontoId(), usuarioId, dto.getNome());

        validarDados(dto);
        validarPonto(dto.getPontoId());
        validarUsuario(usuarioId);

        Hospedagem hospedagem = new Hospedagem();
        hospedagem.setPontoId(dto.getPontoId());
        hospedagem.setUsuarioId(usuarioId);
        hospedagem.setNome(dto.getNome().trim());
        hospedagem.setEndereco(dto.getEndereco().trim());
        hospedagem.setTelefone(dto.getTelefone().trim());
        hospedagem.setPrecoMedio(dto.getPrecoMedio() != null ? dto.getPrecoMedio() : 0.0);

        String tipo = dto.getTipo() != null ? dto.getTipo().trim() : "Hotel";
        if (!TIPOS_VALIDOS.contains(tipo)) {
            tipo = "Hotel";
        }
        hospedagem.setTipo(tipo);

        if (dto.getLinkReserva() != null && !dto.getLinkReserva().trim().isEmpty()) {
            hospedagem.setLinkReserva(dto.getLinkReserva().trim());
        }

        Hospedagem hospedagemSalva = hospedagemRepository.save(hospedagem);
        log.info("Hospedagem criada com sucesso - ID: {}", hospedagemSalva.getId());

        return converterParaDTO(hospedagemSalva);
    }


    @Transactional
    public HospedagemResponseDTO atualizar(Long id, HospedagemRequestDTO dto, Long usuarioId) {
        log.info("Atualizando hospedagem - ID: {}, Usuario: {}", id, usuarioId);

        Hospedagem hospedagem = hospedagemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Hospedagem não encontrada - ID: {}", id);
                    return new RuntimeException("Hospedagem não encontrada");
                });

        if (!hospedagem.getUsuarioId().equals(usuarioId)) {
            log.error("Usuário {} tentou editar hospedagem {} de outro usuário", usuarioId, id);
            throw new RuntimeException("Você não tem permissão para editar esta hospedagem");
        }

        validarDados(dto);

        hospedagem.setNome(dto.getNome().trim());
        hospedagem.setEndereco(dto.getEndereco().trim());
        hospedagem.setTelefone(dto.getTelefone().trim());
        hospedagem.setPrecoMedio(dto.getPrecoMedio() != null ? dto.getPrecoMedio() : 0.0);

        String tipo = dto.getTipo() != null ? dto.getTipo().trim() : "Hotel";
        if (!TIPOS_VALIDOS.contains(tipo)) {
            tipo = hospedagem.getTipo();
        }
        hospedagem.setTipo(tipo);

        if (dto.getLinkReserva() != null && !dto.getLinkReserva().trim().isEmpty()) {
            hospedagem.setLinkReserva(dto.getLinkReserva().trim());
        } else {
            hospedagem.setLinkReserva(null);
        }

        Hospedagem hospedagemAtualizada = hospedagemRepository.save(hospedagem);
        log.info("Hospedagem atualizada com sucesso - ID: {}", hospedagemAtualizada.getId());

        return converterParaDTO(hospedagemAtualizada);
    }

    /**
     * Deletar hospedagem
     */
    @Transactional
    public void deletar(Long id, Long usuarioId) {
        log.info("Deletando hospedagem - ID: {}, Usuario: {}", id, usuarioId);

        Hospedagem hospedagem = hospedagemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Hospedagem não encontrada - ID: {}", id);
                    return new RuntimeException("Hospedagem não encontrada");
                });

        if (!hospedagem.getUsuarioId().equals(usuarioId)) {
            log.error("Usuário {} tentou deletar hospedagem {} de outro usuário", usuarioId, id);
            throw new RuntimeException("Você não tem permissão para deletar esta hospedagem");
        }

        hospedagemRepository.deleteById(id);
        log.info("Hospedagem deletada com sucesso - ID: {}", id);
    }

    /**
     * Buscar hospedagem por ID
     */
    public HospedagemResponseDTO buscarPorId(Long id) {
        log.info("Buscando hospedagem - ID: {}", id);

        Hospedagem hospedagem = hospedagemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Hospedagem não encontrada - ID: {}", id);
                    return new RuntimeException("Hospedagem não encontrada");
                });

        return converterParaDTO(hospedagem);
    }

    /**
     * Listar hospedagens por ponto turístico
     */
    public List<HospedagemResponseDTO> listarPorPonto(Long pontoId) {
        log.info("Listando hospedagens do ponto - ID: {}", pontoId);

        List<Hospedagem> hospedagens = hospedagemRepository.findByPontoIdOrderByCreatedAtDesc(pontoId);
        log.info("Encontradas {} hospedagens para o ponto {}", hospedagens.size(), pontoId);

        return hospedagens.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Listar hospedagens por usuário
     */
    public List<HospedagemResponseDTO> listarPorUsuario(Long usuarioId) {
        log.info("Listando hospedagens do usuário - ID: {}", usuarioId);

        List<Hospedagem> hospedagens = hospedagemRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId);
        log.info("Encontradas {} hospedagens do usuário {}", hospedagens.size(), usuarioId);

        return hospedagens.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Listar todas as hospedagens
     */
    public List<HospedagemResponseDTO> listarTodas() {
        log.info("Listando todas as hospedagens");

        List<Hospedagem> hospedagens = hospedagemRepository.findAll();
        log.info("Total de {} hospedagens encontradas", hospedagens.size());

        return hospedagens.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }


    private void validarDados(HospedagemRequestDTO dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome é obrigatório");
        }
        if (dto.getNome().length() > 200) {
            throw new RuntimeException("Nome deve ter no máximo 200 caracteres");
        }

        if (dto.getEndereco() == null || dto.getEndereco().trim().isEmpty()) {
            throw new RuntimeException("Endereço é obrigatório");
        }

        if (dto.getTelefone() == null || dto.getTelefone().trim().isEmpty()) {
            throw new RuntimeException("Telefone é obrigatório");
        }
        if (dto.getTelefone().length() > 20) {
            throw new RuntimeException("Telefone deve ter no máximo 20 caracteres");
        }

        if (dto.getPrecoMedio() != null && dto.getPrecoMedio() < 0) {
            throw new RuntimeException("Preço médio não pode ser negativo");
        }

        if (dto.getLinkReserva() != null && dto.getLinkReserva().length() > 500) {
            throw new RuntimeException("Link de reserva deve ter no máximo 500 caracteres");
        }
    }

    /**
     * Validar se ponto turístico existe
     */
    private void validarPonto(Long pontoId) {
        if (!pontoTuristicoRepository.existsById(pontoId)) {
            log.error("Ponto turístico não encontrado - ID: {}", pontoId);
            throw new RuntimeException("Ponto turístico não encontrado");
        }
    }

    /**
     * Validar se usuário existe
     */
    private void validarUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            log.error("Usuário não encontrado - ID: {}", usuarioId);
            throw new RuntimeException("Usuário não encontrado");
        }
    }

    /**
     * Converter entidade para DTO
     */
    private HospedagemResponseDTO converterParaDTO(Hospedagem hospedagem) {
        String usuarioNome = usuarioRepository.findById(hospedagem.getUsuarioId())
                .map(Usuario::getNome)
                .orElse("Usuário desconhecido");

        HospedagemResponseDTO dto = new HospedagemResponseDTO();
        dto.setId(hospedagem.getId());
        dto.setPontoId(hospedagem.getPontoId());
        dto.setUsuarioId(hospedagem.getUsuarioId());
        dto.setUsuarioNome(usuarioNome);
        dto.setNome(hospedagem.getNome());
        dto.setEndereco(hospedagem.getEndereco());
        dto.setTelefone(hospedagem.getTelefone());
        dto.setPrecoMedio(hospedagem.getPrecoMedio());
        dto.setTipo(hospedagem.getTipo());
        dto.setLinkReserva(hospedagem.getLinkReserva());
        dto.setCreatedAt(hospedagem.getCreatedAt().format(FORMATTER));
        dto.setUpdatedAt(hospedagem.getUpdatedAt().format(FORMATTER));

        return dto;
    }
}