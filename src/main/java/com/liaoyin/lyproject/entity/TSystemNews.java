package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "t_system_news")
public class TSystemNews {
    /**
     * id主键
     */
    @Id
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 消息分类：0-投票通知
     */
    private Integer type;

    /**
     * 持有人id
     */
    @Column(name = "systemUserId")
    private Integer systemuserid;

    /**
     * 创建时间
     */
    @Column(name = "createDate")
    private Date createdate;

    /**
     * 内容
     */
    private String body;

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
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取消息分类：0-投票通知
     *
     * @return type - 消息分类：0-投票通知
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置消息分类：0-投票通知
     *
     * @param type 消息分类：0-投票通知
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取持有人id
     *
     * @return systemUserId - 持有人id
     */
    public Integer getSystemuserid() {
        return systemuserid;
    }

    /**
     * 设置持有人id
     *
     * @param systemuserid 持有人id
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