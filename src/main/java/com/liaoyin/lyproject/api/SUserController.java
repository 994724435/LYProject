package com.liaoyin.lyproject.api;

import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.aop.user.UserAnnotation;
import com.liaoyin.lyproject.common.filter.filterAnnotaion.FilterAnnotation;
import com.liaoyin.lyproject.common.filter.repeatRequest.RepeatRequest;
import com.liaoyin.lyproject.entity.SAccount;
import com.liaoyin.lyproject.entity.SUser;
import com.liaoyin.lyproject.service.SUserService;
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
 * 时间：2018/9/21 16:27
 * 描述：用户板块
 */
@Api(description = "用户板块")
@RestController
@RequestMapping(value = "/suser")
public class SUserController {

    @Autowired
    private SUserService userService;

    /**
     * 作者：
     * 时间： 2018/9/21 16:34
     * 描述： 用户注册
     **/
    @ApiOperation(value = "用户注册", notes = "", response= String.class)
    @PostMapping(value = "userRegister")
    @RepeatRequest
    public JsonRestResponse userRegister(
            @ApiParam(value = "电话号码",required = true)@RequestParam String userPhone,
            @ApiParam(value = "密码",required = true)@RequestParam String userPassword,
            @ApiParam(value = "真实姓名",required = true)@RequestParam String realName,
            @ApiParam(value = "银行卡卡号",required = true)@RequestParam String bankAccount,
            @ApiParam(value = "银行卡名称",required = true)@RequestParam String bankName,
            @ApiParam(value = "支付宝")@RequestParam(required = false) String aliAccount,
            @ApiParam(value = "微信")@RequestParam(required = false) String wxAccount,
            @ApiParam(value = "推荐人id")@RequestParam(required = false)Integer refereeId
            //@ApiParam(value = "验证码",required = true)@RequestParam String code
            ){
        SUser user = new SUser();
        user.setUseraccount(userPhone);
        user.setPassword(userPassword);
        user.setRealName(realName);
        SAccount account = new SAccount();
        account.setBankName(bankName);
        account.setBankaccount(bankAccount);
        account.setAlipayaccount(aliAccount);
        account.setWxpayaccount(wxAccount);
        return userService.userRegister(user,null,refereeId,account);
    }

    /**
     * 作者：
     * 时间： 2018/9/25 11:18
     * 描述： 用户登录
     **/
    @ApiOperation(value = "用户登录", notes = "", response= String.class)
    @PostMapping(value = "userLogin")
    @RepeatRequest
    public JsonRestResponse userLogin(
            @ApiParam(value = "账号",required = true)@RequestParam String userAccount,
            @ApiParam(value = "密码",required = true)@RequestParam String userPassword,
            HttpServletRequest request
    ){
        SUser user = new SUser();
        user.setUseraccount(userAccount);
        user.setPassword(Common.personEncrypt(userPassword));
        return userService.userLogin(user,request);
    }

    /**
     * 作者：
     * 时间： 2018/9/30 9:05
     * 描述：
     **/
    @ApiOperation(value = "子账号切换登录", notes = "", response= String.class)
    @PostMapping(value = "sonLogin")
    @RepeatRequest
    @FilterAnnotation
    @UserAnnotation(description = "子账号切换登录")
    public JsonRestResponse sonLogin(
            @ApiParam(value = "子账号令牌",required = true)@RequestParam String sonToken,
            HttpServletRequest request
    ){
        return userService.sonLogin(sonToken,request);
    }



    /**
     * 作者：
     * 时间： 2018/9/25 15:44
     * 描述： 修改密码
     **/
    @ApiOperation(value = "修改密码", notes = "", response= String.class)
    @PostMapping(value = "updatePassword")
    @RepeatRequest
    @FilterAnnotation
    @UserAnnotation(description = "修改密码")
    public JsonRestResponse updatePassword(
            @ApiParam(value = "用户id",required = true)@RequestParam Integer uid,
            @ApiParam(value = "验证码",required = true)@RequestParam String code,
            @ApiParam(value = "新密码",required = true)@RequestParam String newPassword
    ){
        SUser user = new SUser();
        user.setId(uid);
        user.setPassword(Common.personEncrypt(newPassword));
        return userService.updatePassword(user,code);
    }

