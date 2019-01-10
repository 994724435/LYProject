package com.liaoyin.lyproject.util;

import com.github.pagehelper.PageInfo;
import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.base.entity.ResultData;

/**
 * @项目名：公司内部模板项目
 * @作者：
 * @描述：请求结果返回对象
 * @日期：Created in 2018/6/8 15:13
 */
public class RestUtil {

	public static JsonRestResponse createResponse(Object obj) {
		JsonRestResponse result = new JsonRestResponse();
		String code = "success";
		result.setCode(code);
		result.setDesc(PropertiesUtil.get(code));
		result.setResult(obj);
		return result;
	}

	public static JsonRestResponse createResponseData(PageInfo pageInfo) {
		JsonRestResponse result = new JsonRestResponse();
		String code = "success";
		result.setCode(code);
		result.setDesc(PropertiesUtil.get(code));
		ResultData data = new ResultData();
		data.setCount(pageInfo.getTotal());
		data.setPages(pageInfo.getPages());
		data.setPageNum(pageInfo.getPageNum());
		data.setPageSize(pageInfo.getPageSize());
		data.setData(pageInfo.getList());
		result.setResult(data);
		return result;
	}

	public static JsonRestResponse createResponseData(PageInfo pageInfo,Object attach) {
		JsonRestResponse result = new JsonRestResponse();
		String code = "success";
		result.setCode(code);
		result.setDesc(PropertiesUtil.get(code));
		ResultData data = new ResultData();
		data.setCount(pageInfo.getTotal());
		data.setPages(pageInfo.getPages());
		data.setPageNum(pageInfo.getPageNum());
		data.setPageSize(pageInfo.getPageSize());
		data.setData(pageInfo.getList());
		result.setResult(data);
		result.setAttach(attach);
		return result;
	}
	
	public static JsonRestResponse createResponse() {
		return createResponse(null);
	}

	public static JsonRestResponse createResponseErro(String message) {
		JsonRestResponse result = new JsonRestResponse();
		String code = "rest.fail";
		result.setCode(code);
		result.setDesc(message);
		return result;
	}

	public static JsonRestResponse createResponse(String code,String message){
		JsonRestResponse result = new JsonRestResponse();
		result.setCode(code);
		result.setDesc(message);
		return result;
	}

	public static JsonRestResponse createResponse(String code,String message,Object attach){
		JsonRestResponse result = new JsonRestResponse();
		result.setCode(code);
		result.setDesc(message);
		result.setAttach(attach);
		return result;
	}

	public static JsonRestResponse createResponse(String code,Object attach){
		JsonRestResponse result = new JsonRestResponse();
		result.setCode(code);
		result.setDesc(PropertiesUtil.get(code));
		result.setAttach(attach);
		return result;
	}

	public static JsonRestResponse createResponseCode(String code){
		JsonRestResponse result = new JsonRestResponse();
		result.setCode(code);
		result.setDesc(PropertiesUtil.get(code));
		return result;
	}

	public static JsonRestResponse createResponse(Object data,Object attach){
		JsonRestResponse result = new JsonRestResponse();
		String code = "success";
		result.setCode(code);
		result.setDesc(PropertiesUtil.get(code));
		result.setResult(data);
		result.setAttach(attach);
		return result;
	}

}
