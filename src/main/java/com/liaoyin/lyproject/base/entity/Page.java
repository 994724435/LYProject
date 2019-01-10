package com.liaoyin.lyproject.base.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @项目名：公司内部模板项目
 * @作者：
 * @描述：分页类
 * @日期：Created in 2018/6/8 11:19
 */
@Getter
@Setter
public class Page implements Serializable{

    private static final long serialVersionUID = -5206444485441643679L;
    /**
     * 第几页
     */
	@ApiModelProperty(value = "第几页")
    @Transient
    Integer num;
    /**
     * 每页显示多少行
     */
	@ApiModelProperty(value = "每页显示多少行")
    @Transient
    Integer size;
    /**
     * 排序字段 例如：orgType desc/asc
     */
	@ApiModelProperty(value = "排序字段 例如：orgType desc/asc")
    @Transient
    String orderby;
}
