package com.liaoyin.lyproject.util;

/**
 * @项目名：公司内部模板项目
 * @作者：
 * @描述：字符串工具类
 * @日期：Created in 2018/6/8 15:22
 */
public final class StringUtil {

    private StringUtil(){

    }
    /**
     * 判断字符串是否为空
     * @param str 待验证的字符串
     * @return true 为空  false 不为空
     */
    public static boolean isNotEmpty( String str ){
        return (str != null && !str.isEmpty());
    }


    public static boolean isEmpty(Object str) {
        return (str == null || str.toString().isEmpty() || "null".equalsIgnoreCase(str.toString()));
    }

}
