package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "t_systemuser_lock_vote")
public class TSystemUserLockVote {
    /**
     * id主键
     */
    @Id
    private Integer id;

    /**
     * 发起人id
     */
    @Column(name = "createUserId")
    private Integer createuserid;

    /**
     * 锁定对象
     */
    @Column(name = "lockUserId")
    private Integer lockuserid;

    /**
     * 参与人员id集合，多个用逗号隔开
     */
    @Column(name = "partakeUserIds")
    private String partakeuserids;

    /**
     * 已处理人员id集合，多个用逗号隔开
     */
    @Column(name = "processedUserIds")
    private String processeduserids;

    /**
     * 同意人员id集合
     */
    @Column(name = "agreeUserIds")
    private String agreeuserids;

    /**
     * 同意数值
     */
    @Column(name = "agreeNum")
    private Integer agreenum;

    /**
     * 不同意人员id集合
     */
    @Column(name = "notAgreeUserIds")
    private String notagreeuserids;

    /**
     * 不同意数值
     */
    @Column(name = "notAgreeNum")
    private Integer notagreenum;

    /**
     * 投票状态：0-投票中   1-已关闭投票
     */
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "createDate")
    private Date createdate;

    /**
     * 投票内容
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
     * 获取发起人id
     *
     * @return createUserId - 发起人id
     */
    public Integer getCreateuserid() {
        return createuserid;
    }

    /**
     * 设置发起人id
     *
     * @param createuserid 发起人id
     */
    public void setCreateuserid(Integer createuserid) {
        this.createuserid = createuserid;
    }

    /**
     * 获取锁定对象
     *
     * @return lockUserId - 锁定对象
     */
    public Integer getLockuserid() {
        return lockuserid;
    }

    /**
     * 设置锁定对象
     *
     * @param lockuserid 锁定对象
     */
    public void setLockuserid(Integer lockuserid) {
        this.lockuserid = lockuserid;
    }

    /**
     * 获取参与人员id集合，多个用逗号隔开
     *
     * @return partakeUserIds - 参与人员id集合，多个用逗号隔开
     */
    public String getPartakeuserids() {
        return partakeuserids;
    }

    /**
     * 设置参与人员id集合，多个用逗号隔开
     *
     * @param partakeuserids 参与人员id集合，多个用逗号隔开
     */
    public void setPartakeuserids(String partakeuserids) {
        this.partakeuserids = partakeuserids;
    }

    /**
     * 获取已处理人员id集合，多个用逗号隔开
     *
     * @return processedUserIds - 已处理人员id集合，多个用逗号隔开
     */
    public String getProcesseduserids() {
        return processeduserids;
    }

    /**
     * 设置已处理人员id集合，多个用逗号隔开
     *
     * @param processeduserids 已处理人员id集合，多个用逗号隔开
     */
    public void setProcesseduserids(String processeduserids) {
        this.processeduserids = processeduserids;
    }

    /**
     * 获取同意人员id集合
     *
     * @return agreeUserIds - 同意人员id集合
     */
    public String getAgreeuserids() {
        return agreeuserids;
    }

    /**
     * 设置同意人员id集合
     *
     * @param agreeuserids 同意人员id集合
     */
    public void setAgreeuserids(String agreeuserids) {
        this.agreeuserids = agreeuserids;
    }

    /**
     * 获取同意数值
     *
     * @return agreeNum - 同意数值
     */
    public Integer getAgreenum() {
        return agreenum;
    }

    /**
     * 设置同意数值
     *
     * @param agreenum 同意数值
     */
    public void setAgreenum(Integer agreenum) {
        this.agreenum = agreenum;
    }

    /**
     * 获取不同意人员id集合
     *
     * @return notAgreeUserIds - 不同意人员id集合
     */
    public String getNotagreeuserids() {
        return notagreeuserids;
    }

    /**
     * 设置不同意人员id集合
     *
     * @param notagreeuserids 不同意人员id集合
     */
    public void setNotagreeuserids(String notagreeuserids) {
        this.notagreeuserids = notagreeuserids;
    }

    /**
     * 获取不同意数值
     *
     * @return notAgreeNum - 不同意数值
     */
    public Integer getNotagreenum() {
        return notagreenum;
    }

    /**
     * 设置不同意数值
     *
     * @param notagreenum 不同意数值
     */
    public void setNotagreenum(Integer notagreenum) {
        this.notagreenum = notagreenum;
    }

    /**
     * 获取投票状态：0-投票中   1-已关闭投票
     *
     * @return status - 投票状态：0-投票中   1-已关闭投票
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置投票状态：0-投票中   1-已关闭投票
     *
     * @param status 投票状态：0-投票中   1-已关闭投票
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * 获取投票内容
     *
     * @return body - 投票内容
     */
    public String getBody() {
        return body;
    }

    /**
     * 设置投票内容
     *
     * @param body 投票内容
     */
    public void setBody(String body) {
        this.body = body;
    }
}