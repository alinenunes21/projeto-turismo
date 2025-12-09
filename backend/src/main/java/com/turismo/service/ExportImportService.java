package com.turismo.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.turismo.dto.PontoTuristicoRequestDTO;
import com.turismo.dto.PontoTuristicoResponseDTO;
import com.turismo.model.PontoTuristico;
import com.turismo.repository.PontoTuristicoRepository;

@Service
public class ExportImportService {

    private final PontoTuristicoRepository pontoRepository;
    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;

    public ExportImportService(PontoTuristicoRepository pontoRepository) {
        this.pontoRepository = pontoRepository;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.xmlMapper = new XmlMapper();
        this.xmlMapper.registerModule(new JavaTimeModule());
        this.xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // ==================== EXPORTAÇÃO ====================

    public ByteArrayInputStream exportarJSON() throws IOException {
        List<PontoTuristico> pontos = pontoRepository.findAll();
        String jsonData = objectMapper.writeValueAsString(pontos);
        return new ByteArrayInputStream(jsonData.getBytes());
    }

    public ByteArrayInputStream exportarCSV() throws IOException {
        List<PontoTuristico> pontos = pontoRepository.findAll();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(out);
        CSVWriter csvWriter = new CSVWriter(writer);

        String[] header = {"id", "nome", "descricao", "cidade", "estado", "pais",
                "latitude", "longitude", "endereco", "criadoPor", "createdAt"};
        csvWriter.writeNext(header);

        for (PontoTuristico ponto : pontos) {
            String[] data = {
                    ponto.getId() != null ? ponto.getId().toString() : "",
                    ponto.getNome(),
                    ponto.getDescricao(),
                    ponto.getCidade(),
                    ponto.getEstado(),
                    ponto.getPais(),
                    ponto.getLatitude() != null ? ponto.getLatitude().toString() : "",
                    ponto.getLongitude() != null ? ponto.getLongitude().toString() : "",
                    ponto.getEndereco(),
                    ponto.getCriadoPor() != null ? ponto.getCriadoPor().toString() : "",
                    ponto.getCreatedAt() != null ? ponto.getCreatedAt().toString() : ""
            };
            csvWriter.writeNext(data);
        }

        csvWriter.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream exportarXML() throws IOException {
        List<PontoTuristico> pontos = pontoRepository.findAll();

        StringWriter stringWriter = new StringWriter();
        stringWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        stringWriter.write("<pontosTuristicos>\n");

        for (PontoTuristico ponto : pontos) {
            String pontoXml = xmlMapper.writeValueAsString(ponto);
            stringWriter.write(pontoXml);
            stringWriter.write("\n");
        }

        stringWriter.write("</pontosTuristicos>");

        return new ByteArrayInputStream(stringWriter.toString().getBytes());
    }


    public List<PontoTuristicoResponseDTO> importarJSON(MultipartFile file, Long usuarioId) throws IOException {
        List<PontoTuristicoRequestDTO> dtos = objectMapper.readValue(
                file.getInputStream(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, PontoTuristicoRequestDTO.class)
        );

        return salvarPontosImportados(dtos, usuarioId);
    }

    public List<PontoTuristicoResponseDTO> importarCSV(MultipartFile file, Long usuarioId) throws IOException, CsvException {
        List<PontoTuristicoRequestDTO> dtos = new ArrayList<>();

        CSVReader reader = new CSVReader(new java.io.InputStreamReader(file.getInputStream()));
        List<String[]> records = reader.readAll();
        reader.close();

        for (int i = 1; i < records.size(); i++) {
            String[] row = records.get(i);

            PontoTuristicoRequestDTO dto = new PontoTuristicoRequestDTO();
            dto.setNome(row[1]);
            dto.setDescricao(row[2]);
            dto.setCidade(row[3]);
            dto.setEstado(row[4]);
            dto.setPais(row[5]);

            if (!row[6].isEmpty()) {
                dto.setLatitude(Double.parseDouble(row[6]));
            }
            if (!row[7].isEmpty()) {
                dto.setLongitude(Double.parseDouble(row[7]));
            }

            dto.setEndereco(row[8]);

            dtos.add(dto);
        }

        return salvarPontosImportados(dtos, usuarioId);
    }

    public List<PontoTuristicoResponseDTO> importarXML(MultipartFile file, Long usuarioId) throws IOException {
        String xmlContent = new String(file.getBytes());

        xmlContent = xmlContent.replaceAll("<\\?xml.*?\\?>", "")
                .replaceAll("<pontosTuristicos>", "")
                .replaceAll("</pontosTuristicos>", "")
                .trim();

        List<PontoTuristicoRequestDTO> dtos = new ArrayList<>();

        String[] pontoXmls = xmlContent.split("</PontoTuristico>");

        for (String pontoXml : pontoXmls) {
            if (pontoXml.trim().isEmpty()) continue;

            pontoXml = pontoXml.trim() + "</PontoTuristico>";
            PontoTuristicoRequestDTO dto = xmlMapper.readValue(pontoXml, PontoTuristicoRequestDTO.class);
            dtos.add(dto);
        }

        return salvarPontosImportados(dtos, usuarioId);
    }

    private List<PontoTuristicoResponseDTO> salvarPontosImportados(List<PontoTuristicoRequestDTO> dtos, Long usuarioId) {
        List<PontoTuristicoResponseDTO> resultados = new ArrayList<>();

        for (PontoTuristicoRequestDTO dto : dtos) {
            // Verificar duplicatas
            if (pontoRepository.existsByNomeIgnoreCaseAndCidadeIgnoreCase(dto.getNome(), dto.getCidade())) {
                continue;
            }

            PontoTuristico ponto = new PontoTuristico();
            ponto.setNome(dto.getNome());
            ponto.setDescricao(dto.getDescricao());
            ponto.setCidade(dto.getCidade());
            ponto.setEstado(dto.getEstado());
            ponto.setPais(dto.getPais());
            ponto.setLatitude(dto.getLatitude());
            ponto.setLongitude(dto.getLongitude());
            ponto.setEndereco(dto.getEndereco());
            ponto.setCriadoPor(usuarioId);
            ponto.setCreatedAt(LocalDateTime.now());

            PontoTuristico pontoSalvo = pontoRepository.save(ponto);

            resultados.add(new PontoTuristicoResponseDTO(
                    pontoSalvo.getId(),
                    pontoSalvo.getNome(),
                    pontoSalvo.getDescricao(),
                    pontoSalvo.getCidade(),
                    pontoSalvo.getEstado(),
                    pontoSalvo.getPais(),
                    pontoSalvo.getLatitude(),
                    pontoSalvo.getLongitude(),
                    pontoSalvo.getEndereco(),
                    pontoSalvo.getCriadoPor(),
                    pontoSalvo.getCreatedAt()
            ));
        }

        return resultados;
    }
}