package io.github.dovemy.hami.web.util.easyexcel;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import io.github.dovemy.hami.web.exception.BusinessException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 通用EasyExcel导入导出处理器
 *
 * @param <I> 导入类型
 * @param <E> 导出类型
 *
 * @author xuhaoming
 * @date 2021/10/9
 */
public abstract class AbstractExcelHandler<I, E> {

    /**
     * 下载导入Excel模板
     *
     * @param fileName            文件名
     * @param templatePath        本地Excel模板路径
     * @param httpServletResponse servlet响应
     * @throws IOException IO异常
     */
    public void downloadImportTemplate(String fileName, String templatePath, HttpServletResponse httpServletResponse) throws IOException {
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ExcelWriter excelWriter = EasyExcel.write(httpServletResponse.getOutputStream())
                .withTemplate(new ClassPathResource(templatePath).getInputStream())
                .build();
        excelWriter.finish();
    }


    /**
     * 导入Excel
     *
     * @param file 上传文件
     * @param templatePath 本地Excel模板路径
     * @throws IOException IO异常
     */
    @SuppressWarnings("unchecked")
    public void importExcel(MultipartFile file, String templatePath) throws IOException {
        Class<I> importRowClazz = (Class<I>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        ExcelImportListener<I> importListener = this.checkImportHeads(file, templatePath, importRowClazz);
        List<I> bodyRows = this.checkImportBodyRows(importListener);
        this.handleImportBodyRows(bodyRows);
    };


    /**
     * 导出Excel
     *
     * @param fileName            文件名
     * @param templatePath        本地Excel模板路径
     * @param bodyRows            导出内容列表
     * @param httpServletResponse servlet响应
     * @throws IOException IO异常
     */
    public void exportExcel(String fileName, String templatePath, List<E> bodyRows, HttpServletResponse httpServletResponse) throws IOException {
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
        ExcelWriter excelWriter = EasyExcel.write(httpServletResponse.getOutputStream())
                .withTemplate(new ClassPathResource(templatePath).getInputStream())
                .build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.fill(bodyRows, writeSheet);
        excelWriter.finish();
    }


    /**
     * 校验导入Excel头部是否正确
     *
     * @param file         上传文件
     * @param templatePath 本地Excel模板路径
     * @param clazz        列对象
     * @return Excel上传监听器
     * @throws IOException IO异常
     */
    protected ExcelImportListener<I> checkImportHeads(MultipartFile file, String templatePath, Class<I> clazz) throws IOException {
        // 本地模板
        ExcelImportListener<I> templateListener = new ExcelImportListener<>();
        InputStream excelTemplateInputStream = new ClassPathResource(templatePath).getInputStream();
        EasyExcel.read(excelTemplateInputStream, clazz, templateListener).sheet().headRowNumber(1).doRead();
        List<Map<Integer, String>> templateHeadList = templateListener.getHeadList();
        // 上传文件
        ExcelImportListener<I> importListener = new ExcelImportListener<>();
        EasyExcel.read(file.getInputStream(), clazz, importListener).sheet().headRowNumber(1).doRead();
        List<Map<Integer, String>> importHeadList = importListener.getHeadList();
        // 对比表头
        for (int i = 0; i < templateHeadList.size(); i++) {
            String templateHead = JSON.toJSONString(templateHeadList.get(i));
            String importHead = JSON.toJSONString(importHeadList.get(i));
            if (!templateHead.equals(importHead)) {
                throw new BusinessException("上传Excel模板错误");
            }
        }
        return importListener;
    }


    /**
     * 校验导入Excel内容是否正确
     *
     * @param importListener Excel上传监听器
     * @return 导入内容列表
     */
    protected List<I> checkImportBodyRows(ExcelImportListener<I> importListener) {
        List<I> bodyRows = importListener.getBodyList();
        if (bodyRows == null || bodyRows.size() == 0) {
            throw new BusinessException("导入内容为空");
        }
        return bodyRows;
    }


    /**
     * 处理导入Excel内容不符
     *
     * @param bodyRows 导入内容列表
     */
    protected void handleImportBodyRows(List<I> bodyRows) {

    }


}
