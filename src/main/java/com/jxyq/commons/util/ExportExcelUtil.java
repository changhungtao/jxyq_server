package com.jxyq.commons.util;

import com.jxyq.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExportExcelUtil {
    public static HSSFWorkbook exportExcel(List<Map<String, Object>> map_list, Map<String, String> header, String sheetName) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = null;
        String key = null;
        HSSFRow row = null;
        try {
            if (StringUtils.isBlank(sheetName)) {
                sheet = wb.createSheet(sheetName);
            } else {
                sheet = wb.createSheet();
            }

            for (int i = 0; i < header.size(); i++) {
                sheet.setColumnWidth(i, 8000);
            }

            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            /*表头*/
            Iterator it = header.keySet().iterator();
            row = sheet.createRow(0);
            for (int k = 0; k < header.size(); k++) {
                if (it.hasNext()) {
                    key = it.next().toString();
                    HSSFCell cell = row.createCell(k);
                    cell.setCellValue(header.get(key).toString());
                    cell.setCellStyle(style);
                }
            }

            /*数据*/
            int index = 1;
            for (Map<String, Object> map : map_list) {
                row = sheet.createRow(index);
                it = header.keySet().iterator();
                for (int m = 0; m < header.size(); m++) {
                    key = it.next().toString();
                    Object proValue = map.get(key);
                    if (proValue != null) {
                        HSSFCell cell = row.createCell(m);
                        cell.setCellValue(proValue.toString());
                        cell.setCellStyle(style);
                    } else {
                        HSSFCell cell = row.createCell(m);
                        cell.setCellValue("");
                    }
                }
                index++;
            }
        } catch (Exception ep) {
            ep.printStackTrace();
        }
        return wb;
    }
}
