package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.MJob;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface MJobMapper extends Mapper<MJob> {

    /**
     * 作者：
     * 时间： 2018/10/15 15:44
     * 描述： 查询未入款的工作调度
     **/
    List<MJob> selectJob();

    /**
     * 作者：
     * 时间： 2018/10/15 15:53
     * 描述： 根据匹配单查询对应的工作调度
     **/
    MJob selectJobToMateId(Integer mateId);

    /**
     * 作者：
     * 时间： 2018/10/31 17:48
     * 描述： 根据匹配单查询对应的工作调度
     **/
    List<MJob> selectJobListToMateId(Integer mateId);

    /**
     * 作者：
     * 时间： 2018/10/31 17:46
     * 描述：
     **/
    List<Map<String,Object>> selectErroData();
}