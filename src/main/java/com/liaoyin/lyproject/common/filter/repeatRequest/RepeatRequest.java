package com.liaoyin.lyproject.common.filter.repeatRequest;

import java.lang.annotation.*;

/**
 * 作者：
 * 时间：2018/8/14 11:56
 * 描述：指定接口在规定时间内不能重复请求
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatRequest {
}
