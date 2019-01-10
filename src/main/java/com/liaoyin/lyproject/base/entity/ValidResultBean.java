package com.liaoyin.lyproject.base.entity;

/**
 * @项目名：公司内部模板项目
 * @作者：
 * @描述：验证结果参数类
 * @日期：Created in 2018/6/8 11:29
 */
public class ValidResultBean {
	private String fieldName ;
	private String errorMassage;
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getErrorMassage() {
		return errorMassage;
	}
	public void setErrorMassage(String errorMassage) {
		this.errorMassage = errorMassage;
	}

}
