package com.liaoyin.lyproject.api;

import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.aop.system.SystemUserAnnotation;
import com.liaoyin.lyproject.common.filter.filterAnnotaion.FilterAnnotation;
import com.liaoyin.lyproject.common.filter.repeatRequest.RepeatRequest;
import com.liaoyin.lyproject.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：
 * 时间：2018/9/30 10:51
 * 描述：后台管理系统板块
 */
@Api(description = "后台管理系统板块")
@RestController
@RequestMapping(value = "/system")
public class TSystemController {

    @Autowired
    private TSystemService systemService;
    @Autowired
    private SUserService userService;
    @Autowired
    private MPlatoonOrderService platoonOrderService;
    @Autowired
    private MCashapplyOrderService cashapplyOrderService;
    @Autowired
    private MRobService robService;
    @Autowired
    private SAccountService accountService;

    /**
     * 作者：
     * 时间： 2018/9/30 15:59
     * 描述： 系统用户登录
     **/
    @ApiOperation(value = "系统用户登录", notes = "", response= String.class)
    @PostMapping(value = "systemUserLogin")
    @RepeatRequest
    public JsonRestResponse systemUserLogin(
            @ApiParam(value = "电话号码",required = true)@RequestParam String phone,
            @ApiParam(value = "密码",required = true)@RequestParam String password,
            @ApiParam(value = "验证码",required = true)@RequestParam String code,
            @ApiParam(value = "当前登录用户的外网地址",required = true)@RequestParam String ip,
            HttpServletRequest request){
            return systemService.systemUserLogin(phone,password,code,ip,request);
    }


    /**
     * 作者：
     * 时间： 2018/10/10 12:38
     * 描述：获取系统用户列表
     **/
    @ApiOperation(value = "获取系统用户列表", notes = "", response= String.class)
    @PostMapping(value = "selectSystemUserAll")
    @FilterAnnotation
    @SystemUserAnnotation(description = "获取系统用户列表")
    public JsonRestResponse selectSystemUserAll(
            HttpServletRequest request){
        return systemService.selectSystemUserAll(request);
    }



    /**
     * 作者：
     * 时间： 2018/9/30 16:47
     * 描述： 查询后台系统用户操作日志
     **/
    @ApiOperation(value = "查询后台系统用户操作日志", notes = "", response= String.class)
    @PostMapping(value = "selectSystemUserLoginLog")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询后台系统用户操作日志")
    public JsonRestResponse selectSystemUserLoginLog(
            @ApiParam(value = "检索值（昵称、登录账号）")@RequestParam(required = false) String key,
            @ApiParam(value = "开始时间（yyyy-MM-dd）")@RequestParam(required = false) String startDate,
            @ApiParam(value = "结束时间（yyyy-MM-dd）")@RequestParam(required = false) String endDate,
            @ApiParam(value = "0-登录日志  1-操作日志  不传为查询全部")@RequestParam(required = false) Integer status,
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize,
            HttpServletRequest request
    ){
        return systemService.selectSystemUserLoginLog(key,startDate,endDate,startPage,pageSize,request,status);
    }

    /**
     * 作者：
     * 时间： 2018/9/25 10:43
     * 描述： 用户列表
     **/
    @ApiOperation(value = "用户列表", notes = "", response= String.class)
    @PostMapping(value = "queryUser")
    @FilterAnnotation
    @SystemUserAnnotation(description = "拉取用户列表")
    public JsonRestResponse queryUser(
            @ApiParam(value = "检索值（电话号码、昵称）")@RequestParam(required = false) String key,
            @ApiParam(value = "0：未激活  1：激活  2：冻结  3：封号  4：黑名单  5：客服列表")@RequestParam(required = false) Integer status,
            @ApiParam(value = "推荐人id")@RequestParam(required = false) Integer refereeId,
            @ApiParam(value = "是否禁用0-正常  1-禁用")@RequestParam(required = false) Integer isDelete,
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize
    ){
        return systemService.queryUser(key,status,refereeId,startPage,pageSize,isDelete);
    }

