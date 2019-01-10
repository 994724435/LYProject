package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "m_rob")
public class MRob {
    /**
     * id主键
     */
    @Id
    private Integer id;

    /**
     * 总抢单金额
     */
    private Integer price;

    /**
     * 已抢单金额
     */
    @Column(name = "matePrice")
    private Integer mateprice;

    /**
     * 未抢单金额
     */
    @Column(name = "remainPrice")
    private Integer remainprice;

    /**
     * 创建人id
     */
    @Column(name = "systemUserId")
    private Integer systemuserid;

    /**
     * 抢单状态：0-抢单中  1-抢单完成
     */
    private Integer status;

    /**
     * 批次号
     */
    @Column(name = "batchNum")
    private String batchnum;

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
     * 获取总抢单金额
     *
     * @return price - 总抢单金额
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * 设置总抢单金额
     *
     * @param price 总抢单金额
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * 获取已抢单金额
     *
     * @return matePrice - 已抢单金额
     */
    public Integer getMateprice() {
        return mateprice;
    }

    /**
     * 设置已抢单金额
     *
     * @param mateprice 已抢单金额
     */
    public void setMateprice(Integer mateprice) {
        this.mateprice = mateprice;
    }

    /**
     * 获取未抢单金额
     *
     * @return remainPrice - 未抢单金额
     */
    public Integer getRemainprice() {
        return remainprice;
    }

    /**
     * 设置未抢单金额
     *
     * @param remainprice 未抢单金额
     */
    public void setRemainprice(Integer remainprice) {
        this.remainprice = remainprice;
    }

    /**
     * 获取创建人id
     *
     * @return systemUserId - 创建人id
     */
    public Integer getSystemuserid() {
        return systemuserid;
    }

    /**
     * 设置创建人id
     *
     * @param systemuserid 创建人id
     */
    public void setSystemuserid(Integer systemuserid) {
        this.systemuserid = systemuserid;
    }

    /**
     * 获取抢单状态：0-抢单中  1-抢单完成
     *
     * @return status - 抢单状态：0-抢单中  1-抢单完成
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置抢单状态：0-抢单中  1-抢单完成
     *
     * @param status 抢单状态：0-抢单中  1-抢单完成
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取批次号
     *
     * @return batchNum - 批次号
     */
    public String getBatchnum() {
        return batchnum;
    }

    /**
     * 设置批次号
     *
     * @param batchnum 批次号
     */
    public void setBatchnum(String batchnum) {
        this.batchnum = batchnum;
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