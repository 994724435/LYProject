package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.MAgreement;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface MAgreementMapper extends Mapper<MAgreement> {

    /**
     * 作者：
     * 时间： 2018/9/25 14:55
     * 描述： 根据类型查询协议
     **/
    MAgreement selectAgreement(Integer type);

}