package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.TSystemUser;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface TSystemUserMapper extends Mapper<TSystemUser> {
    /**
     * 作者：
     * 时间： 2018/9/30 16:09
     * 描述： 根据电话号码查询系统用户
     **/
    TSystemUser selectSystemUserPhone(String phone);

    /**
     * 作者：
     * 时间： 2018/10/10 12:38
     * 描述： 获取系统用户列表
     **/
    List<Map<String,Object>> selectSystemUserAll();
}