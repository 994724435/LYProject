package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

/**
 * 作者：
 * 时间： 2018/9/28 20:01
 * 描述： 排单
 **/
@Table(name = "m_platoon_order")
public class MPlatoonOrder {
    /**
     * id主键
     */
    @Id
    private Integer id;

    /**
     * 排单金额
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
     * 排单人id
     */
    @Column(name = "userId")
    private Integer userid;

    /**
     * 0-匹配中  1-匹配完成  2-放弃
     */
    private Integer status;

    /**
     * 排单创建时间
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
     * 获取排单金额
     *
     * @return price - 排单金额
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * 设置排单金额
     *
     * @param price 排单金额
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
     * 获取未剩匹配金额
     *
     * @return remainPrice - 未剩匹配金额
     */
    public Integer getRemainprice() {
        return remainprice;
    }

    /**
     * 设置未剩匹配金额
     *
     * @param remainprice 未剩匹配金额
     */
    public void setRemainprice(Integer remainprice) {
        this.remainprice = remainprice;
    }

    /**
     * 获取排单人id
     *
     * @return userId - 排单人id
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置排单人id
     *
     * @param userid 排单人id
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
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
     * 获取排单创建时间
     *
     * @return createDate - 排单创建时间
     */
    public Date getCreatedate() {
        return createdate;
    }

    /**
     * 设置排单创建时间
     *
     * @param createdate 排单创建时间
     */
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
}