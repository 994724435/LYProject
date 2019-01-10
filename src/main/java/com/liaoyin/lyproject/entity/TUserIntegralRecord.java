package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "t_user_integral_record")
public class TUserIntegralRecord {
    @Id
    private Integer id;

    /**
     * 积分值
     */
    private Integer intergral;

    /**
     * 用户id
     */
    @Column(name = "userId")
    private Integer userid;

    /**
     * 充值人id
     */
    @Column(name = "systemUserId")
    private Integer systemuserid;
    
    /**
     * 0-增加  1-减少
     **/
    @Column(name = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "createDate")
    private Date createdate;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取积分值
     *
     * @return intergral - 积分值
     */
    public Integer getIntergral() {
        return intergral;
    }

    /**
     * 设置积分值
     *
     * @param intergral 积分值
     */
    public void setIntergral(Integer intergral) {
        this.intergral = intergral;
    }

    /**
     * 获取用户id
     *
     * @return userId - 用户id
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置用户id
     *
     * @param userid 用户id
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取充值人id
     *
     * @return systemUserId - 充值人id
     */
    public Integer getSystemuserid() {
        return systemuserid;
    }

    /**
     * 设置充值人id
     *
     * @param systemuserid 充值人id
     */
    public void setSystemuserid(Integer systemuserid) {
        this.systemuserid = systemuserid;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}