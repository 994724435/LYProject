package com.liaoyin.lyproject.common.filter.filterAnnotaion;


import java.lang.annotation.*;

/**
 * 作者：
 * 时间：2018/8/2 11:00
 * 描述：指定接口安全过滤
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FilterAnnotation {
}
