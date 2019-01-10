package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.SAccountRecord;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface SAccountRecordMapper extends Mapper<SAccountRecord> {

    /**
     * 作者：
     * 时间： 2018/9/26 9:37
     * 描述： 根据用户id查询对应的账户记录
     **/
    List<SAccountRecord> selectAccountRecordToUserId(@Param("userId") Integer userId, @Param("recordtype") Integer recordtype,
                                                     @Param("recordmold") Integer recordmold);

    /**
     * 作者：
     * 时间： 2018/10/22 17:29
     * 描述： 查询前端销售用户积分记录
     **/
    List<Map<String,Object>> selectSystemUserIntegralRecord(@Param("userId") Integer userId,
                                                            @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 作者：
     * 时间： 2018/10/22 17:34
     * 描述： 统计前端销售用户积分总额
     **/
    BigDecimal selectSystemUserIntegralNum(@Param("userId") Integer userId,
                                           @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}