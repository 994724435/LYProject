package com.liaoyin.lyproject.api;

import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.common.filter.filterAnnotaion.FilterAnnotation;
import com.liaoyin.lyproject.common.filter.repeatRequest.RepeatRequest;
import com.liaoyin.lyproject.entity.Demo;
import com.liaoyin.lyproject.service.MSmsCodeService;
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
 * 时间：2018/9/25 9:45
 * 描述：验证码模块
 */
@Api(description = "验证码模块")
@RestController
@RequestMapping(value = "/msmscode")
public class MSmsCodeController {

    @Autowired
    private MSmsCodeService smsCodeService;

    /**
     * 作者：
     * 时间： 2018/9/25 10:06
     * 描述： 发送验证码
     **/
    @ApiOperation(value = "发送验证码", notes = "", response= String.class)
    @PostMapping(value = "sendCode")
    @RepeatRequest
    public synchronized JsonRestResponse sendCode(
            @ApiParam(value = "电话号码",required = true)@RequestParam String phone,
            @ApiParam(value = "发送类型：注册-"+Config.REGISTER+"  修改密码-"+Config.UPDATEPASSWORD+
                    "  忘记密码-"+Config.FORGETPASSWORD+"   添加子账号-"+Config.SAVESUNACCOUNT +"   系统用户登录"+
                    Config.SYSTEMUSERLOGIN+"  修改用户资料-"+Config.UPDATEUSERINFO,required = true)@RequestParam String type
            //@ApiParam(value = "",required = true)@RequestParam String type
    ){
        return smsCodeService.sendCode(phone,type);
    }

    /**
     * 作者：
     * 时间： 2018/9/25 15:25
     * 描述：验证验证码是否准确
     **/
    @ApiOperation(value = "验证验证码是否准确", notes = "", response= String.class)
    @PostMapping(value = "validateCodeIsOk")
    @RepeatRequest
    public JsonRestResponse validateCodeIsOk(
            @ApiParam(value = "电话号码",required = true)@RequestParam String phone,
            @ApiParam(value = "发送类型：注册-"+Config.REGISTER+"  修改密码-"+Config.UPDATEPASSWORD+
                    "  忘记密码-"+Config.FORGETPASSWORD+"   添加子账号-"+Config.SAVESUNACCOUNT +"   系统用户登录"+
                    Config.SYSTEMUSERLOGIN+"  修改用户资料-"+Config.UPDATEUSERINFO,required = true)@RequestParam String type,
            @ApiParam(value = "验证码",required = true)@RequestParam String code
    ){
        return smsCodeService.validateCodeIsOk(phone,type,code);
    }

}
