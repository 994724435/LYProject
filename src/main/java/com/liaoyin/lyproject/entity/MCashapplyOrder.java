package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

/**
 * 作者：
 * 时间： 2018/9/28 20:01
 * 描述： 提现
 **/
@Table(name = "m_cashapply_order")
public class MCashapplyOrder {
    /**
     * id主键
     */
    @Id
    private Integer id;

    /**
     * 提现金额
     */
    private Integer price;

    /**
     * 0-匹配中  1-匹配完成   2-放弃
     */
    private Integer status;

    /**
     * 0-正常匹配  1-已被封闭
     **/
    private Integer mold;

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
     * 提现人id
     */
    @Column(name = "userId")
    private Integer userid;

    /**
     * 提现时间
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
     * 获取提现金额
     *
     * @return price - 提现金额
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * 设置提现金额
     *
     * @param price 提现金额
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * 获取0-匹配中  1-匹配完成
     *
     * @return status - 0-匹配中  1-匹配完成
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0-匹配中  1-匹配完成
     *
     * @param status 0-匹配中  1-匹配完成
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * 获取提现人id
     *
     * @return userId - 提现人id
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置提现人id
     *
     * @param userid 提现人id
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取提现时间
     *
     * @return createDate - 提现时间
     */
    public Date getCreatedate() {
        return createdate;
    }

    /**
     * 设置提现时间
     *
     * @param createdate 提现时间
     */
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Integer getMold() {
        return mold;
    }

    public void setMold(Integer mold) {
        this.mold = mold;
    }
}