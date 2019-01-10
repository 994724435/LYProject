package com.liaoyin.lyproject.entity;

import com.liaoyin.lyproject.base.entity.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@ApiModel(value = "demo例子实体")
@Table(name = "demo")
@Data
public class Demo extends Page {
    @Id
    @GeneratedValue(generator = "UUID")
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "删除标记")
    private String del;

    @ApiModelProperty(value = "测试列")
    private String testA;


}