    /**
     * 作者：
     * 时间： 2018/10/9 17:56
     * 描述： 修改用户信息
     **/
    @ApiOperation(value = "修改用户信息", notes = "", response= String.class)
    @PostMapping(value = "updateUserInfo")
    @RepeatRequest
    @FilterAnnotation
    @SystemUserAnnotation(description = "修改了用户信息")
    public JsonRestResponse updateUserInfo(
            @ApiParam(value = "用户id",required = true)@RequestParam(required = true) Integer userId,
            @ApiParam(value = "账号")@RequestParam(required = false) String userAccount,
            @ApiParam(value = "昵称")@RequestParam(required = false) String userNickName,
            @ApiParam(value = "密码")@RequestParam(required = false) String password,
            @ApiParam(value = "二级密码")@RequestParam(required = false) String twopassword,
            @ApiParam(value = "支付宝账号")@RequestParam(required = false) String alipayAccount,
            @ApiParam(value = "微信号")@RequestParam(required = false) String wxpayAccount,
            @ApiParam(value = "银行卡号")@RequestParam(required = false) String bankAccount,
            @ApiParam(value = "银行卡名称")@RequestParam(required = false) String bankName,
            @ApiParam(value = "真实姓名")@RequestParam(required = false) String realName,
            @ApiParam(value = "是否禁用 0：否 1：是")@RequestParam(required = false) Integer isDelete,
            @ApiParam(value = "是否允许撞单 0-允许 1-不允许")@RequestParam(required = false)Integer mold,
            @ApiParam(value = "推荐人id")@RequestParam(required = false)Integer refereeId,
            HttpServletRequest request
    ){
        return systemService.updateUserInfo(userId,request,userNickName,twopassword,alipayAccount,wxpayAccount
                ,bankAccount,realName,bankName,mold,isDelete,password,userAccount,refereeId);
    }




    /**
     * 作者：
     * 时间： 2018/9/30 11:12
     * 描述：查询用户登录日志
     **/
    @ApiOperation(value = "查询普通用户登录日志", notes = "", response= String.class)
    @PostMapping(value = "selectUserLoginLog")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询普通用户登录日志")
    public JsonRestResponse selectUserLoginLog(
            @ApiParam(value = "检索值（昵称、登录账号）")@RequestParam(required = false) String key,
            @ApiParam(value = "开始时间（yyyy-MM-dd）")@RequestParam(required = false) String startDate,
            @ApiParam(value = "结束时间（yyyy-MM-dd）")@RequestParam(required = false) String endDate,
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize,
            HttpServletRequest request
    ){
        return systemService.selectUserLoginLog(key,startDate,endDate,startPage,pageSize,request);
    }

    /**
     * 作者：
     * 时间： 2018/10/4 14:48
     * 描述： 获取正在排单数与正在提现数
     **/
    @ApiOperation(value = "获取正在排单数与正在提现数", notes = "", response= String.class)
    @PostMapping(value = "selectPlatoonOrderNumAndCashapplyOrderNum")
    @FilterAnnotation
    @SystemUserAnnotation(description = "获取正在排单数与正在提现数")
    public JsonRestResponse selectPlatoonOrderNumAndCashapplyOrderNum(HttpServletRequest request){
        return systemService.selectPlatoonOrderNumAndCashapplyOrderNum(request);
    }

