package com.liaoyin.lyproject.common.aop.system;


import java.lang.annotation.*;

/**
 * 时间：2018/8/2 11:00
 * 描述：指定接口过滤
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemUserAnnotation {
    String description()  default "";
}
