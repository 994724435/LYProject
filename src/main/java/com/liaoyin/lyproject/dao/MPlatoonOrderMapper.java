package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.MPlatoonOrder;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface MPlatoonOrderMapper extends Mapper<MPlatoonOrder> {

    /**
     * 作者：
     * 时间： 2018/10/4 16:01
     * 描述： 拉取未匹配的排单
     **/
    List<MPlatoonOrder> selectMplatoonOrderNot();

    /**
     * 作者：
     * 时间： 2018/9/29 17:17
     * 描述： 用户排单记录
     **/
    List<Map<String,Object>> selectMplatoonOrderRecord(@Param("userId") Integer userId,@Param("status")Integer status,
                                                       @Param("platoonStatus")Integer platoonStatus);

    /**
     * 作者：
     * 时间： 2018/10/4 13:19
     * 描述： 后台系统拉取排单记录
     **/
    List<Map<String,Object>> selectPlatoonOrderSystem(@Param("key") String key, @Param("startDate") Date startDate,
                                                      @Param("endDate") Date endDate,@Param("mold")Integer mold);

    /**
     * 作者：
     * 时间： 2018/10/13 17:31
     * 描述： 统计资金数
     **/
    Map<String,Object> selectSumPlatoonPriceToStatus(@Param("startDate") Date startDate,
                                             @Param("endDate") Date endDate);

    /**
     * 作者：
     * 时间： 2018/10/29 10:57
     * 描述： 统计排单人数
     **/
    BigDecimal selectPlantUserNum();

    /**
     * 作者：
     * 时间： 2018/11/2 9:48
     * 描述： 统计排单人数-主账号
     **/
    BigDecimal selectPlantUserLiveNum();

    /**
     * 作者：
     * 时间： 2018/11/2 9:48
     * 描述： 统计排单人数-子账号
     **/
    BigDecimal selectPlantUserSonNum();

    /**
     * 作者：
     * 时间： 2018/11/9 10:42
     * 描述： 拉取最后一次正在排单的数据
     **/
    MPlatoonOrder selectUserEndDatePlantoonOrder(Integer userId);

    /**
     * 作者：
     * 时间： 2018/11/20 17:09
     * 描述： 获取用户排单最大金额
     **/
    BigDecimal selectUserPlantMax(Integer userId);

    /**
     * 作者：
     * 时间： 2018/11/22 17:15
     * 描述： 拉取最后一次排单的数据
     **/
    MPlatoonOrder selectUserEndDatePlantoonOrders(Integer userId);

    /**
     * 作者：
     * 时间： 2018/11/29 14:41
     * 描述： 获取排单时间大于提现时间的数据
     **/
    List<MPlatoonOrder> selectUserPlantoonOrdersUserIds(@Param("userIds") String userIds);
}