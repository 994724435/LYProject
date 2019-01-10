package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.MSmsCode;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
@org.apache.ibatis.annotations.Mapper
public interface MSmsCodeMapper extends Mapper<MSmsCode> {
    /**
     * 作者：
     * 时间： 2018/9/25 10:18
     * 描述： 查询最新的验证码
     **/
    MSmsCode selectCodeNewest(@Param("phone") String phone, @Param("type") String type);
}