    /**
     * 作者：
     * 时间： 2018/9/25 15:55
     * 描述： 忘记密码
     **/
    @ApiOperation(value = "忘记密码", notes = "", response= String.class)
    @PostMapping(value = "forgetPassword")
    @RepeatRequest
    public JsonRestResponse forgetPassword(
            @ApiParam(value = "电话号码",required = true)@RequestParam String phone,
            @ApiParam(value = "验证码",required = true)@RequestParam String code,
            @ApiParam(value = "新密码",required = true)@RequestParam String newPassword
    ){
        SUser user = new SUser();
        user.setUseraccount(phone);
        user.setPassword(Common.personEncrypt(newPassword));
        return userService.forgetPassword(user,code);
    }


    /**
     * 作者：
     * 时间： 2018/9/25 10:43
     * 描述： 用户列表
     **/
    @ApiOperation(value = "用户列表", notes = "", response= String.class)
    @PostMapping(value = "queryUser")
    @FilterAnnotation
    @UserAnnotation(description = "查询推荐人列表")
    public JsonRestResponse queryUser(
            @ApiParam(value = "检索值（电话号码、昵称）")@RequestParam(required = false) String key,
            @ApiParam(value = "0：未激活  1：激活  2：冻结  3：封号  4：黑名单")@RequestParam(required = false) Integer status,
            //@ApiParam(value = "推荐人id")@RequestParam(required = false) Integer refereeId,
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize,
            HttpServletRequest request
    ){
        return userService.queryUser(key,status,startPage,pageSize,request);
    }

    /**
     * 作者：
     * 时间： 2018/9/25 14:15
     * 描述： 用户详情
     **/
    @ApiOperation(value = "用户详情", notes = "", response= String.class)
    @PostMapping(value = "queryUserDetail")
    @FilterAnnotation
    @UserAnnotation(description = "用户详情")
    public JsonRestResponse queryUserDetail(
            @ApiParam(value = "用户id",required = true)@RequestParam Integer userId){
        return userService.queryUserDetail(userId);
    }


    /**
     * 作者：
     * 时间： 2018/9/25 16:06
     * 描述： 添加子账号
     **/
    @ApiOperation(value = "添加子账号", notes = "", response= String.class)
    @PostMapping(value = "saveSonAccount")
    @RepeatRequest
    @FilterAnnotation
    @UserAnnotation(description = "添加子账号")
    public synchronized JsonRestResponse saveSonAccount(
            @ApiParam(value = "昵称",required = true)@RequestParam String userNickName,
            @ApiParam(value = "真实姓名",required = true)@RequestParam String realName,
            @ApiParam(value = "密码",required = true)@RequestParam String userPassword,
            //@ApiParam(value = "验证码",required = true)@RequestParam String code,
            @ApiParam(value = "支付宝账号")@RequestParam(required = false) String alipayAccount,
            @ApiParam(value = "微信号")@RequestParam(required = false) String wxpayAccount,
            @ApiParam(value = "银行卡号")@RequestParam(required = false) String bankAccount,
            @ApiParam(value = "银行卡名称")@RequestParam(required = false) String bankName,
            HttpServletRequest request
            ){
        return userService.saveSonAccount(request,userNickName,userPassword,null,alipayAccount,wxpayAccount,bankAccount,bankName,realName);
    }

    /**
     * 作者：
     * 时间： 2018/9/29 21:14
     * 描述： 获取子账号
     **/
    @ApiOperation(value = "获取子账号", notes = "", response= String.class)
    @PostMapping(value = "querySonAccount")
    @FilterAnnotation
    @UserAnnotation(description = "查询子账号")
    public JsonRestResponse querySonAccount(HttpServletRequest request){
        return userService.querySonAccount(request);
    }