    /**
     * 作者：
     * 时间： 2018/10/4 13:15
     * 描述： 获取排单列表与提现列表
     **/
    @ApiOperation(value = "获取排单列表与提现列表", notes = "", response= String.class)
    @PostMapping(value = "selectPlatoonOrderAndCashapplyOrder")
    @FilterAnnotation
    @SystemUserAnnotation(description = "获取排单列表与提现列表")
    public JsonRestResponse selectPlatoonOrderAndCashapplyOrder(
            @ApiParam(value = "检索值（排单人昵称、账号 or 提现人昵称、账号）")@RequestParam(required = false) String key,
            @ApiParam(value = "开始时间（yyyy-MM-dd）")@RequestParam(required = false) String startDate,
            @ApiParam(value = "结束时间（yyyy-MM-dd）")@RequestParam(required = false) String endDate,
            @ApiParam(value = "0-匹配中  1-匹配完成  2-放弃")@RequestParam(required = false) Integer mold,
            @ApiParam(value = "0-排单  1-提现",required = true)@RequestParam Integer status,
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize,
            HttpServletRequest request
    ){
        return systemService.selectPlatoonOrderAndCashapplyOrder(key,startDate,endDate,startPage,pageSize,request,status,mold);
    }

    /**
     * 作者：
     * 时间： 2018/10/4 13:34
     * 描述： 排单与提现进行匹配
     **/
    @ApiOperation(value = "排单与提现进行匹配", notes = "", response= String.class)
    @PostMapping(value = "matePlatoonOrderAndCashapplyOrder")
    @FilterAnnotation
    @RepeatRequest
    @SystemUserAnnotation(description = "排单与提现匹配")
    public synchronized JsonRestResponse matePlatoonOrderAndCashapplyOrder(
            @ApiParam(value = "匹配数量")@RequestParam(required = false) Integer num,
            @ApiParam(value = "提现记录id")@RequestParam(required = false) Integer cashapplyId,
            @ApiParam(value = "0-根据数值匹配   1-指定提现记录匹配",required = true)@RequestParam Integer status,
            HttpServletRequest request
    ){
        return systemService.matePlatoonOrderAndCashapplyOrder(num,cashapplyId,status,request);
    }

    /**
     * 作者：
     * 时间： 2018/10/4 14:24
     * 描述： 用户系列操作
     **/
    @ApiOperation(value = "用户系列操作", notes = "", response= String.class)
    @PostMapping(value = "userSeriesOperation")
    @FilterAnnotation
    @RepeatRequest
    @SystemUserAnnotation(description = "用户系列操作")
    public synchronized JsonRestResponse userSeriesOperation(
            @ApiParam(value = "用户id",required = true)@RequestParam Integer userId,
            @ApiParam(value = "0-让用户处于未激活状态  1-激活  2-冻结  3-封号  4-黑名单",required = true)@RequestParam Integer status,
            HttpServletRequest request
    ){
        return systemService.userSeriesOperation(userId,status,request);
    }

    /**
     * 作者：
     * 时间： 2018/10/9 11:21
     * 描述： 发起账号锁定投票
     **/
    @ApiOperation(value = "发起账号锁定投票", notes = "", response= String.class)
    @PostMapping(value = "createSystemUserLockVote")
    @FilterAnnotation
    @RepeatRequest
    @SystemUserAnnotation(description = "发起账号锁定投票")
    public synchronized JsonRestResponse createSystemUserLockVote(
            @ApiParam(value = "被锁定用户id",required = true)@RequestParam Integer systemUserId,
            HttpServletRequest request
    ){
        return systemService.createSystemUserLockVote(systemUserId,request);
    }


    /**
     * 作者：
     * 时间： 2018/10/10 14:40
     * 描述： 查询账号锁定投票记录
     **/
    @ApiOperation(value = "查询账号锁定投票记录", notes = "", response= String.class)
    @PostMapping(value = "selectSystemUserLockVote")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询账号锁定投票记录")
    public JsonRestResponse selectSystemUserLockVote(
            //@ApiParam(value = "投票状态：0-投票中   1-已关闭投票",required = true)@RequestParam(required = true) Integer status,
            @ApiParam(value = "当前页",required = true)@RequestParam(required = true) Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam(required = true) Integer pageSize,
            HttpServletRequest request
    ){
        return systemService.selectSystemUserLockVote(request,startPage,pageSize);
    }

