package com.smm.framework.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

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
        return simpleExport(excelDirectory,headerAlias,datas,null);
    }

    /**
     * 简单格式导出 & 按列汇总求和
     * @param excelDirectory
     * @param headerAlias
     * @param datas
     * @param sumColumnNums：
     *        1、需要求和的列，第一列为：1
     *        2、求和的列，只能是数字类型（int,double,float等），不能是String类型或其他类型
     * @return
     */
    public static String simpleExport(String excelDirectory,LinkedHashMap<String,String> headerAlias, List datas,List<Integer> sumColumnNums){
        String fileName = createExcelName();
        String filePath = getExcelDirectoryPath(excelDirectory)+fileName;
        ExcelWriter writer = ExcelUtil.getWriter(filePath);

        writer.setHeaderAlias(headerAlias);
        writer.setOnlyAlias(true);
        writer.write(datas, true);

        if(datas!=null && datas.size()>0){
            //按列汇总求和
            sumColumns(writer,sumColumnNums);
        }

        writer.close();
        String fileAccessPath = excelDirectory+"/"+fileName;
        return fileAccessPath;
    }

    /**
     * 按列汇总求和
     * @param writer
     * @param sumColumnNums
     */
    private static void sumColumns(ExcelWriter writer,List<Integer> sumColumnNums){
        if(sumColumnNums!=null && sumColumnNums.size()>0){
            int rowCount = writer.getRowCount();
            int letterStartNum = 65;
            Row row = writer.getSheet().createRow(rowCount);
            for(Integer columnNum:sumColumnNums){
                //下标是从0开始的，所以要减一
                int column = columnNum-1;

                int letterNum = letterStartNum+(column);
                String columnName = (char)letterNum+"";

                String sumstring = "SUM("+columnName+"2:"+columnName+rowCount+")";//求和公式
                Cell cell = row.createCell(column);
                cell.setCellFormula(sumstring);
            }
        }
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
