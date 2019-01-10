package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "s_user_login_log")
public class SUserLoginLog {
    /**
     * id主键
     */
    @Id
    private Integer id;

    /**
     * 登录账号
     */
    @Column(name = "userAccount")
    private String useraccount;

    /**
     * 登录密码
     */
    @Column(name = "userPassword")
    private String userpassword;

    /**
     * 真实姓名
     **/
    @Column(name = "realName")
    private String realName;

    /**
     * 请求来源ip
     */
    @Column(name = "requestIp")
    private String requestip;

    /**
     * 线程
     */
    @Column(name = "requestThread")
    private String requestthread;

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
     * 获取登录账号
     *
     * @return userAccount - 登录账号
     */
    public String getUseraccount() {
        return useraccount;
    }

    /**
     * 设置登录账号
     *
     * @param useraccount 登录账号
     */
    public void setUseraccount(String useraccount) {
        this.useraccount = useraccount;
    }

    /**
     * 获取登录密码
     *
     * @return userPassword - 登录密码
     */
    public String getUserpassword() {
        return userpassword;
    }

    /**
     * 设置登录密码
     *
     * @param userpassword 登录密码
     */
    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    /**
     * 获取请求来源ip
     *
     * @return requestIp - 请求来源ip
     */
    public String getRequestip() {
        return requestip;
    }

    /**
     * 设置请求来源ip
     *
     * @param requestip 请求来源ip
     */
    public void setRequestip(String requestip) {
        this.requestip = requestip;
    }

    /**
     * 获取线程
     *
     * @return requestThread - 线程
     */
    public String getRequestthread() {
        return requestthread;
    }

    /**
     * 设置线程
     *
     * @param requestthread 线程
     */
    public void setRequestthread(String requestthread) {
        this.requestthread = requestthread;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}