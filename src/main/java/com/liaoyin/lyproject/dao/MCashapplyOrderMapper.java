package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.MCashapplyOrder;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface MCashapplyOrderMapper extends Mapper<MCashapplyOrder> {

    /**
     * 作者：
     * 时间： 2018/10/4 16:03
     * 描述： 拉取未匹配的提现
     **/
    List<MCashapplyOrder> selectCashapplyOrderNot();

    /**
     * 作者：
     * 时间： 2018/10/4 14:44
     * 描述： 查询用户提现记录
     **/
    List<Map<String,Object>> queryUserCashapplyOrderRecord(@Param("userId") Integer userId,
                                                           @Param("cashapplyStatus")Integer cashapplyStatus);

    /**
     * 作者：
     * 时间： 2018/10/4 13:24
     * 描述： 后台系统查询提现记录
     **/
    List<Map<String,Object>> selectCashapplyOrderSystem(@Param("key") String key, @Param("startDate") Date startDate,
                                                        @Param("endDate") Date endDate,@Param("mold")Integer mold);

    /**
     * 作者：
     * 时间： 2018/10/29 9:22
     * 描述： 查询提现人最后一次排单时间
     **/
    List<Map<String,Object>> selectCahsapplyOverPlanToonDate(@Param("key") String key, @Param("startDate") Date startDate,
                                                             @Param("endDate") Date endDate);

    /**
     * 作者：
     * 时间： 2018/11/19 17:37
     * 描述： 用户提现状态操作
     **/
    void updateCashapplyMold(@Param("userId")Integer userId,@Param("mold")Integer mold);

    /**
     * 作者：
     * 时间： 2018/11/29 15:00
     * 描述： 提现状态操作
     **/
    void updateCashapplyMoldIsSwitch(Integer mold);

    /**
     * 作者：
     * 时间： 2018/11/29 15:00
     * 描述： 提现状态操作
     **/
    void updateCashapplyMoldIsSwitchToUserIds(@Param("mold") Integer mold,@Param("userIds")String userIds);

    /**
     * 作者：
     * 时间： 2018/11/22 17:16
     * 描述： 查询用户最后一次排单数据
     **/
    MCashapplyOrder selectCashapplyEndDate(Integer userId);
}