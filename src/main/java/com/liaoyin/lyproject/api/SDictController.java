package com.liaoyin.lyproject.api;

import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.common.filter.filterAnnotaion.FilterAnnotation;
import com.liaoyin.lyproject.common.filter.repeatRequest.RepeatRequest;
import com.liaoyin.lyproject.service.SDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者：
 * 时间：2018/9/29 9:59
 * 描述：字典模块
 */
@Api(description = "字典模块")
@RestController
@RequestMapping(value = "/sdict")
public class SDictController {

    @Autowired
    private SDictService dictService;

    /**
     * 作者：
     * 时间： 2018/9/29 10:17
     * 描述： 添加字典
     **/
    @ApiOperation(value = "添加字典", notes = "", response= String.class)
    @PostMapping(value = "saveDict")
    @FilterAnnotation
    @RepeatRequest
    public JsonRestResponse saveDict(
            @ApiParam(value = "编码",required = true)@RequestParam String code,
            @ApiParam(value = "类型汇总",required = true)@RequestParam String dictType,
            @ApiParam(value = "分类型",required = true)@RequestParam String displayvalue,
            @ApiParam(value = "真实值",required = true)@RequestParam Double realvalue,
            @ApiParam(value = "父id")@RequestParam(required = false) Integer parentid
    ){
        return dictService.saveDict(code,dictType,displayvalue,realvalue,parentid);
    }

    
    /**
     * 作者：
     * 时间： 2018/10/9 18:06
     * 描述： 查询字典
     **/
    @ApiOperation(value = "查询字典", notes = "", response= String.class)
    @PostMapping(value = "selectDict")
    @FilterAnnotation
    public JsonRestResponse selectDict(
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize
    ){
        return dictService.selectDict(startPage,pageSize);
    }

    /**
     * 作者：
     * 时间： 2018/10/9 18:06
     * 描述： 修改字典
     **/
    @ApiOperation(value = "修改字典", notes = "", response= String.class)
    @PostMapping(value = "updateDict")
    @FilterAnnotation
    public JsonRestResponse updateDict(
            @ApiParam(value = "字典id",required = true)@RequestParam Integer id,
            @ApiParam(value = "编码",required = true)@RequestParam String code,
            @ApiParam(value = "类型汇总",required = true)@RequestParam String dictType,
            @ApiParam(value = "分类型",required = true)@RequestParam String displayvalue,
            @ApiParam(value = "真实值",required = true)@RequestParam Double realvalue,
            @ApiParam(value = "父id")@RequestParam(required = false) Integer parentid
    ){

        return dictService.updateDict(id,code,dictType,displayvalue,realvalue,parentid);
    }


}
