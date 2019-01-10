package com.liaoyin.lyproject.exception;

import lombok.Getter;
import lombok.Setter;

public class BaseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -777459836445914851L;

	// 错误代码
	@Getter
	@Setter
	protected String code;
	// 错误描述
	@Getter
	@Setter
	protected String desc;

	@Getter
	@Setter
	protected String[] args;
	
	public BaseException() {
		super();
	}

	public BaseException(String code) {
		super(code);
		this.code = code;
	}

	public BaseException(String code,String... args){
		super(code);
		this.code = code;
		this.args=args;
	}

}