    /**
     * 作者：
     * 时间： 2018/10/10 15:07
     * 描述： 执行投票
     **/
    @ApiOperation(value = "执行投票", notes = "", response= String.class)
    @PostMapping(value = "systemUserVote")
    @FilterAnnotation
    @RepeatRequest
    @SystemUserAnnotation(description = "执行投票")
    public synchronized JsonRestResponse systemUserVote(
            @ApiParam(value = "投票操作：0-同意   1-不同意",required = true)@RequestParam(required = true) Integer status,
            HttpServletRequest request
    ){
        return systemService.systemUserVote(request,status);
    }

    /**
     * 作者：
     * 时间： 2018/10/9 14:57
     * 描述： 发起抢单
     **/
    @ApiOperation(value = "发起抢单", notes = "", response= String.class)
    @PostMapping(value = "launchRob")
    @FilterAnnotation
    @RepeatRequest
    @SystemUserAnnotation(description = "发起抢单")
    public synchronized JsonRestResponse launchRob(
            @ApiParam(value = "金额",required = true) @RequestParam(required = true) Integer price,
            HttpServletRequest request
    ){
        return systemService.launchRob(price,request);
    }

    /**
     * 作者：
     * 时间： 2018/10/9 15:27
     * 描述： 查询设置的抢单记录
     **/
    @ApiOperation(value = "查询设置的抢单记录", notes = "", response= String.class)
    @PostMapping(value = "selectRob")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询设置的抢单记录")
    public JsonRestResponse selectRob(
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize,
            HttpServletRequest request
    ){
        return systemService.selectRob(startPage,pageSize,request);
    }

    /**
     * 作者：
     * 时间： 2018/10/9 15:43
     * 描述： 查询抢单匹配记录
     **/
    @ApiOperation(value = "查询抢单匹配记录", notes = "", response= String.class)
    @PostMapping(value = "selectRobOrder")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询抢单匹配记录")
    public JsonRestResponse selectRobOrder(
            @ApiParam(value = "检索值")@RequestParam(required = false) String key,
            @ApiParam(value = "批次号")@RequestParam(required = false) String batchNum,
            @ApiParam(value = "0-未排单  1-已排单")@RequestParam(required = false) Integer status,
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize,
            HttpServletRequest request
    ){
        return systemService.selectRobOrder(key,batchNum,startPage,pageSize,request,status);
    }

    /**
     * 作者：
     * 时间： 2018/10/9 17:17
     * 描述： 指定抢单记录进行匹配
     **/
    @ApiOperation(value = "指定抢单记录进行匹配", notes = "", response= String.class)
    @PostMapping(value = "specifyRobOrderMate")
    @FilterAnnotation
    @SystemUserAnnotation(description = "指定抢单记录进行匹配")
    public synchronized JsonRestResponse specifyRobOrderMate(
            @ApiParam(value = "记录id",required = true)@RequestParam Integer id,
            HttpServletRequest request
    ){
        return systemService.specifyRobOrderMate(id,request);
    }

    /**
     * 作者：
     * 时间： 2018/10/22 14:29
     * 描述： 指定匹配
     **/
    @ApiOperation(value = "指定匹配", notes = "", response= String.class)
    @PostMapping(value = "specifiedMatching")
    @FilterAnnotation
    @SystemUserAnnotation(description = "指定抢单与提现进行匹配")
    public synchronized JsonRestResponse specifiedMatching(
            @ApiParam(value = "排单、抢单id",required = true)@RequestParam Integer planToonId,
            @ApiParam(value = "提现id",required = true)@RequestParam Integer cashapplyId,
            @ApiParam(value = "0-排单与提现匹配的   1-抢单与提现匹配的",required = true)@RequestParam Integer status,
            HttpServletRequest request
    ){
        return systemService.specifiedMatching(planToonId,cashapplyId,status,request);
    }


