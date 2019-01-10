package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.base.dao.BaseMapper;
import com.liaoyin.lyproject.entity.Demo;

import java.util.List;
/**
 * @项目名：公司内部模板项目
 * @作者：
 * @描述：demo持久层接口
 * @日期：Created in 2018/6/8 17:30
 */
@org.apache.ibatis.annotations.Mapper
public interface DemoMapper extends BaseMapper<Demo> {
    /**
     * @方法名：selectDemoByPage
     * @描述： 分页查询
     * @作者：
     * @日期： Created in 2018/6/8 16:48
     */
    List<Demo> selectDemoByPage(Demo demo);



}