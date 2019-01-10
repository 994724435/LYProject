package com.liaoyin.lyproject.util;

import java.util.UUID;

/**
 * @项目名：公司内部模板项目
 * @作者：
 * @描述：uuid工具类
 * @日期：Created in 2018/6/8 15:23
 */
public class UUIDUtil {

    /**
     * @方法名：getUUID
     * @描述： 生成没有“-”的UUID
     * @作者：
     * @日期： Created in 2018/6/8 15:23
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
