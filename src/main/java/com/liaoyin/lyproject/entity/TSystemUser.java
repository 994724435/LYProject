package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "t_systemuser")
public class TSystemUser {
    /**
     * id主键
     */
    @Id
    private Integer id;

    /**
     * 账号
     */
    @Column(name = "userAccount")
    private String useraccount;

    /**
     * 密码
     */
    @Column(name = "userPassword")
    private String userpassword;

    /**
     * 手机号
     */
    @Column(name = "userPhone")
    private String userphone;

    /**
     * 是否锁定：0-正常  1-锁定
     */
    @Column(name = "isLock")
    private Integer islock;

    /**
     * 真实姓名
     */
    @Column(name = "realName")
    private String realname;

    /**
     * 令牌
     */
    private String token;

    /**
     * 最后一次登录时间
     */
    @Column(name = "lastLoginDate")
    private Date lastlogindate;

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
     * 获取账号
     *
     * @return userAccount - 账号
     */
    public String getUseraccount() {
        return useraccount;
    }

    /**
     * 设置账号
     *
     * @param useraccount 账号
     */
    public void setUseraccount(String useraccount) {
        this.useraccount = useraccount;
    }

    /**
     * 获取密码
     *
     * @return userPassword - 密码
     */
    public String getUserpassword() {
        return userpassword;
    }

    /**
     * 设置密码
     *
     * @param userpassword 密码
     */
    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    /**
     * 获取手机号
     *
     * @return userPhone - 手机号
     */
    public String getUserphone() {
        return userphone;
    }

    /**
     * 设置手机号
     *
     * @param userphone 手机号
     */
    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    /**
     * 获取是否锁定：0-正常  1-锁定
     *
     * @return isLock - 是否锁定：0-正常  1-锁定
     */
    public Integer getIslock() {
        return islock;
    }

    /**
     * 设置是否锁定：0-正常  1-锁定
     *
     * @param islock 是否锁定：0-正常  1-锁定
     */
    public void setIslock(Integer islock) {
        this.islock = islock;
    }

    /**
     * 获取真实姓名
     *
     * @return realName - 真实姓名
     */
    public String getRealname() {
        return realname;
    }

    /**
     * 设置真实姓名
     *
     * @param realname 真实姓名
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }

    /**
     * 获取令牌
     *
     * @return token - 令牌
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置令牌
     *
     * @param token 令牌
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取最后一次登录时间
     *
     * @return lastLoginDate - 最后一次登录时间
     */
    public Date getLastlogindate() {
        return lastlogindate;
    }

    /**
     * 设置最后一次登录时间
     *
     * @param lastlogindate 最后一次登录时间
     */
    public void setLastlogindate(Date lastlogindate) {
        this.lastlogindate = lastlogindate;
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