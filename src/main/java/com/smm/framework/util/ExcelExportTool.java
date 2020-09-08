package com.smm.framework.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Alan Chen
 * @description Excel导出工具类
 * @date 2020-02-25
 */
public class ExcelExportTool {

    private ExcelExportTool(){}

    /**
     * 简单格式导出
     * @param response
     * @param headerAlias 用LinkedHashMap表头才会按顺序显示
     * @param datas
     */
    public static void simpleExport(HttpServletResponse response, LinkedHashMap<String,String> headerAlias, List datas){
        ExcelWriter writer = ExcelUtil.getWriter();

        writer.setHeaderAlias(headerAlias);
        writer.setOnlyAlias(true);
        writer.write(datas, true);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String name = createExcelName();
        response.setHeader("Content-Disposition","attachment;filename="+name);
        ServletOutputStream out= null;
        try {
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.close();
        }
        IoUtil.close(out);
    }

    /**
     * 简单格式导出
     * @param headerAlias 用LinkedHashMap表头才会按顺序显示
     * @param datas
     */
    public static String simpleExport(String excelDirectory,LinkedHashMap<String,String> headerAlias, List datas){
        String filePath = getExcelDirectoryPath(excelDirectory)+createExcelName();
        ExcelWriter writer = ExcelUtil.getWriter(filePath);

        writer.setHeaderAlias(headerAlias);
        writer.setOnlyAlias(true);
        writer.write(datas, true);
        writer.close();

        return filePath;
    }

    private static String createExcelName(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = format.format(date.getTime()) +".xls";
        return fileName;
    }


    public static String getExcelDirectoryPath(String excelDirectory){
        return ApplicationDirectoryTool.getApplicationUploadFilesDirectory(excelDirectory);
    }

}
