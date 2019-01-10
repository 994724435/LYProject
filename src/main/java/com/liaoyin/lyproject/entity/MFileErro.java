package com.liaoyin.lyproject.entity;

import javax.persistence.*;

@Table(name = "m_file_erro")
public class MFileErro {
    /**
     * id主键
     */
    @Id
    private Integer id;

    /**
     * 文件名称
     */
    @Column(name = "fileName")
    private String filename;

    /**
     * 请求ip
     */
    @Column(name = "requestIp")
    private String requestip;

    /**
     * 当前登录用户id
     */
    @Column(name = "nowLoginId")
    private Integer nowloginid;

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
     * 获取文件名称
     *
     * @return fileName - 文件名称
     */
    public String getFilename() {
        return filename;
    }

    /**
     * 设置文件名称
     *
     * @param filename 文件名称
     */
    public void setFilename(String filename) {
        this.filename = filename;
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
     * 获取当前登录用户id
     *
     * @return nowLoginId - 当前登录用户id
     */
    public Integer getNowloginid() {
        return nowloginid;
    }

    /**
     * 设置当前登录用户id
     *
     * @param nowloginid 当前登录用户id
     */
    public void setNowloginid(Integer nowloginid) {
        this.nowloginid = nowloginid;
    }
}