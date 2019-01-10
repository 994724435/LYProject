package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.MRob;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface MRobMapper extends Mapper<MRob> {

    /**
     * 作者：
     * 时间： 2018/10/9 15:32
     * 描述： 根据状态查询抢单
     **/
    MRob selectRobStatus(Integer status);

    /**
     * 作者：
     * 时间： 2018/10/9 15:32
     * 描述： 查询设置的抢单记录
     **/
    List<Map<String,Object>> selectRobAllToMap();

}