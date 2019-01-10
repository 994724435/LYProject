package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.MRobOrder;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface MRobOrderMapper extends Mapper<MRobOrder> {

    /**
     * 作者：
     * 时间： 2018/10/9 15:38
     * 描述： 查询抢单匹配记录
     **/
    List<Map<String,Object>> selectRobOrderAllMap(@Param("key") String key, @Param("batchNum") String batchNum,
                                                  @Param("status")Integer status);

    /**
     * 作者：
     * 时间： 2018/10/10 17:54
     * 描述： 查询用户抢单情况
     **/
    List<Map<String,Object>> selectUserRobOrderRecord(@Param("userId") Integer userId,@Param("status")Integer status);

    /**
     * 作者：
     * 时间： 2018/10/10 17:54
     * 描述： 查询用户抢单情况总金额
     **/
    BigDecimal selectUserRobOrderRecordSumPrice(@Param("userId") Integer userId,@Param("status")Integer status);

    /**
     * 作者：
     * 时间： 2018/10/14 22:58
     * 描述： 根据状态查询用户抢单情况
     **/
    List<MRobOrder> selectUserAllRobOrderToStatus(@Param("userId") Integer userId,@Param("status") Integer status);

    /**
     * 作者：
     * 时间： 2018/11/9 10:27
     * 描述： 查询用户最后一次抢单记录
     **/
    MRobOrder selectUserRobOrderDateDesc(Integer userId);
}