package com.liaoyin.lyproject.api;

import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.common.filter.filterAnnotaion.FilterAnnotation;
import com.liaoyin.lyproject.common.filter.repeatRequest.RepeatRequest;
import com.liaoyin.lyproject.entity.MAgreement;
import com.liaoyin.lyproject.service.MAgreementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 作者：
 * 时间：2018/9/25 14:44
 * 描述：
 */
@Api(description = "协议模块")
@RestController
@RequestMapping(value = "/magreement")
public class MAgreementController {

    @Autowired
    private MAgreementService agreementService;

    /**
     * 作者：
     * 时间： 2018/9/25 14:50
     * 描述： 添加协议
     **/
    @ApiOperation(value = "添加协议", notes = "", response= String.class)
    @PostMapping(value = "saveAgreement")
    @FilterAnnotation
    @RepeatRequest
    public JsonRestResponse saveAgreement(
            @ApiParam(value = "类型 0-注册协议",required = true)@RequestParam Integer type,
            @ApiParam(value = "内容",required = true)@RequestParam String body){
        MAgreement agreement = new MAgreement();
        agreement.setType(type);
        agreement.setBody(body);
        agreement.setCreatedate(new Date());
        return agreementService.saveAgreement(agreement);
    }

    /**
     * 作者：
     * 时间： 2018/9/25 14:59
     * 描述： 修改协议
     **/
    @ApiOperation(value = "修改协议", notes = "", response= String.class)
    @PostMapping(value = "updateAgreement")
    @FilterAnnotation
    @RepeatRequest
    public JsonRestResponse updateAgreement(
            @ApiParam(value = "协议id",required = true)@RequestParam Integer id,
            @ApiParam(value = "内容",required = true)@RequestParam String body){
        MAgreement agreement = new MAgreement();
        agreement.setId(id);
        agreement.setBody(body);
        agreement.setLastupdatedate(new Date());
        return agreementService.updateAgreement(agreement);
    }

    /**
     * 作者：
     * 时间： 2018/9/25 15:00
     * 描述： 查询协议
     **/
    @ApiOperation(value = "查询协议", notes = "", response= String.class)
    @PostMapping(value = "queryAgreement")
    @FilterAnnotation
    public JsonRestResponse queryAgreement(
            @ApiParam(value = "类型 0-注册协议",required = true)@RequestParam Integer type){
        return agreementService.queryAgreement(type);
    }

}
