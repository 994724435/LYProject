package com.liaoyin.lyproject.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @项目名：公司内部模板项目
 * @作者：
 * @描述：IP工具类
 * @日期：Created in 2018/6/8 15:05
 */
public class IpUtil {


    /**
     *
     * @Title: getRemortIP
     * @Description: 获取IP
     * @param request
     * @return
     */
    public static String getRemortIP(HttpServletRequest request){
        String ip = null;
        if(request.getHeader("x-forwarded-for") == null){
            ip = request.getRemoteAddr();
        }else{
            ip = request.getHeader("x-forwarded-for");
        }
        if(ip.indexOf(",")!=-1){//如果存在，号，需要分隔
            String[] split = ip.split(",");
            ip = split[0];
        }
        return  ip;

    }
}