    /**
     * 作者：
     * 时间： 2018/10/10 18:24
     * 描述： 查询匹配记录
     **/
    @ApiOperation(value = "查询匹配记录", notes = "", response= String.class)
    @PostMapping(value = "selectMateRecordToAll")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询匹配记录")
    public JsonRestResponse selectMateRecordToAll(
            @ApiParam(value = "检索值（排单人昵称、账号 or 提现人昵称、账号）")@RequestParam(required = false) String key,
            @ApiParam(value = "开始时间（yyyy-MM-dd）")@RequestParam(required = false) String startDate,
            @ApiParam(value = "结束时间（yyyy-MM-dd）")@RequestParam(required = false) String endDate,
            @ApiParam(value = "0-等待付款   1-已打款   2-已确认收款")@RequestParam(required = false) Integer status,
            @ApiParam(value = "0-正常 1-打款超时  2-确认收款超时")@RequestParam(required = false) Integer mold,
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize,
            HttpServletRequest request
    ){
        return systemService.selectMateRecordToAll(key,startDate,endDate,startPage,pageSize,request,status,mold);
    }

    /**
     * 作者：
     * 时间： 2018/10/22 11:44
     * 描述： 删除超时记录
     **/
    @ApiOperation(value = "操作超时记录", notes = "", response= String.class)
    @PostMapping(value = "overtimeRecordOperation")
    @FilterAnnotation
    @SystemUserAnnotation(description = "对超时记录进行操作")
    public JsonRestResponse overtimeRecordOperation(
            @ApiParam(value = "匹配单id",required = true)@RequestParam Integer mateId,
            @ApiParam(value = "0-删除匹配（该操作只能在打款超时做按钮）  1-强制确认收款（该操作只能在确认收款超时做按钮）",
                    required = true)@RequestParam Integer type,
            HttpServletRequest request
    ){
        return systemService.overtimeRecordOperation(mateId,type,request);
    }

    /**
     * 作者：
     * 时间： 2018/10/23 16:19
     * 描述： 平台接单
     **/
    @ApiOperation(value = "平台接单", notes = "", response= String.class)
    @PostMapping(value = "platformReceipt")
    @FilterAnnotation
    @SystemUserAnnotation(description = "平台接单")
    public synchronized JsonRestResponse platformReceipt(
            @ApiParam(value = "匹配单id",required = true)@RequestParam Integer mateId,
            @ApiParam(value = "指定人id",required = true)@RequestParam Integer userId,
            HttpServletRequest request
    ){
        return systemService.platformReceipt(mateId,userId,request);
    }



    /**
     * 作者：
     * 时间： 2018/10/24 16:48
     * 描述： 平台派单
     **/
    @ApiOperation(value = "派单", notes = "", response= String.class)
    @PostMapping(value = "distributeOrder")
    @FilterAnnotation
    @SystemUserAnnotation(description = "平台派单")
    public synchronized JsonRestResponse distributeOrder(
            @ApiParam(value = "排单id或抢单id",required = true)@RequestParam Integer oid,
            @ApiParam(value = "0-排单 1-抢单",required = true)@RequestParam Integer status,
            @ApiParam(value = "指定人电话",required = true)@RequestParam String phone,
            HttpServletRequest request
    ){
        return systemService.distributeOrder(oid,status,phone,request);
    }

    /**
     * 作者：
     * 时间： 2018/10/24 17:08
     * 描述： 拉取派单人员列表
     **/
    @ApiOperation(value = "拉取派单人员列表", notes = "", response= String.class)
    @PostMapping(value = "selectDistributeOrderUser")
    @FilterAnnotation
    @SystemUserAnnotation(description = "拉取派单人员列表")
    public JsonRestResponse selectDistributeOrderUser(){
        return systemService.selectDistributeOrderUser();
    }


    /**
     * 作者：
     * 时间： 2018/10/11 10:10
     * 描述： 用户详情
     **/
    @ApiOperation(value = "用户详情", notes = "", response= String.class)
    @PostMapping(value = "queryUserDetail")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询用户详情")
    public JsonRestResponse queryUserDetail(
            @ApiParam(value = "用户id",required = true)@RequestParam Integer userId){
        return userService.queryUserDetail(userId);
    }

