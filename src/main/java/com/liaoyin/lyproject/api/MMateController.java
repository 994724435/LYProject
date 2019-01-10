package com.liaoyin.lyproject.api;

import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.common.aop.user.UserAnnotation;
import com.liaoyin.lyproject.common.filter.filterAnnotaion.FilterAnnotation;
import com.liaoyin.lyproject.common.filter.repeatRequest.RepeatRequest;
import com.liaoyin.lyproject.service.MMateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：
 * 时间：2018/10/4 16:22
 * 描述：匹配单模块
 */
@Api(description = "匹配单模块")
@RestController
@RequestMapping(value = "/mate")
public class MMateController {

    @Autowired
    private MMateService mateService;

    /**
     * 作者：
     * 时间： 2018/10/4 16:26
     * 描述： 查询匹配单记录
     **/
    @ApiOperation(value = "查询匹配单记录", notes = "", response= String.class)
    @PostMapping(value = "selectMateRecord")
    @FilterAnnotation
    @UserAnnotation(description = "查询匹配记录")
    public JsonRestResponse selectMateRecord(HttpServletRequest request,
                                             @ApiParam(value = "排单id")@RequestParam(required = false) Integer planToonId,
                                             @ApiParam(value = "排单人id")@RequestParam(required = false) Integer planToonUserId,
                                             @ApiParam(value = "提现id")@RequestParam(required = false) Integer cashapplyId,
                                             @ApiParam(value = "提现人id")@RequestParam(required = false) Integer cashapplyUserId,
                                             @ApiParam(value = "0-等待付款   1-已打款   2-已确认收款",required = true)@RequestParam Integer status,
                                             @ApiParam(value = "0-排单与提现匹配的   1-抢单与提现匹配的")@RequestParam(required = false) Integer type,
                                             @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
                                             @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize){
        return mateService.selectMateRecord(planToonUserId,cashapplyUserId,status,startPage,pageSize,request,
                planToonId,cashapplyId,type);
    }

    /**
     * 作者：
     * 时间： 2018/10/4 16:40
     * 描述： 查询匹配单记录详情
     **/
    @ApiOperation(value = "查询匹配单记录详情", notes = "", response= String.class)
    @PostMapping(value = "selectMateRecordDetail")
    @FilterAnnotation
    @UserAnnotation(description = "查询匹配单记录详情")
    public JsonRestResponse selectMateRecordDetail(HttpServletRequest request,
                                             @ApiParam(value = "匹配单id",required = true)@RequestParam Integer mateId){
        return mateService.selectMateRecordDetail(mateId,request);
    }


    /**
     * 作者：
     * 时间： 2018/10/10 11:04
     * 描述： 打款上传凭证
     **/
    @ApiOperation(value = "打款上传凭证", notes = "", response= String.class)
    @PostMapping(value = "paymentUploadVoucher")
    @FilterAnnotation
    @RepeatRequest
    @UserAnnotation(description = "上传打款凭证")
    public synchronized JsonRestResponse paymentUploadVoucher(HttpServletRequest request,
                                                 @ApiParam(value = "匹配单id",required = true)@RequestParam(required = true) Integer mateId,
                                                 @ApiParam(value = "凭证链接，多个使用逗号隔开",required = true)@RequestParam(required = true) String prooffile)
    throws Exception{
        return mateService.paymentUploadVoucher(mateId,prooffile,request);
    }


    /**
     * 作者：
     * 时间： 2018/10/10 14:23
     * 描述： 匹配单确认收款
     **/
    @ApiOperation(value = "匹配单确认收款", notes = "", response= String.class)
    @PostMapping(value = "confirmGathering")
    @FilterAnnotation
    @RepeatRequest
    @UserAnnotation(description = "匹配单确认收款")
    public synchronized JsonRestResponse confirmGathering(HttpServletRequest request,
                                             @ApiParam(value = "匹配单id",required = true)@RequestParam(required = true) Integer mateId){
        return mateService.confirmGathering(request,mateId);
    }


}
