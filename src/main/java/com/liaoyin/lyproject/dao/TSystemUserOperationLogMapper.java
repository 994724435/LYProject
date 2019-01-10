package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.TSystemUserOperationLog;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface TSystemUserOperationLogMapper extends Mapper<TSystemUserOperationLog> {
    /**
     * 作者：
     * 时间： 2018/9/30 16:39
     * 描述： 查询后台系统用户操作日志
     **/
    List<TSystemUserOperationLog> selectSystemUserLoginLog(@Param("key") String key, @Param("startDate") Date startDate,
                                                           @Param("endDate") Date endDate, @Param("status") Integer status);
}