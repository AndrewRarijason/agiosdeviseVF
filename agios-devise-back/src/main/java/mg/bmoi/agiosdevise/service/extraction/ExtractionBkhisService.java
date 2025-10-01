package mg.bmoi.agiosdevise.service.extraction;

import mg.bmoi.agiosdevise.DTO.ExtractionBkhisDto;
import mg.bmoi.agiosdevise.repository.ExtractionBkhisRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExtractionBkhisService {

    private final ExtractionBkhisRepository extractionBkhisRepository;

    public ExtractionBkhisService(ExtractionBkhisRepository extractionBkhisRepository) {
        this.extractionBkhisRepository = extractionBkhisRepository;
    }

    public List<ExtractionBkhisDto> getBkhisExtraction(String dateRef, String dateFin) {
        List<Object[]> results = extractionBkhisRepository.findBkhisExtraction(dateRef, dateFin);
        return results.stream()
                .map(row -> new ExtractionBkhisDto(
                        (String) row[0],
                        (String) row[1],
                        (String) row[2],
                        (Date) row[3],
                        (Date) row[4],
                        (BigDecimal) row[5],
                        (Character) row[6]
                )).collect(Collectors.toList());

    }

    public InputStreamResource generateCsv(List<ExtractionBkhisDto> data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {

            writer.write('\ufeff'); // BOM pour UTF-8
            writer.println("AGE;DEV;NCP;DCO;DVA;MON;SEN"); // Séparateur point-virgule

            data.forEach(dto -> {
                String age = dto.getAge() != null ? dto.getAge() : "";
                String dev = dto.getDev() != null ? dto.getDev() : "";
                String ncp = dto.getNcp() != null ? dto.getNcp() : "";
                String dco = dto.getDco() != null ? dto.getDco().toString() : "";
                String dva = dto.getDva() != null ? dto.getDva().toString() : "";
                String mon = dto.getMon() != null ? dto.getMon().toPlainString().replace('.', ',') : "0,00";
                String sen = dto.getSen() != null ? dto.getSen().toString() : "";

                writer.println(String.join(";", age, dev, ncp, dco, dva, mon, sen));
        });

            writer.flush();
            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du fichier CSV", e);
        }
    }

    public InputStreamResource generateExcel(List<ExtractionBkhisDto> data) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Bkhis");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("AGE");
            header.createCell(1).setCellValue("DEV");
            header.createCell(2).setCellValue("NCP");
            header.createCell(3).setCellValue("DCO");
            header.createCell(4).setCellValue("DVA");
            header.createCell(5).setCellValue("MON");
            header.createCell(6).setCellValue("SEN");

            int rowIdx = 1;
            for (ExtractionBkhisDto dto : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(dto.getAge());
                row.createCell(1).setCellValue(dto.getDev());
                row.createCell(2).setCellValue(dto.getNcp());
                row.createCell(3).setCellValue(dto.getDco() != null ? dto.getDco().toString() : "");
                row.createCell(4).setCellValue(dto.getDva() != null ? dto.getDva().toString() : "");
                row.createCell(5).setCellValue(dto.getMon() != null ? dto.getMon().doubleValue() : 0.0);
                row.createCell(6).setCellValue(dto.getSen() != null ? dto.getSen().toString() : "");
            }

            workbook.write(out);
            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du fichier Excel", e);
        }
    }
}
