package com.liaoyin.lyproject.config;

import com.alibaba.fastjson.JSON;
import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.common.filter.repeatRequest.RepeatRequestFilter;
import com.liaoyin.lyproject.util.RedisUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * 作者：
 * 时间： 2018/8/7 14:04
 * 描述： 防止请求重复提交
 **/
public class Signature {


	/**
	 * 作者：
	 * 时间： 2018/8/7 14:04
	 * 描述： 防止请求重复提交
	 **/
    public static boolean checkSign(HttpServletRequest request, HttpServletResponse response) throws Exception{
		boolean b = true;
		String path = Common.requestUrlAssemble(request.getServletPath());
		System.out.println("重复地址："+path);
		//接口名称含有此后缀表示规定时间内不能重复请求
		if (RepeatRequestFilter.get(path.concat(Config.RepeatRequest))){
			try {
				HttpSession session = request.getSession();
				Object sessionData = session.getAttribute(path);
				long nowTime = System.currentTimeMillis()/1000;
				//不存在或上一次提交的时间是大于了规定时间的=正常访问
				if (Common.isNull(sessionData) || (nowTime-(long)sessionData)>Config.requestLifeCycle){
					session.setMaxInactiveInterval(Config.requestLifeCycle);
					session.setAttribute(path,nowTime);
				}else{
					response.setHeader("Cache-Control", "no-store");
					response.setHeader("Pragma", "no-cache");
					response.setContentType("application/json;charset=UTF-8");
					response.setCharacterEncoding("UTF-8");
					PrintWriter out=response.getWriter();
					JsonRestResponse result = new JsonRestResponse();
					result.setCode("requestLifeCycle");
					result.setDesc("请"+(Config.requestLifeCycle-(nowTime-(long)sessionData))+"秒后再试...");
					out.write(JSON.toJSONString(result));
					out.close();
					out.flush();
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				//当出现异常时，不做任何处理，返回正确结果，避免当前请求发生阻塞
			}
		}
		return b;
    }
}
