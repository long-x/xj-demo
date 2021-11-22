package com.ecdata.cmp.common.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * excel导出
 *
 * @author xuxiaojian
 * @date 2020/3/21 0:30
 */
public class ExcelUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * excel 导出工具类
     *
     * @param response
     * @param fileName    文件名
     * @param projects    对象集合
     * @param columnNames 导出的excel中的列名（中文）
     * @param keys        对应的是对象中的字段名字
     * @throws IOException
     */
    public static void export(HttpServletResponse response, String fileName, List<?> projects, String[] columnNames, String[] keys) {

        ExcelWriter bigWriter = ExcelUtil.getBigWriter();

        if (columnNames != null) {
            for (int i = 0; i < columnNames.length; i++) {
                bigWriter.addHeaderAlias(keys[i], columnNames[i]);
                bigWriter.setColumnWidth(i, 30);
            }
        }

        ServletOutputStream out = null;
        try {
            // 一次性写出内容，使用默认样式，强制输出标题
            bigWriter.write(projects, true);
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");

            String downloadFileName = new String((fileName).getBytes(), StandardCharsets.UTF_8);

            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName + ".xlsx");

            out = response.getOutputStream();
            bigWriter.flush(out, true);
        } catch (IOException e) {
            logger.error("导出excel异常!", e);
        } finally {
            // 关闭writer，释放内存
            bigWriter.close();
        }

        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }
}
