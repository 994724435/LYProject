package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.MOverTimeJob;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface MOverTimeJobMapper extends Mapper<MOverTimeJob> {

    /**
     * 作者：
     * 时间： 2018/10/22 11:17
     * 描述： 根据排单id和状态查询工作域
     **/
    MOverTimeJob selectOverTimeJobToMateIdAndStatus(@Param("mateId")Integer mateId,
                                                    @Param("mold")Integer mold);

    /**
     * 作者：
     * 时间： 2018/10/22 11:17
     * 描述： 根据排单id和状态查询工作域
     **/
    List<MOverTimeJob> selectOverTimeJobToMateIdAndStatusAll(@Param("mateId")Integer mateId,
                                                    @Param("mold")Integer mold);

    /**
     * 作者：
     * 时间： 2018/10/23 11:09
     * 描述： 从库里查出未执行的工作域
     **/
    List<MOverTimeJob> selectOverTimeJobAll();

    /**
     * 作者：
     * 时间： 2018/11/9 9:08
     * 描述： 检测错误的超时工作调度
     **/
    List<Map<String,Object>> selectOvertimeErroData();

}