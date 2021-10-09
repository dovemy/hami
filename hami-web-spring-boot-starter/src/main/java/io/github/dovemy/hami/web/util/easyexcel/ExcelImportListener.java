package io.github.dovemy.hami.web.util.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel上传监听器
 *
 * @author xuhaoming
 * @since 2021/10/9
 */
public class ExcelImportListener<T> extends AnalysisEventListener<T> {

    private final List<Map<Integer, String>> headList = new ArrayList<>();

    private final List<T> bodyList = new ArrayList<>();

    /**
     * Excel表头处理回调
     *
     * @param headMap 表头键值对
     * @param context Excel上下文
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        headList.add(headMap);
    }

    /**
     * Excel表体每行处理回调
     *
     * @param data 行数据
     * @param analysisContext Excel上下文
     */
    @Override
    public void invoke(T data, AnalysisContext analysisContext) {
        bodyList.add(data);
    }

    /**
     * Excel读完后的回调
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<Map<Integer, String>> getHeadList() {
        return headList;
    }

    public List<T> getBodyList() {
        return bodyList;
    }
}
