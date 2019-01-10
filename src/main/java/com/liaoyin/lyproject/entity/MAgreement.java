package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "m_agreement")
public class MAgreement {
    @Id
    private Integer id;

    /**
     * 协议类型：0-注册协议
     */
    private Integer type;

    /**
     * 最后一次修改时间
     */
    @Column(name = "lastUpdateDate")
    private Date lastupdatedate;

    /**
     * 注册时间
     */
    @Column(name = "createDate")
    private Date createdate;

    /**
     * 内容
     */
    private String body;

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
     * 获取协议类型：0-注册协议
     *
     * @return type - 协议类型：0-注册协议
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置协议类型：0-注册协议
     *
     * @param type 协议类型：0-注册协议
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取最后一次修改时间
     *
     * @return lastUpdateDate - 最后一次修改时间
     */
    public Date getLastupdatedate() {
        return lastupdatedate;
    }

    /**
     * 设置最后一次修改时间
     *
     * @param lastupdatedate 最后一次修改时间
     */
    public void setLastupdatedate(Date lastupdatedate) {
        this.lastupdatedate = lastupdatedate;
    }

    /**
     * 获取注册时间
     *
     * @return createDate - 注册时间
     */
    public Date getCreatedate() {
        return createdate;
    }

    /**
     * 设置注册时间
     *
     * @param createdate 注册时间
     */
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    /**
     * 获取内容
     *
     * @return body - 内容
     */
    public String getBody() {
        return body;
    }

    /**
     * 设置内容
     *
     * @param body 内容
     */
    public void setBody(String body) {
        this.body = body;
    }
}