    /**
     * 作者：
     * 时间： 2018/10/11 11:16
     * 描述： 用户排单记录
     **/
    @ApiOperation(value = "用户排单记录", notes = "", response= String.class)
    @PostMapping(value = "queryUserPlatoonOrderRecord")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询用户排单记录")
    public JsonRestResponse queryUserPlatoonOrderRecord(HttpServletRequest request,
                                                        @ApiParam(value = "用户id",required = true)@RequestParam Integer userId,
                                                        @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
                                                        @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize,
                                                        @ApiParam(value = "0-等待付款   1-已打款   2-已确认收款  不传查全部")@RequestParam(required = false) Integer status,
                                                        @ApiParam(value = "0-匹配中  1-匹配完成   2-放弃")@RequestParam(required = false) Integer platoonStatus
    ){
        return platoonOrderService.queryUserPlatoonOrderRecord(userId,request,startPage,pageSize,status,platoonStatus);
    }

    /**
     * 作者：
     * 时间： 2018/10/11 11:18
     * 描述： 用户提现记录
     **/
    @ApiOperation(value = "用户提现记录", notes = "", response= String.class)
    @PostMapping(value = "queryUserCashapplyOrderRecord")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询用户提现记录")
    public JsonRestResponse queryUserCashapplyOrderRecord(HttpServletRequest request,
                                                          @ApiParam(value = "用户id",required = true)@RequestParam Integer userId,
                                                          @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
                                                          @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize,
                                                          @ApiParam(value = "0-匹配中  1-匹配完成   2-放弃")@RequestParam(required = false) Integer cashapplyStatus
    ){
        return cashapplyOrderService.queryUserCashapplyOrderRecord(userId,request,startPage,pageSize,cashapplyStatus);
    }

    /**
     * 作者：
     * 时间： 2018/10/11 11:20
     * 描述：
     **/
    @ApiOperation(value = "查询用户抢单记录", notes = "", response= String.class)
    @PostMapping(value = "selectUserRobOrderRecord")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询用户抢单记录")
    public JsonRestResponse selectUserRobOrderRecord(HttpServletRequest request,
                                                     @ApiParam(value = "用户id",required = true)@RequestParam Integer userId,
                                                     @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
                                                     @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize){
        return robService.selectUserRobOrderRecord(userId,request,startPage,pageSize,null);
    }

    /**
     * 作者：
     * 时间： 2018/10/11 11:24
     * 描述： 获取用户的账户记录
     **/
    @ApiOperation(value = "获取用户的账户记录", notes = "", response= String.class)
    @PostMapping(value = "queryUserAccountRecord")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询用户的账户记录")
    public JsonRestResponse queryUserAccountRecord(
            @ApiParam(value = "用户id",required = true)@RequestParam Integer userId,
            @ApiParam(value = "0-金额  1-积分")@RequestParam(required = false) Integer recordtype,
            @ApiParam(value = "0-排单 1-抢单 2-收款  3-提现  4-赠送(接收)积分")@RequestParam(required = false) Integer recordmold,
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize
    ){
        return accountService.queryUserAccountRecord(userId,recordtype,recordmold,startPage,pageSize);
    }

    /**
     * 作者：
     * 时间： 2018/10/11 17:31
     * 描述： 积分充值
     **/
    @ApiOperation(value = "积分操作", notes = "", response= String.class)
    @PostMapping(value = "rechargeIntegral")
    @FilterAnnotation
    @RepeatRequest
    @SystemUserAnnotation(description = "积分操作")
    public synchronized JsonRestResponse rechargeIntegral(
            HttpServletRequest request,
            @ApiParam(value = "用户id",required = true)@RequestParam Integer userId,
            @ApiParam(value = "积分值",required = true)@RequestParam Integer integral,
            @ApiParam(value = "0-增加  1-减少  为空表示‘增加’")@RequestParam(required = false) Integer status
    ){
        if (Common.isNull(status)) status=0;
        return systemService.rechargeIntegral(userId,integral,request,status);
    }

