package com.liaoyin.lyproject.exception;

/**
 * @项目名：公司内部模板项目
 * @作者：
 * @描述：全局异常处理类
 * @日期：Created in 2018/6/8 14:49
 */
public class BusinessException extends BaseException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3830256277582181780L;

	public BusinessException(String code) {
		super(code);
		this.code = code;
	}

	public BusinessException(String code, String... args) {
		super(code);
		this.args=args;
	}

}
