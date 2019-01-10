package com.liaoyin.lyproject.entity;

import javax.persistence.*;

@Table(name = "s_account")
public class SAccount {
    /**
     * id主键
     */
    @Id
    private Integer id;

    /**
     * 总余额
     */
    @Column(name = "totalPrice")
    private Integer totalprice;

    /**
     * 可提现余额
     */
    @Column(name = "canPrice")
    private Integer canprice;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 支付宝账号
     */
    @Column(name = "alipayAccount")
    private String alipayaccount;

    /**
     * 微信账号
     */
    @Column(name = "wxpayAccount")
    private String wxpayaccount;

    /**
     * 银行卡卡号
     */
    @Column(name = "bankAccount")
    private String bankaccount;

    /**
     * 银行卡名称
     **/
    @Column(name = "bankName")
    private String bankName;

    /**
     * 用户id
     */
    @Column(name = "userId")
    private Integer userid;

    /**
     * 推荐人数量
     **/
    @Column(name = "refereeNum")
    private Integer refereeNum;

    /**
     * 0-正常 1-冻结
     **/
    private Integer status;

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
     * 获取总余额
     *
     * @return totalPrice - 总余额
     */
    public Integer getTotalprice() {
        return totalprice;
    }

    /**
     * 设置总余额
     *
     * @param totalprice 总余额
     */
    public void setTotalprice(Integer totalprice) {
        this.totalprice = totalprice;
    }

    /**
     * 获取可提现余额
     *
     * @return canPrice - 可提现余额
     */
    public Integer getCanprice() {
        return canprice;
    }

    /**
     * 设置可提现余额
     *
     * @param canprice 可提现余额
     */
    public void setCanprice(Integer canprice) {
        this.canprice = canprice;
    }

    /**
     * 获取积分
     *
     * @return integral - 积分
     */
    public Integer getIntegral() {
        return integral;
    }

    /**
     * 设置积分
     *
     * @param integral 积分
     */
    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    /**
     * 获取支付宝账号
     *
     * @return alipayAccount - 支付宝账号
     */
    public String getAlipayaccount() {
        return alipayaccount;
    }

    /**
     * 设置支付宝账号
     *
     * @param alipayaccount 支付宝账号
     */
    public void setAlipayaccount(String alipayaccount) {
        this.alipayaccount = alipayaccount;
    }

    /**
     * 获取微信账号
     *
     * @return wxpayAccount - 微信账号
     */
    public String getWxpayaccount() {
        return wxpayaccount;
    }

    /**
     * 设置微信账号
     *
     * @param wxpayaccount 微信账号
     */
    public void setWxpayaccount(String wxpayaccount) {
        this.wxpayaccount = wxpayaccount;
    }

    /**
     * 获取银行卡卡号
     *
     * @return bankAccount - 银行卡卡号
     */
    public String getBankaccount() {
        return bankaccount;
    }

    /**
     * 设置银行卡卡号
     *
     * @param bankaccount 银行卡卡号
     */
    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
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

    public Integer getRefereeNum() {
        return refereeNum;
    }

    public void setRefereeNum(Integer refereeNum) {
        this.refereeNum = refereeNum;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}