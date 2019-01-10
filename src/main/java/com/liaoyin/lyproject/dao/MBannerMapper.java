package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.MBanner;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface MBannerMapper extends Mapper<MBanner> {

    /**
     * 作者：
     * 时间： 2018/9/25 17:02
     * 描述： 根据类型查询banner
     **/
    List<MBanner> selectBannerAllBannerType(Integer bannerType);

}