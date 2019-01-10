package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

/**
 * 作者：
 * 时间： 2018/9/28 20:01
 * 描述： 匹配单
 **/
@Table(name = "m_mate")
public class MMate {

    /**
     * id主键
     */
    @Id
    private Integer id;

    /**
     * 匹配金额
     */
    private Integer price;

    /**
     * 0-正常 1-打款超时  2-确认收款超时
     **/
    private Integer mold;

    /**
     * 0-等待付款   1-已打款   2-已确认收款
     */
    private Integer status;

    /**
     * 排单id
     **/
    @Column(name = "planToonId")
    private Integer planToonId;

    /**
     * 排单人id
     */
    @Column(name = "planToonUserId")
    private Integer plantoonuserid;

    /**
     * 提现id
     **/
    @Column(name = "cashapplyId")
    private Integer cashapplyId;

    /**
     * 收款方id
     */
    @Column(name = "cashapplyUserId")
    private Integer cashapplyuserid;

    /**
     * 0-排单与提现匹配的   1-抢单与提现匹配的
     **/
    private Integer type;

    /**
     * 匹配时间
     */
    @Column(name = "createDate")
    private Date createdate;

    /**
     * 凭证，多个使用逗号隔开
     */
    @Column(name = "proofFile")
    private String prooffile;

    /**
     * 打款时间
     **/
    @Column(name = "makePriceDate")
    private Date makePriceDate;

    /**
     * 确认收款时间
     **/
    @Column(name = "confirmPriceDate")
    private Date confirmPriceDate;


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
     * 获取匹配金额
     *
     * @return price - 匹配金额
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * 设置匹配金额
     *
     * @param price 匹配金额
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * 获取0-等待付款   1-已打款   2-已确认收款
     *
     * @return status - 0-等待付款   1-已打款   2-已确认收款
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0-等待付款   1-已打款   2-已确认收款
     *
     * @param status 0-等待付款   1-已打款   2-已确认收款
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取排单人id
     *
     * @return planToonUserId - 排单人id
     */
    public Integer getPlantoonuserid() {
        return plantoonuserid;
    }

    /**
     * 设置排单人id
     *
     * @param plantoonuserid 排单人id
     */
    public void setPlantoonuserid(Integer plantoonuserid) {
        this.plantoonuserid = plantoonuserid;
    }

    /**
     * 获取收款方id
     *
     * @return cashapplyUserId - 收款方id
     */
    public Integer getCashapplyuserid() {
        return cashapplyuserid;
    }

    /**
     * 设置收款方id
     *
     * @param cashapplyuserid 收款方id
     */
    public void setCashapplyuserid(Integer cashapplyuserid) {
        this.cashapplyuserid = cashapplyuserid;
    }

    /**
     * 获取匹配时间
     *
     * @return createDate - 匹配时间
     */
    public Date getCreatedate() {
        return createdate;
    }

    /**
     * 设置匹配时间
     *
     * @param createdate 匹配时间
     */
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    /**
     * 获取凭证，多个使用逗号隔开
     *
     * @return proofFile - 凭证，多个使用逗号隔开
     */
    public String getProoffile() {
        return prooffile;
    }

    /**
     * 设置凭证，多个使用逗号隔开
     *
     * @param prooffile 凭证，多个使用逗号隔开
     */
    public void setProoffile(String prooffile) {
        this.prooffile = prooffile;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getMakePriceDate() {
        return makePriceDate;
    }

    public void setMakePriceDate(Date makePriceDate) {
        this.makePriceDate = makePriceDate;
    }

    public Date getConfirmPriceDate() {
        return confirmPriceDate;
    }

    public void setConfirmPriceDate(Date confirmPriceDate) {
        this.confirmPriceDate = confirmPriceDate;
    }

    public Integer getPlanToonId() {
        return planToonId;
    }

    public void setPlanToonId(Integer planToonId) {
        this.planToonId = planToonId;
    }

    public Integer getCashapplyId() {
        return cashapplyId;
    }

    public void setCashapplyId(Integer cashapplyId) {
        this.cashapplyId = cashapplyId;
    }

    public Integer getMold() {
        return mold;
    }

    public void setMold(Integer mold) {
        this.mold = mold;
    }
}