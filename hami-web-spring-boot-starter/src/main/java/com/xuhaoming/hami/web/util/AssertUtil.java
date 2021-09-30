package com.xuhaoming.hami.web.util;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.xuhaoming.hami.web.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.Map;

/**
 * 断言工具
 *
 * @author xuhaoming
 * @date 2021/9/1 9:51
 */
public class AssertUtil {

    /**
     * 断言条件为 true， 如果是false则抛出业务异常
     *
     * @param condition 条件
     * @param errorMsg  错误提示信息
     */
    public static void isTrue(boolean condition, String errorMsg) {
        if (!condition) {
            throw new BusinessException(errorMsg);
        }
    }

    /**
     * 断言条件为 true， 如果是false则抛出指定异常
     *
     * @param condition  条件
     * @param httpStatus http状态码
     * @param errorMsg   错误提示信息
     */
    public static void isTrue(boolean condition, HttpStatus httpStatus, String errorMsg) {
        if (!condition) {
            throw new BusinessException(httpStatus, errorMsg);
        }
    }

    /**
     * 断言条件为 false， 如果是 true 则抛出业务异常
     *
     * @param condition 条件
     * @param errorMsg  错误提示信息
     */
    public static void isFalse(boolean condition, String errorMsg) {
        if (condition) {
            throw new BusinessException(errorMsg);
        }
    }

    /**
     * 断言条件为 false， 如果是 true 则抛出指定异常
     *
     * @param condition  条件
     * @param httpStatus http状态码
     * @param errorMsg   错误提示信息
     */
    public static void isFalse(boolean condition, HttpStatus httpStatus, String errorMsg) {
        if (condition) {
            throw new BusinessException(errorMsg);
        }
    }

    /**
     * 断言对象非空， 如果为null 则抛出业务异常
     *
     * @param obj      对象
     * @param errorMsg 错误提示信息
     */
    public static void isNotNull(Object obj, String errorMsg) {
        if (obj == null) {
            throw new BusinessException(errorMsg);
        }
    }

    /**
     * 断言对象非空， 如果为null 则抛出指定异常
     *
     * @param obj        对象
     * @param httpStatus http状态码
     * @param errorMsg   错误提示信息
     */
    public static void isNotNull(Object obj, HttpStatus httpStatus, String errorMsg) {
        if (obj == null) {
            throw new BusinessException(httpStatus, errorMsg);
        }
    }

    /**
     * 断言字符串非空白串，否则则抛出业务异常
     *
     * @param str      字符串
     * @param errorMsg 错误提示信息
     */
    public static void isNotBlank(String str, String errorMsg) {
        if (StrUtil.isBlank(str)) {
            throw new BusinessException(errorMsg);
        }
    }

    /**
     * 断言字符串非空白串，否则则抛出指定异常
     *
     * @param str        字符串
     * @param httpStatus http状态码
     * @param errorMsg   错误提示信息
     */
    public static void isNotBlank(String str, HttpStatus httpStatus, String errorMsg) {
        if (StrUtil.isBlank(str)) {
            throw new BusinessException(httpStatus, errorMsg);
        }
    }

    /**
     * 断言集合是否非空，否则则抛出业务异常
     *
     * @param collection 集合
     * @param errorMsg   错误提示信息
     */
    public static void isNotEmpty(Collection<?> collection, String errorMsg) {
        if (collection == null || collection.isEmpty()) {
            throw new BusinessException(errorMsg);
        }

    }

    /**
     * 断言集合是否非空，否则则抛出指定异常
     *
     * @param collection 集合
     * @param httpStatus http状态码
     * @param errorMsg   错误提示信息
     */
    public static void isNotEmpty(Collection<?> collection, HttpStatus httpStatus, String errorMsg) {
        if (collection == null || collection.isEmpty()) {
            throw new BusinessException(httpStatus, errorMsg);
        }
    }


    /**
     * 断言集合是否非空，否则则抛出业务异常
     *
     * @param map      集合
     * @param errorMsg 错误提示信息
     */
    public static void isNotEmpty(Map<?, ?> map, String errorMsg) {
        if (map == null || map.isEmpty()) {
            throw new BusinessException(errorMsg);
        }
    }

    /**
     * 断言集合是否非空，否则则抛出指定异常
     *
     * @param map        集合
     * @param httpStatus http状态码
     * @param errorMsg   错误提示信息
     */
    public static void isNotEmpty(Map<?, ?> map, HttpStatus httpStatus, String errorMsg) {
        if (map == null || map.isEmpty()) {
            throw new BusinessException(httpStatus, errorMsg);
        }
    }


}
