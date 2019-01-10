package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.SUserLoginLog;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SUserLoginLogMapper extends Mapper<SUserLoginLog> {

    List<SUserLoginLog> selectUserLoginLog(@Param("key")String key, @Param("startDate")Date startDate,
                                           @Param("endDate")Date endDate);

}