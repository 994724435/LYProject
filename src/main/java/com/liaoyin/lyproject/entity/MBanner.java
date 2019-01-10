package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "m_banner")
public class MBanner {
    /**
     * id主键
     */
    @Id
    private Integer id;

    /**
     * 展示顺序
     */
    @Column(name = "bannerOrder")
    private Integer bannerorder;

    /**
     * banner类型
     */
    @Column(name = "bannerType")
    private Integer bannertype;

    /**
     * banner图片链接
     */
    @Column(name = "bannerImg")
    private String bannerimg;

    /**
     * banner对象，可以为对象id，也可以为链接
     */
    @Column(name = "bannerObject")
    private String bannerobject;

    /**
     * 创建时间
     */
    @Column(name = "createDate")
    private Date createdate;

    /**
     * 获取id主键
     *
     * @return id - id主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id主键
     *
     * @param id id主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取展示顺序
     *
     * @return bannerOrder - 展示顺序
     */
    public Integer getBannerorder() {
        return bannerorder;
    }

    /**
     * 设置展示顺序
     *
     * @param bannerorder 展示顺序
     */
    public void setBannerorder(Integer bannerorder) {
        this.bannerorder = bannerorder;
    }

    /**
     * 获取banner类型
     *
     * @return bannerType - banner类型
     */
    public Integer getBannertype() {
        return bannertype;
    }

    /**
     * 设置banner类型
     *
     * @param bannertype banner类型
     */
    public void setBannertype(Integer bannertype) {
        this.bannertype = bannertype;
    }

    /**
     * 获取banner图片链接
     *
     * @return bannerImg - banner图片链接
     */
    public String getBannerimg() {
        return bannerimg;
    }

    /**
     * 设置banner图片链接
     *
     * @param bannerimg banner图片链接
     */
    public void setBannerimg(String bannerimg) {
        this.bannerimg = bannerimg;
    }

    /**
     * 获取banner对象，可以为对象id，也可以为链接
     *
     * @return bannerObject - banner对象，可以为对象id，也可以为链接
     */
    public String getBannerobject() {
        return bannerobject;
    }

    /**
     * 设置banner对象，可以为对象id，也可以为链接
     *
     * @param bannerobject banner对象，可以为对象id，也可以为链接
     */
    public void setBannerobject(String bannerobject) {
        this.bannerobject = bannerobject;
    }

    /**
     * 获取创建时间
     *
     * @return createDate - 创建时间
     */
    public Date getCreatedate() {
        return createdate;
    }

    /**
     * 设置创建时间
     *
     * @param createdate 创建时间
     */
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
}