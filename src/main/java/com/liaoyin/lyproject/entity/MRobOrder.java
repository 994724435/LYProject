package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "m_rob_order")
public class MRobOrder {
    /**
     * id主键
     */
    @Id
    private Integer id;

    /**
     * 抢单金额
     */
    private Integer price;

    /**
     * 已匹配金额
     */
    @Column(name = "matePrice")
    private Integer mateprice;

    /**
     * 未匹配金额
     */
    @Column(name = "remainPrice")
    private Integer remainprice;

    /**
     * 抢单人id
     */
    @Column(name = "userId")
    private Integer userid;

    /**
     * 所属批次号
     */
    @Column(name = "batchNum")
    private String batchnum;

    /**
     * 排单情况：0-未排单  1-已排单  2-已完成
     */
    private Integer status;

    /**
     * 抢单时间
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
     * 获取抢单金额
     *
     * @return price - 抢单金额
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * 设置抢单金额
     *
     * @param price 抢单金额
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * 获取已匹配金额
     *
     * @return matePrice - 已匹配金额
     */
    public Integer getMateprice() {
        return mateprice;
    }

    /**
     * 设置已匹配金额
     *
     * @param mateprice 已匹配金额
     */
    public void setMateprice(Integer mateprice) {
        this.mateprice = mateprice;
    }

    /**
     * 获取未匹配金额
     *
     * @return remainPrice - 未匹配金额
     */
    public Integer getRemainprice() {
        return remainprice;
    }

    /**
     * 设置未匹配金额
     *
     * @param remainprice 未匹配金额
     */
    public void setRemainprice(Integer remainprice) {
        this.remainprice = remainprice;
    }

    /**
     * 获取抢单人id
     *
     * @return userId - 抢单人id
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置抢单人id
     *
     * @param userid 抢单人id
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取所属批次号
     *
     * @return batchNum - 所属批次号
     */
    public String getBatchnum() {
        return batchnum;
    }

    /**
     * 设置所属批次号
     *
     * @param batchnum 所属批次号
     */
    public void setBatchnum(String batchnum) {
        this.batchnum = batchnum;
    }

    /**
     * 获取排单情况：0-已排单  1-未排单
     *
     * @return status - 排单情况：0-已排单  1-未排单
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置排单情况：0-已排单  1-未排单
     *
     * @param status 排单情况：0-已排单  1-未排单
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取抢单时间
     *
     * @return createDate - 抢单时间
     */
    public Date getCreatedate() {
        return createdate;
    }

    /**
     * 设置抢单时间
     *
     * @param createdate 抢单时间
     */
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
}