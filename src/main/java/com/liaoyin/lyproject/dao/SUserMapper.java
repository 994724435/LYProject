package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.*;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface SUserMapper extends Mapper<SUser> {

    int selectUserwxAccount(String wxAccount);

    int selectUserbankAccount(String bankAccount);

    int selectUserAlipay(String alipay);

    /**
     * 作者：
     * 时间： 2018/9/21 16:50
     * 描述： 根据电话号码查询
     **/
    SUser selectUserPhone(String phone);

    /**
     * 作者：
     * 时间： 2018/9/25 11:22
     * 描述： 根据电话号码查询多个用户_主账号与多个小号
     **/
    List<SUser> selectUserPhoneAll(String phone);

    /**
     * 作者：
     * 时间： 2018/9/29 15:36
     * 描述： 根据手机号获取主账号信息
     **/
    SUser selectArrangementUser(String phone);


    /**
     * 作者：
     * 时间： 2018/9/30 9:06
     * 描述： 根据令牌获取用户信息
     **/
    SUser selectUserInfoToken(@Param("token") String token,@Param("phone")String phone);

    /**
     * 作者：
     * 时间： 2018/9/21 16:50
     * 描述： 根据昵称查询
     **/
    SUser selectUserNickName(String nickName);

    /**
     * 作者：
     * 时间： 2018/9/25 10:54
     * 描述： 用户列表
     **/
    List<Map<String,Object>> selectUserListMap(@Param("key") String key, @Param("status") Integer status,
                                               @Param("refereeId") Integer refereeId,@Param("isDelete")Integer isDelete,
                                               @Param("arrangement")Integer arrangement);

    /**
     * 作者：
     * 时间： 2018/11/19 15:03
     * 描述： 后台拉取用户列表
     **/
    List<Map<String,Object>> selectUserListMapSystem(@Param("key") String key, @Param("status") Integer status,
                                               @Param("refereeId") Integer refereeId,@Param("isDelete")Integer isDelete,
                                               @Param("arrangement")Integer arrangement);

    /**
     * 作者：
     * 时间： 2018/9/25 10:54
     * 描述： 用户列表_推荐用户无限阶层
     **/
    List<Map<String,Object>> selectUserListMapChildLst(@Param("key") String key, @Param("status") Integer status,
                                               @Param("childLstUserId") String childLstUserId,@Param("isDelete")Integer isDelete);

    /**
     * 作者：
     * 时间： 2018/11/1 11:50
     * 描述： 根据电话号码精确查找用户
     **/
    List<Map<String,Object>> selectUserListMapAllPhone(String key);

    /**
     * 作者：
     * 时间： 2018/10/19 15:10
     * 描述： 查询属性节点id
     **/
    String selectUserIdChildLst(Integer userId);

    /**
     * 作者：
     * 时间： 2018/10/24 10:35
     * 描述： 封禁在指定时间内未排单的用户_基于已经排单的
     **/
    void updateNotPlanToonOrderTimeUser();

    /**
     * 作者：
     * 时间： 2018/10/24 10:35
     * 描述： 封禁在指定时间内未排单的用户_基于未排单的
     **/
    void updateNotPlanToonOrderTimeUserTwo();

    /**
     * 作者：
     * 时间： 2018/10/24 17:11
     * 描述： 拉取派单用户
     **/
    List<Map<String,Object>> selectDistributeOrderUser();


    List<Map<String,Object>> userZr();

    List<Map<String,Object>> giftIntegralRecord();

    List<Map<String,Object>> giftCashapply();

    List<MCashapplyOrder> giftPlatoonOrder();

    void clearAccount();

    List<MMate> selectAllMMate();

    Map<String,Object> giftPlatoonOrderOne(Integer userId);
    Map<String,Object> robOrder(Integer userId);
    MCashapplyOrder giftCashapplyOrderOne(Integer userId);

    List<SUser> selectAllsUserSA();
    List<MRobOrder> robOrderOut(Integer userId);
    List<MPlatoonOrder> platOrderOut(Integer userId);
    List<MMate> selectMateOut(Integer userId);

    /**
     * 作者：
     * 时间： 2018/11/21 17:41
     * 描述： 防撤资预警
     **/
    List<Map<String,Object>> selectWithdrawing();

    /**
     * 作者：
     * 时间： 2018/11/22 9:27
     * 描述： 防撤资人员
     **/
    List<Map<String,Object>> selectWithdrawingUser(@Param("userIds") String userIds);

    /**
     * 作者：
     * 时间： 2018/11/22 11:42
     * 描述： 查询注册警示列表
     **/
    List<Map<String,Object>> selectRegisterWarning();

    /**
     * 作者：
     * 时间： 2018/11/22 11:44
     * 描述： 查询警示列表人员
     **/
    List<Map<String,Object>> selectWarningUsers(Integer userId);

    /**
     * 作者：
     * 时间： 2018/12/3 10:06
     * 描述： 查询防撤资人员id集合
     **/
    String selectWarningUserIds();
}