package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.SAccount;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface SAccountMapper extends Mapper<SAccount> {
    /**
     * 作者：
     * 时间： 2018/9/25 14:22
     * 描述： 根据用户id获取账户信息
     **/
    SAccount selectAccountUserId(Integer userId);

    /**
     * 作者：
     * 时间： 2018/11/5 11:05
     * 描述： 获取账户信息-行级锁
     **/
    SAccount selectAccountForUpdate(Integer userId);

    /**
     * 作者：
     * 时间： 2018/9/28 20:46
     * 描述： 直接扣除积分
     **/
    void updateAccountIntgralToReduce(@Param("userId") Integer userId, @Param("intgral") Integer intgral);

    /**
     * 作者：
     * 时间： 2018/9/28 21:35
     * 描述： 直接扣除余额
     **/
    void updateAccountPriceToReduce(@Param("userId") Integer userId,@Param("price") Integer price);

    /**
     * 作者：
     * 时间： 2018/11/22 10:56
     * 描述： 账户状态操作：0-正常 1-冻结
     **/
    void updateAccountStatusToUserId(@Param("userId") Integer userId,@Param("status") Integer status);

    /**
     * 作者：
     * 时间： 2018/11/29 15:01
     * 描述： 账户状态操作
     **/
    void updateAccountStatusTo(Integer status);

    /**
     * 作者：
     * 时间： 2018/11/29 15:01
     * 描述： 账户状态操作
     **/
    void updateAccountStatusAndUserIds(@Param("status") Integer status,@Param("userIds")String userIds);
}