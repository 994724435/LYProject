package com.liaoyin.lyproject.base.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @项目名：公司内部模板项目
 * @作者：
 * @描述：返回结果类
 * @日期：Created in 2018/6/8 11:15
 */
@Data
public class JsonRestResponse{
	//返回状态码
	@ApiModelProperty(value = "状态码")
    private String code;
    //返回状态描述
	@ApiModelProperty(value = "状态描述")
    private String desc;
    //返回result结果
	@ApiModelProperty(value = "返回对象")
    private Object result;
	//返回附加值
    @ApiModelProperty(value = "附加数据包")
    private Object attach;

}
