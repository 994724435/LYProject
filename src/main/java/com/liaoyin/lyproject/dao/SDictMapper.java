package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.SDict;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SDictMapper extends Mapper<SDict> {

    /**
     * 作者：
     * 时间： 2018/9/25 16:14
     * 描述： 根据编码查询单一
     **/
    SDict selectDict(String code);

    /**
     * 作者：
     * 时间： 2018/9/25 16:14
     * 描述：根据编码查询集合
     **/
    List<SDict> selectDictAll(String code);

    /**
     * 作者：
     * 时间： 2018/10/9 18:08
     * 描述： 查询所有字典
     **/
    List<SDict> selectDictDictionary();

}