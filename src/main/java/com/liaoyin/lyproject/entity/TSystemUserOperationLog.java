package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "t_systemuser_operation_log")
public class TSystemUserOperationLog {
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
     * 登录密码或请求参数
     */
    @Column(name = "userPassword")
    private String userpassword;

    /**
     * 真实姓名
     */
    @Column(name = "realName")
    private String realname;

    /**
     * 日志类型：0-登录日志  1-操作日志
     */
    private Integer status;

    /**
     * 请求ip
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
     * 操作内容
     */
    private String message;

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
     * 获取日志类型：0-登录日志  1-操作日志
     *
     * @return status - 日志类型：0-登录日志  1-操作日志
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置日志类型：0-登录日志  1-操作日志
     *
     * @param status 日志类型：0-登录日志  1-操作日志
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取请求ip
     *
     * @return requestIp - 请求ip
     */
    public String getRequestip() {
        return requestip;
    }

    /**
     * 设置请求ip
     *
     * @param requestip 请求ip
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

    /**
     * 获取操作内容
     *
     * @return message - 操作内容
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置操作内容
     *
     * @param message 操作内容
     */
    public void setMessage(String message) {
        this.message = message;
    }
}