    /**
     * 作者：
     * 时间： 2018/10/11 17:52
     * 描述： 获取积分充值记录
     **/
    @ApiOperation(value = "获取积分充值记录", notes = "", response= String.class)
    @PostMapping(value = "queryUserRechargeIntegralRecord")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询积分充值记录")
    public JsonRestResponse queryUserRechargeIntegralRecord(
            @ApiParam(value = "用户id")@RequestParam(required = false) Integer userId,
            @ApiParam(value = "系统用户id")@RequestParam(required = false) Integer systenUserId,
            @ApiParam(value = "检索值(系统用户名称、电话 or 普通用户名称、电话)")@RequestParam(required = false) String key,
            @ApiParam(value = "开始时间（yyyy-MM-dd）")@RequestParam(required = false) String startDate,
            @ApiParam(value = "结束时间（yyyy-MM-dd）")@RequestParam(required = false) String endDate,
            @ApiParam(value = "0-增加  1-减少")@RequestParam(required = false) Integer status,
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize
    ){
        return systemService.queryUserRechargeIntegralRecord(userId,systenUserId,key,startPage,pageSize,startDate,endDate,status);
    }

    /**
     * 作者：
     * 时间： 2018/10/13 17:24
     * 描述： 统计首页数据
     **/
    @ApiOperation(value = "统计首页数据", notes = "", response= String.class)
    @PostMapping(value = "statisticsHomeData")
    @FilterAnnotation
    public JsonRestResponse statisticsHomeData(
            @ApiParam(value = "开始时间（yyyy-MM-dd HH:mm:ss）")@RequestParam(required = false) String startDate,
            @ApiParam(value = "结束时间（yyyy-MM-dd HH:mm:ss）")@RequestParam(required = false) String endDate
    ){
        return systemService.statisticsHomeData(startDate,endDate);
    }

    /**
     * 作者：
     * 时间： 2018/10/13 18:15
     * 描述： 删除排单
     **/
    @ApiOperation(value = "删除排单", notes = "", response= String.class)
    @PostMapping(value = "deletePlatoon")
    @FilterAnnotation
    @SystemUserAnnotation(description = "删除排单")
    public JsonRestResponse deletePlatoon(
            @ApiParam(value = "排单id",required = true)@RequestParam Integer platoonId
    ){
        return systemService.deletePlatoon(platoonId);
    }

    /**
     * 作者：
     * 时间： 2018/10/13 18:20
     * 描述： 删除提现
     **/
    @ApiOperation(value = "删除提现", notes = "", response= String.class)
    @PostMapping(value = "deleteCashpply")
    @FilterAnnotation
    @SystemUserAnnotation(description = "删除提现")
    public JsonRestResponse deleteCashpply(
            @ApiParam(value = "提现id",required = true)@RequestParam Integer cashapplyId
    ){
        return systemService.deleteCashpply(cashapplyId);
    }

    /**
     * 作者：
     * 时间： 2018/10/22 17:22
     * 描述： 查询管理员前台积分销量
     **/
    @ApiOperation(value = "查询管理员前台积分销量", notes = "", response= String.class)
    @PostMapping(value = "selectSystemUserIntegralNum")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询了管理员前台积分销量")
    public JsonRestResponse selectSystemUserIntegralNum(
            @ApiParam(value = "管理员电话",required = true)@RequestParam String phone,
            @ApiParam(value = "开始时间（yyyy-MM-dd HH:mm:ss）")@RequestParam(required = false) String startDate,
            @ApiParam(value = "结束时间（yyyy-MM-dd HH:mm:ss）")@RequestParam(required = false) String endDate,
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize
    ){
        return systemService.selectSystemUserIntegralNum(phone,startDate,endDate,startPage,pageSize);
    }

