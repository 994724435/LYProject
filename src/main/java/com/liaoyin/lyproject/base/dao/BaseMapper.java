package com.liaoyin.lyproject.base.dao;

import tk.mybatis.mapper.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @项目名：公司内部模板项目
 * @作者：
 * @描述：demo持久层接口
 * @日期：Created in 2018/6/8 17:30
 */
public interface BaseMapper<T> extends  Mapper<T> , InsertListMapper<T> {



}