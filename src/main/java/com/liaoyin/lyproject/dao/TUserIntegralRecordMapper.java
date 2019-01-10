package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.TUserIntegralRecord;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
@org.apache.ibatis.annotations.Mapper
public interface TUserIntegralRecordMapper extends Mapper<TUserIntegralRecord> {

    /**
     * 作者：
     * 时间： 2018/10/11 17:56
     * 描述： 获取积分充值记录
     **/
    List<Map<String,Object>> queryUserRechargeIntegralRecord(@Param("userId") Integer userId, @Param("systenUserId") Integer systenUserId,
                                                             @Param("key") String key,@Param("startDate")Date startDate,
                                                             @Param("endDate") Date endDate,@Param("status")Integer status);

    /**
     * 作者：
     * 时间： 2018/10/11 17:56
     * 描述： 获取充值的积分数
     **/
    BigDecimal queryUserRechargeIntegralRecordBigDecimal(@Param("userId") Integer userId, @Param("systenUserId") Integer systenUserId,
                                               @Param("key") String key, @Param("startDate")Date startDate,
                                               @Param("endDate") Date endDate,@Param("status")Integer status);
}