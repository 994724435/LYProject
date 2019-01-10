package com.liaoyin.lyproject.api;

import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.common.aop.user.UserAnnotation;
import com.liaoyin.lyproject.common.bean.SessionBean;
import com.liaoyin.lyproject.common.filter.filterAnnotaion.FilterAnnotation;
import com.liaoyin.lyproject.common.filter.repeatRequest.RepeatRequest;
import com.liaoyin.lyproject.exception.BusinessException;
import com.liaoyin.lyproject.service.MPlatoonOrderService;
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
 * 时间：2018/9/28 20:02
 * 描述：排单模块
 */
@Api(description = "排单模块")
@RestController
@RequestMapping(value = "/mplatoonOrder")
public class MPlatoonOrderController {

    @Autowired
    private MPlatoonOrderService platoonOrderService;

    /**
     * 作者：
     * 时间： 2018/9/28 20:08
     * 描述： 用户排单
     **/
    @ApiOperation(value = "用户排单", notes = "", response= String.class)
    @PostMapping(value = "userPlatoonOrder")
    @FilterAnnotation
    @RepeatRequest
    @UserAnnotation(description = "排单")
    public JsonRestResponse userPlatoonOrder(HttpServletRequest request,
                                             @ApiParam(value = "金额",required = true)@RequestParam Integer price){
        return platoonOrderService.userPlatoonOrder(request,price);
    }

    /**
     * 作者：
     * 时间： 2018/9/29 17:15
     * 描述：
     **/
    @ApiOperation(value = "用户排单记录", notes = "", response= String.class)
    @PostMapping(value = "queryUserPlatoonOrderRecord")
    @FilterAnnotation
    @UserAnnotation(description = "查询用户排单记录")
    public JsonRestResponse queryUserPlatoonOrderRecord(HttpServletRequest request,
                                                        @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
                                                        @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize,
                                                        @ApiParam(value = "0-等待付款   1-已打款   2-已确认收款  不传查全部")@RequestParam(required = false) Integer status,
                                                        @ApiParam(value = "0-匹配中  1-匹配完成   2-放弃")@RequestParam(required = false) Integer platoonStatus

    ){
        SessionBean bean = Config.getSessionBean(request);
        if (Common.isNull(bean)){
            throw new BusinessException("common.tokenInvalid");
        }
        return platoonOrderService.queryUserPlatoonOrderRecord(bean.getUid(),request,startPage,pageSize,status,platoonStatus);
    }

    /**
     * 作者：
     * 时间： 2018/10/13 20:24
     * 描述： 放弃排单
     **/
    @ApiOperation(value = "放弃排单", notes = "", response= String.class)
    @PostMapping(value = "givePlatoon")
    @FilterAnnotation
    @UserAnnotation(description = "放弃排单")
    public JsonRestResponse givePlatoon(HttpServletRequest request,
                                        @ApiParam(value = "排单id",required = true)@RequestParam Integer platoonId
    ){
        return platoonOrderService.givePlatoon(request,platoonId);
    }

}
