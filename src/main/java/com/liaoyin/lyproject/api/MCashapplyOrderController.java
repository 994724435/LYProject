package com.liaoyin.lyproject.api;

import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.common.aop.user.UserAnnotation;
import com.liaoyin.lyproject.common.bean.SessionBean;
import com.liaoyin.lyproject.common.filter.filterAnnotaion.FilterAnnotation;
import com.liaoyin.lyproject.common.filter.repeatRequest.RepeatRequest;
import com.liaoyin.lyproject.exception.BusinessException;
import com.liaoyin.lyproject.service.MCashapplyOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：
 * 时间：2018/9/28 21:23
 * 描述：提现模块
 */
@Api(description = "提现模块")
@RestController
@RequestMapping(value = "/mcashapplyOrder")
public class MCashapplyOrderController{

    @Autowired
    private MCashapplyOrderService cashapplyOrderService;

    /**
     * 作者：
     * 时间： 2018/9/28 21:26
     * 描述： 申请提现
     **/
    @ApiOperation(value = "申请提现", notes = "", response= String.class)
    @PostMapping(value = "insetCashapply")
    @FilterAnnotation
    @UserAnnotation(description = "申请提现")
    public synchronized JsonRestResponse insetCashapply(HttpServletRequest request,
                                           @ApiParam(value = "金额",required = true)@RequestParam Integer price){
        return cashapplyOrderService.insetCashapply(request,price);
    }

    /**
     * 作者：
     * 时间： 2018/10/4 16:26
     * 描述： 用户提现记录
     **/
    @ApiOperation(value = "用户提现记录", notes = "", response= String.class)
    @PostMapping(value = "queryUserCashapplyOrderRecord")
    @FilterAnnotation
    @UserAnnotation(description = "查询提现记录")
    public JsonRestResponse queryUserCashapplyOrderRecord(HttpServletRequest request,
                                                        @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
                                                        @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize,
                                                        @ApiParam(value = "0-匹配中  1-匹配完成   2-放弃")@RequestParam(required = false) Integer cashapplyStatus
                                                          ){
        SessionBean bean = Config.getSessionBean(request);
        if (Common.isNull(bean)){
            throw new BusinessException("common.tokenInvalid");
        }
        return cashapplyOrderService.queryUserCashapplyOrderRecord(bean.getUid(),request,startPage,pageSize,cashapplyStatus);
    }

    /**
     * 作者：
     * 时间： 2018/10/13 20:24
     * 描述： 放弃提现
     **/
    @ApiOperation(value = "放弃提现", notes = "", response= String.class)
    @PostMapping(value = "giveCashapply")
    @FilterAnnotation
    @UserAnnotation(description = "放弃提现")
    public JsonRestResponse giveCashapply(HttpServletRequest request,
                                        @ApiParam(value = "提现id",required = true)@RequestParam Integer cashapplyId
    ){
        return cashapplyOrderService.givePlatoon(request,cashapplyId);
    }

}
