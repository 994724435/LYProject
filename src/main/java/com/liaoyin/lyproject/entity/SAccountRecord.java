package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "s_account_record")
public class SAccountRecord {
    /**
     * id主键
     */
    @Id
    private Integer id;

    /**
     * 记录详情
     */
    @Column(name = "recordBody")
    private String recordbody;

    /**
     * 操作金额
     */
    @Column(name = "recordPrice")
    private Integer recordprice;

    /**
     * 增加或减少后的金额
     */
    @Column(name = "recordNowPrice")
    private Integer recordnowprice;

    /**
     * 增加或减少标识：0-减少  1-增加
     */
    @Column(name = "recordStatus")
    private Integer recordstatus;

    /**
     * 当前记录对应账户类型：0-金额  1-积分
     */
    @Column(name = "recordType")
    private Integer recordtype;

    /**
     * 账单类型：0-排单 1-抢单 2-收款  3-提现  4-赠送(接收)积分   5-激活会员用户
     */
    @Column(name = "recordMold")
    private Integer recordmold;

    /**
     * 当前记录对应对象
     */
    @Column(name = "recordToObject")
    private String recordtoobject;

    /**
     * 当前记录对应的用户id
     */
    @Column(name = "recordToUserId")
    private Integer recordtouserid;

    /**
     * 关联账户
     */
    @Column(name = "recordToAccountId")
    private Integer recordtoaccountid;

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
     * 获取记录详情
     *
     * @return recordBody - 记录详情
     */
    public String getRecordbody() {
        return recordbody;
    }

    /**
     * 设置记录详情
     *
     * @param recordbody 记录详情
     */
    public void setRecordbody(String recordbody) {
        this.recordbody = recordbody;
    }

    /**
     * 获取操作金额
     *
     * @return recordPrice - 操作金额
     */
    public Integer getRecordprice() {
        return recordprice;
    }

    /**
     * 设置操作金额
     *
     * @param recordprice 操作金额
     */
    public void setRecordprice(Integer recordprice) {
        this.recordprice = recordprice;
    }

    /**
     * 获取增加或减少后的金额
     *
     * @return recordNowPrice - 增加或减少后的金额
     */
    public Integer getRecordnowprice() {
        return recordnowprice;
    }

    /**
     * 设置增加或减少后的金额
     *
     * @param recordnowprice 增加或减少后的金额
     */
    public void setRecordnowprice(Integer recordnowprice) {
        this.recordnowprice = recordnowprice;
    }

    /**
     * 获取增加或减少标识：0-减少  1-增加
     *
     * @return recordStatus - 增加或减少标识：0-减少  1-增加
     */
    public Integer getRecordstatus() {
        return recordstatus;
    }

    /**
     * 设置增加或减少标识：0-减少  1-增加
     *
     * @param recordstatus 增加或减少标识：0-减少  1-增加
     */
    public void setRecordstatus(Integer recordstatus) {
        this.recordstatus = recordstatus;
    }

    /**
     * 获取当前记录对应账户类型：0-金额  1-积分
     *
     * @return recordType - 当前记录对应账户类型：0-金额  1-积分
     */
    public Integer getRecordtype() {
        return recordtype;
    }

    /**
     * 设置当前记录对应账户类型：0-金额  1-积分
     *
     * @param recordtype 当前记录对应账户类型：0-金额  1-积分
     */
    public void setRecordtype(Integer recordtype) {
        this.recordtype = recordtype;
    }

    /**
     * 获取账单类型：0-排单 1-抢单 2-收款
     *
     * @return recordMold - 账单类型：0-排单 1-抢单 2-收款
     */
    public Integer getRecordmold() {
        return recordmold;
    }

    /**
     * 设置账单类型：0-排单 1-抢单 2-收款
     *
     * @param recordmold 账单类型：0-排单 1-抢单 2-收款
     */
    public void setRecordmold(Integer recordmold) {
        this.recordmold = recordmold;
    }

    /**
     * 获取当前记录对应对象
     *
     * @return recordToObject - 当前记录对应对象
     */
    public String getRecordtoobject() {
        return recordtoobject;
    }

    /**
     * 设置当前记录对应对象
     *
     * @param recordtoobject 当前记录对应对象
     */
    public void setRecordtoobject(String recordtoobject) {
        this.recordtoobject = recordtoobject;
    }

    /**
     * 获取当前记录对应的用户id
     *
     * @return recordToUserId - 当前记录对应的用户id
     */
    public Integer getRecordtouserid() {
        return recordtouserid;
    }

    /**
     * 设置当前记录对应的用户id
     *
     * @param recordtouserid 当前记录对应的用户id
     */
    public void setRecordtouserid(Integer recordtouserid) {
        this.recordtouserid = recordtouserid;
    }

    /**
     * 获取关联账户
     *
     * @return recordToAccountId - 关联账户
     */
    public Integer getRecordtoaccountid() {
        return recordtoaccountid;
    }

    /**
     * 设置关联账户
     *
     * @param recordtoaccountid 关联账户
     */
    public void setRecordtoaccountid(Integer recordtoaccountid) {
        this.recordtoaccountid = recordtoaccountid;
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