    /**
     * 作者：
     * 时间： 2018/10/29 9:10
     * 描述： 查询提现人最后一次排单时间
     **/
    @ApiOperation(value = "查询提现人最后一次排单时间", notes = "", response= String.class)
    @PostMapping(value = "selectCahsapplyOverPlanToonDate")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询了提现列表对应人的最后一次排单时间")
    public JsonRestResponse selectCahsapplyOverPlanToonDate(
            @ApiParam(value = "检索值（提现人昵称、账号）")@RequestParam(required = false) String key,
            @ApiParam(value = "开始时间（yyyy-MM-dd HH:mm:ss）")@RequestParam(required = false) String startDate,
            @ApiParam(value = "结束时间（yyyy-MM-dd HH:mm:ss）")@RequestParam(required = false) String endDate,
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize
    ){
        return systemService.selectCahsapplyOverPlanToonDate(key,startDate,endDate,startPage,pageSize);
    }

    /**
     * 作者：
     * 时间： 2018/11/21 17:38
     * 描述： 查询防撤组
     **/
    @ApiOperation(value = "防撤资预警", notes = "", response= String.class)
    @PostMapping(value = "selectWithdrawing")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询了防撤资预警")
    public JsonRestResponse selectWithdrawing(
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize
    ){
        return systemService.selectWithdrawing(startPage,pageSize);
    }

    /**
     * 作者：
     * 时间： 2018/11/22 9:13
     * 描述： 防撤资人员列表
     **/
    @ApiOperation(value = "防撤资人员列表", notes = "", response= String.class)
    @PostMapping(value = "selectWithdrawingUser")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询防撤资人员列表")
    public JsonRestResponse selectWithdrawingUser(
            @ApiParam(value = "总用户",required = true)@RequestParam String userIds,
            @ApiParam(value = "部分用户",required = true)@RequestParam String userIdsTwo,
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize
    ){
        return systemService.selectWithdrawingUser(userIds,userIdsTwo,startPage,pageSize);
    }

    /**
     * 作者：
     * 时间： 2018/11/22 9:59
     * 描述： 提现封禁
     **/
    @ApiOperation(value = "提现封禁操作", notes = "", response= String.class)
    @PostMapping(value = "cashapplyClosure")
    @FilterAnnotation
    @SystemUserAnnotation(description = "提现封禁操作")
    public JsonRestResponse cashapplyClosure(
            HttpServletRequest request,
            @ApiParam(value = "0-解禁  1-封禁",required = true)@RequestParam Integer status,
            @ApiParam(value = "用户id串",required = true)@RequestParam String userIds){
        return systemService.cashapplyClosure(status,userIds);
    }


    /**
     * 作者：
     * 时间： 2018/11/22 11:40
     * 描述： 注册警示列表
     **/
    @ApiOperation(value = "注册警示列表", notes = "", response= String.class)
    @PostMapping(value = "selectRegisterWarning")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询注册警示列表")
    public JsonRestResponse selectRegisterWarning(
            HttpServletRequest request,
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize){
        return systemService.selectRegisterWarning(request,startPage,pageSize);
    }

    /**
     * 作者：
     * 时间： 2018/11/22 11:43
     * 描述： 查询警示列表人员
     **/
    @ApiOperation(value = "查询警示列表人员", notes = "", response= String.class)
    @PostMapping(value = "selectWarningUsers")
    @FilterAnnotation
    @SystemUserAnnotation(description = "查询警示列表人员")
    public JsonRestResponse selectWarningUsers(
            HttpServletRequest request,
            @ApiParam(value = "警示列人员id",required = true)@RequestParam Integer userId,
            @ApiParam(value = "当前页",required = true)@RequestParam Integer startPage,
            @ApiParam(value = "显示条数",required = true)@RequestParam Integer pageSize){
        return systemService.selectWarningUsers(request,startPage,pageSize,userId);
    }
}
