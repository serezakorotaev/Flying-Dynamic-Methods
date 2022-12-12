package ru.korotaev.dynamics.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.korotaev.dynamics.model.Parameters;
import ru.korotaev.dynamics.service.method.EilerService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final EilerService eilerService;

    public Resource exportByEilerMethod(double dt, boolean alpha) throws IOException {
        final XSSFWorkbook workbook = new XSSFWorkbook();
        fillDocument(eilerService.eilerMethod(dt, alpha), workbook, alpha);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    }

    private void fillDocument(List<Parameters> parametersList , XSSFWorkbook workbook, boolean alpha) {
        final XSSFSheet sheet = workbook.createSheet(String.format("основные элементы траектории при %s", alpha ? "изменяющимся угле" : "угле 0 градусов"));
        fillHeaders(sheet);
        int rowInExcel = 1;
        for (final Parameters parameters : parametersList) {
            final XSSFRow row = sheet.createRow(rowInExcel);
            fillRow(parameters, row);
            rowInExcel ++;
        }
    }

    private void fillRow(Parameters parameters , XSSFRow row) {
        row.createCell(0, CellType.STRING).setCellValue(parameters.getN());
        row.createCell(1, CellType.STRING).setCellValue(parameters.getT());
        row.createCell(2, CellType.STRING).setCellValue(parameters.getCurrentWeight());
        row.createCell(3, CellType.STRING).setCellValue(parameters.getPush());
        row.createCell(4, CellType.STRING).setCellValue(parameters.getVelocity());
        row.createCell(5, CellType.STRING).setCellValue(parameters.getMach());
        row.createCell(6, CellType.STRING).setCellValue(parameters.getCxa());
        row.createCell(7, CellType.STRING).setCellValue(parameters.getAlpha());
        row.createCell(8, CellType.STRING).setCellValue(parameters.getFettaC());
        row.createCell(9, CellType.STRING).setCellValue(parameters.getCya());
        row.createCell(10, CellType.STRING).setCellValue(parameters.getDVdt());
        row.createCell(11, CellType.STRING).setCellValue(parameters.getWz());
        row.createCell(12, CellType.STRING).setCellValue(parameters.getTetta());
        row.createCell(13, CellType.STRING).setCellValue(parameters.getY());
        row.createCell(14, CellType.STRING).setCellValue(parameters.getDYdt());
        row.createCell(15, CellType.STRING).setCellValue(parameters.getX());
        row.createCell(16, CellType.STRING).setCellValue(parameters.getDXdt());
    }

    private void fillHeaders(XSSFSheet sheet) {
        final XSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0, CellType.STRING).setCellValue("N");
        headerRow.createCell(1, CellType.STRING).setCellValue("t,c");
        headerRow.createCell(2, CellType.STRING).setCellValue("m, кг");
        headerRow.createCell(3, CellType.STRING).setCellValue("Р,Н");
        headerRow.createCell(4, CellType.STRING).setCellValue("V,м/с");
        headerRow.createCell(5, CellType.STRING).setCellValue("М");
        headerRow.createCell(6, CellType.STRING).setCellValue("Cxa");
        headerRow.createCell(7, CellType.STRING).setCellValue("a град");
        headerRow.createCell(8, CellType.STRING).setCellValue("θc , град");
        headerRow.createCell(9, CellType.STRING).setCellValue("Суа");
        headerRow.createCell(10, CellType.STRING).setCellValue("dV/dt, м/с^2");
        headerRow.createCell(11, CellType.STRING).setCellValue("Wz, 1/c");
        headerRow.createCell(12, CellType.STRING).setCellValue("ϑ, град");
        headerRow.createCell(13, CellType.STRING).setCellValue("y, м");
        headerRow.createCell(14, CellType.STRING).setCellValue("dy/dt, м/с");
        headerRow.createCell(15, CellType.STRING).setCellValue("x, м");
        headerRow.createCell(16, CellType.STRING).setCellValue("dx/dt, м/с");
    }
}
