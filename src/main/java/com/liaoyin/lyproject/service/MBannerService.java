package com.liaoyin.lyproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.base.service.BaseService;
import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.dao.MBannerMapper;
import com.liaoyin.lyproject.entity.MBanner;
import com.liaoyin.lyproject.util.RestUtil;
import org.apache.ibatis.builder.BuilderException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 作者：
 * 时间：2018/9/25 16:41
 * 描述：
 */
@Service
public class MBannerService extends BaseService<MBannerMapper,MBanner> {



    public JsonRestResponse saveBanner(MBanner banner) {
        this.mapper.insertSelective(banner);
        return RestUtil.createResponse(this.mapper.selectOne(banner));
    }

    public JsonRestResponse updateBanner(MBanner banner) {
        this.mapper.updateByPrimaryKeySelective(banner);
        return RestUtil.createResponse(banner);
    }

    public JsonRestResponse deleteBanner(Integer id) {
        this.mapper.deleteByPrimaryKey(id);
        return RestUtil.createResponse();
    }

    public JsonRestResponse queryBanner(Integer bannertype, Integer startPage, Integer pageSize, Integer status) {
        JsonRestResponse result = new JsonRestResponse();
        switch (status){
            case 0://分页
                if (Common.isNull(startPage)||Common.isNull(pageSize)){
                    throw new BuilderException("common.page.not");
                }
                PageHelper.startPage(startPage, pageSize);
                List<MBanner> lists = this.mapper.selectBannerAllBannerType(bannertype);
                PageInfo<MBanner> p = new PageInfo<>(lists);
                result = RestUtil.createResponse(p);
                break;
            case 1://不分页
                result = RestUtil.createResponse(this.mapper.selectBannerAllBannerType(bannertype));
                break;
        }
        return result;
    }
}