    /**
     * 作者：
     * 时间： 2018/9/26 9:51
     * 描述： 修改用户信息
     **/
    @ApiOperation(value = "修改用户信息", notes = "", response= String.class)
    @PostMapping(value = "updateUserInfo")
    @RepeatRequest
    @FilterAnnotation
    @UserAnnotation(description = "修改用户信息")
    public JsonRestResponse updateUserInfo(
            @ApiParam(value = "昵称")@RequestParam(required = false) String userNickName,
            @ApiParam(value = "二级密码")@RequestParam(required = false) String twopassword,
            @ApiParam(value = "支付宝账号")@RequestParam(required = false) String alipayAccount,
            @ApiParam(value = "微信号")@RequestParam(required = false) String wxpayAccount,
            @ApiParam(value = "银行卡号")@RequestParam(required = false) String bankAccount,
            @ApiParam(value = "银行卡名称")@RequestParam(required = false) String bankName,
            @ApiParam(value = "真实姓名")@RequestParam(required = false) String realName,
            @ApiParam(value = "是否允许撞单 0-允许 1-不允许")@RequestParam(required = false)Integer mold,
            @ApiParam(value = "验证码")@RequestParam(required = false) String code,
            HttpServletRequest request
    ){
        return userService.updateUserInfo(request,userNickName,twopassword,alipayAccount,wxpayAccount,bankAccount,
                realName,bankName,mold,code);
    }


    /**
     * 作者：
     * 时间： 2018/10/4 17:26
     * 描述： 用户激活
     **/
    @ApiOperation(value = "用户激活", notes = "", response= String.class)
    @PostMapping(value = "userActivation")
    @RepeatRequest
    @UserAnnotation(description = "激活用户")
    public JsonRestResponse updateUserInfo(
            @ApiParam(value = "激活用户id",required = true)@RequestParam Integer userId,
            HttpServletRequest request
    ){
        return userService.userActivation(userId,request);
    }

    /**
     * 作者：
     * 时间： 2018/10/4 18:03
     * 描述： 向被推荐用户赠送积分
     **/
    @ApiOperation(value = "向用户赠送积分", notes = "", response= String.class)
    @PostMapping(value = "giftIntegralToRefereeUser")
    @RepeatRequest
    @UserAnnotation(description = "赠送积分")
    public synchronized JsonRestResponse giftIntegralToRefereeUser(
            @ApiParam(value = "对方用户id",required = true)@RequestParam Integer refeeUserId,
            @ApiParam(value = "积分值",required = true)@RequestParam Integer integral,
            HttpServletRequest request
    ){
        return userService.giftIntegralToRefereeUser(refeeUserId,integral,request);
    }

    /**
     * 作者：
     * 时间： 2018/10/29 11:58
     * 描述： 版本更新
     **/
    @ApiOperation(value = "版本更新", notes = "", response= String.class)
    @PostMapping(value = "queryVersion")
    public JsonRestResponse queryVersion(
            @ApiParam(value = "当前版本号",required = true)@RequestParam Integer versionNum,
            HttpServletRequest request
    ){
        return userService.queryVersion(versionNum,request);
    }




//    @ApiOperation(value = "数据转入_用户", notes = "", response= String.class)
//    @PostMapping(value = "userZr")
//    public synchronized JsonRestResponse giftIntegralToRefereeUser(){
//        return userService.userZr();
//    }
//
//    @ApiOperation(value = "数据转入_积分记录", notes = "", response= String.class)
//    @PostMapping(value = "giftIntegralRecord")
//    public synchronized JsonRestResponse giftIntegralRecord(){
//        return userService.giftIntegralRecord();
//    }
//
//    @ApiOperation(value = "数据转入_提现", notes = "", response= String.class)
//    @PostMapping(value = "giftCashapply")
//    public synchronized JsonRestResponse giftCashapply(){
//        return userService.giftCashapply();
//    }
//
//    @ApiOperation(value = "数据整合_account", notes = "", response= String.class)
//    @PostMapping(value = "giftPlatoonOrder")
//    public synchronized JsonRestResponse giftPlatoonOrder(){
//        return userService.giftPlatoonOrder();
//    }
//
//    @ApiOperation(value = "数据整合_account_2", notes = "", response= String.class)
//    @PostMapping(value = "giftPlatoonOrderTwo")
//    public synchronized JsonRestResponse giftPlatoonOrderTwo(){
//        return userService.giftPlatoonOrderTwo();
//    }

//    @ApiOperation(value = "数据整合_account_2", notes = "", response= String.class)
//    @PostMapping(value = "giftPlatoonOrderTwo")
//    public synchronized JsonRestResponse giftPlatoonOrderTwo(){
//        return userService.giftPlatoonOrderTwo();
//    }
}
