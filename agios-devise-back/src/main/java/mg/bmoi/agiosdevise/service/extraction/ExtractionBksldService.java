package mg.bmoi.agiosdevise.service.extraction;

import mg.bmoi.agiosdevise.DTO.ExtractionBksldDto;
import mg.bmoi.agiosdevise.repository.ExtractionBksldRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExtractionBksldService {

    private final ExtractionBksldRepository extractionBksldRepository;
    private final DataSource oracleConnectionManager;
    private static final Logger logger = LoggerFactory.getLogger(ExtractionBksldService.class);

    public ExtractionBksldService(ExtractionBksldRepository extractionBksldRepository,
                                  DataSource oracleConnectionManager) {
        this.extractionBksldRepository = extractionBksldRepository;
        this.oracleConnectionManager = oracleConnectionManager;
    }


    public List<ExtractionBksldDto> getBksldExtraction(String date) {
        List<Object[]> results = extractionBksldRepository.findComptesWithSoldes(date);

        return results.stream()
                .map(row -> new ExtractionBksldDto(
                        (String) row[0],  // age
                        (String) row[1],  // dev
                        (String) row[2],  // ncp
                        (BigDecimal) row[3]  // sde
                ))
                .collect(Collectors.toList());
    }

    public InputStreamResource generateCsv(List<ExtractionBksldDto> data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {

            // Écriture de l'en-tête CSV avec BOM pour Excel
            writer.write('\ufeff'); // BOM pour UTF-8
            writer.println("AGE;DEV;NCP;SDE"); // Séparateur point-virgule

            // Écriture des données
            data.forEach(dto -> {
                String age = dto.getAge() != null ? dto.getAge() : "";
                String dev = dto.getDev() != null ? dto.getDev() : "";
                String ncp = dto.getNcp() != null ? dto.getNcp() : "";
                String sde = dto.getSde() != null ? dto.getSde().toPlainString().replace('.', ',') : "0,00";

                writer.println(String.join(";", age, dev, ncp, sde));
            });

            writer.flush();
            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du CSV", e);
        }
    }

    public InputStreamResource generateExcel(List<ExtractionBksldDto> data) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Bksld");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("AGE");
            header.createCell(1).setCellValue("DEV");
            header.createCell(2).setCellValue("NCP");
            header.createCell(3).setCellValue("SDE");

            int rowIdx = 1;
            for (ExtractionBksldDto dto : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(dto.getAge());
                row.createCell(1).setCellValue(dto.getDev());
                row.createCell(2).setCellValue(dto.getNcp());
                row.createCell(3).setCellValue(dto.getSde().doubleValue());
            }

            workbook.write(out);
            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du fichier Excel", e);
        }
    }
}