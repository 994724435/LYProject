package com.liaoyin.lyproject.api;

import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.common.filter.filterAnnotaion.FilterAnnotation;
import com.liaoyin.lyproject.common.filter.repeatRequest.RepeatRequest;
import com.liaoyin.lyproject.entity.MBanner;
import com.liaoyin.lyproject.service.MBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者：
 * 时间：2018/9/25 16:40
 * 描述：banner模块
 */
@Api(description = "banner模块")
@RestController
@RequestMapping(value = "/mbanner")
public class MBannerController {

    @Autowired
    private MBannerService bannerService;

    /**
     * 作者：
     * 时间： 2018/9/25 16:47
     * 描述： 添加banner
     **/
    @ApiOperation(value = "添加banner", notes = "", response= String.class)
    @PostMapping(value = "saveBanner")
    @RepeatRequest
    @FilterAnnotation
    public JsonRestResponse saveBanner(
            @ApiParam(value = "banner类型(字典表bannerType)",required = true)@RequestParam Integer bannertype,
            @ApiParam(value = "图片链接",required = true)@RequestParam String bannerimg,
            @ApiParam(value = "banner对象，可以为对象id，也可以为链接")@RequestParam(required = false) String bannerobject,
            @ApiParam(value = "展示顺序")@RequestParam(required = false) Integer bannerorder){
        MBanner banner = new MBanner();
        banner.setBannertype(bannertype);
        banner.setBannerimg(bannerimg);
        banner.setBannerobject(bannerobject);
        banner.setBannerorder(bannerorder);
        return bannerService.saveBanner(banner);
    }

    /**
     * 作者：
     * 时间： 2018/9/25 16:54
     * 描述： 修改banner
     **/
    @ApiOperation(value = "修改banner", notes = "", response= String.class)
    @PostMapping(value = "updateBanner")
    @RepeatRequest
    @FilterAnnotation
    public JsonRestResponse updateBanner(
            @ApiParam(value = "bannerid)",required = true)@RequestParam Integer id,
            @ApiParam(value = "banner类型(字典表bannerType)")@RequestParam(required = false) Integer bannertype,
            @ApiParam(value = "图片链接")@RequestParam(required = false) String bannerimg,
            @ApiParam(value = "banner对象，可以为对象id，也可以为链接")@RequestParam(required = false) String bannerobject,
            @ApiParam(value = "展示顺序")@RequestParam(required = false) Integer bannerorder){
        MBanner banner = new MBanner();
        banner.setId(id);
        banner.setBannertype(bannertype);
        banner.setBannerimg(bannerimg);
        banner.setBannerobject(bannerobject);
        banner.setBannerorder(bannerorder);
        return bannerService.updateBanner(banner);
    }

    /**
     * 作者：
     * 时间： 2018/9/25 16:56
     * 描述： 删除banner
     **/
    @ApiOperation(value = "删除banner", notes = "", response= String.class)
    @PostMapping(value = "deleteBanner")
    @RepeatRequest
    @FilterAnnotation
    public JsonRestResponse deleteBanner(
            @ApiParam(value = "bannerid)",required = true)@RequestParam Integer id){
        return bannerService.deleteBanner(id);
    }


    /**
     * 作者：
     * 时间： 2018/9/25 16:59
     * 描述： 查询banner
     **/
    @ApiOperation(value = "查询banner", notes = "", response= String.class)
    @PostMapping(value = "queryBanner")
    @FilterAnnotation
    public JsonRestResponse queryBanner(
            @ApiParam(value = "banner类型(字典表bannerType)")@RequestParam(required = false) Integer bannertype,
            @ApiParam(value = "当前页")@RequestParam(required = false) Integer startPage,
            @ApiParam(value = "显示条数")@RequestParam(required = false) Integer pageSize,
            @ApiParam(value = "是否分页查询 0-是   1-否",required = true)@RequestParam Integer status){
        return bannerService.queryBanner(bannertype,startPage,pageSize,status);
    }



}
