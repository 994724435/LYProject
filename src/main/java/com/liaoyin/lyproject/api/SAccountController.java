package com.liaoyin.lyproject.api;

import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.common.filter.filterAnnotaion.FilterAnnotation;
import com.liaoyin.lyproject.common.filter.repeatRequest.RepeatRequest;
import com.liaoyin.lyproject.service.SAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 时间：2018/9/25 17:17
 * 描述：账户模块
 */
@Api(description = "账户模块")
@RestController
@RequestMapping(value = "/saccount")
public class SAccountController {

    @Autowired
    private SAccountService accountService;

    /**
     * 作者：
     * 时间： 2018/9/25 17:23
     * 描述： 获取账户信息
     **/
    @ApiOperation(value = "获取账户信息", notes = "", response= String.class)
    @PostMapping(value = "queryUserAccount")
    @RepeatRequest
    @FilterAnnotation
    public JsonRestResponse queryUserAccount(
            @ApiParam(value = "用户id",required = true)@RequestParam Integer userId
    ){
        return accountService.queryUserAccount(userId);
    }

    /**
     * 作者：
     * 时间： 2018/9/26 9:35
     * 描述： 获取账户记录
     **/
    @ApiOperation(value = "获取账户记录", notes = "", response= String.class)
    @PostMapping(value = "queryUserAccountRecord")
    @FilterAnnotation
    public JsonRestResponse queryUserAccountRecord(
            @ApiParam(value = "用户id",required = true)@RequestParam Integer userId,
            @ApiParam(value = "0-金额  1-积分")@RequestParam(required = false) Integer recordtype,
            @ApiParam(value = "0-排单 1-抢单 2-收款  3-提现  4-赠送(接收)积分")@RequestParam(required = false) Integer recordmold,
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize
    ){
        return accountService.queryUserAccountRecord(userId,recordtype,recordmold,startPage,pageSize);
    }

}
