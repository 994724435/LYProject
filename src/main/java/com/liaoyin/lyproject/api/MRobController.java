package com.liaoyin.lyproject.api;

import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.common.aop.user.UserAnnotation;
import com.liaoyin.lyproject.common.bean.SessionBean;
import com.liaoyin.lyproject.common.filter.filterAnnotaion.FilterAnnotation;
import com.liaoyin.lyproject.exception.BusinessException;
import com.liaoyin.lyproject.service.MRobService;
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
 * 时间：2018/10/9 15:54
 * 描述：抢单模块
 */
@Api(description = "抢单模块")
@RestController
@RequestMapping(value = "/mrob")
public class MRobController {

    @Autowired
    private MRobService robService;

    /**
     * 作者：
     * 时间： 2018/10/9 15:57
     * 描述： 查询抢单
     **/
    @ApiOperation(value = "查询抢单", notes = "", response= String.class)
    @PostMapping(value = "selectRob")
    @FilterAnnotation
    public JsonRestResponse selectRob(HttpServletRequest request){
        return robService.selectRob(request);
    }

    /**
     * 作者：
     * 时间： 2018/10/9 16:03
     * 描述： 开始抢单
     **/
    @ApiOperation(value = "开始抢单", notes = "", response= String.class)
    @PostMapping(value = "executeRob")
    @FilterAnnotation
    @UserAnnotation(description = "提交抢单")
    public synchronized JsonRestResponse executeRob(HttpServletRequest request,
                                       @ApiParam(value = "金额",required = true)@RequestParam Integer price){
        return robService.executeRob(price,request);
    }

    /**
     * 作者：
     * 时间： 2018/10/10 17:53
     * 描述： 查询用户抢单情况
     **/
    @ApiOperation(value = "查询用户抢单情况", notes = "", response= String.class)
    @PostMapping(value = "selectUserRobOrderRecord")
    @FilterAnnotation
    @UserAnnotation(description = "查询抢单情况")
    public JsonRestResponse selectUserRobOrderRecord(HttpServletRequest request,
                                                    @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
                                                    @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize,
                                                     @ApiParam(value = "0-未匹配 1-已匹配 2-已完成")@RequestParam(required = false) Integer status
                                                     ){
        SessionBean bean = Config.getSessionBean(request);
        if (Common.isNull(bean)){
            throw new BusinessException("common.tokenInvalid");
        }
        return robService.selectUserRobOrderRecord(bean.getUid(),request,startPage,pageSize,status);
    }


}
