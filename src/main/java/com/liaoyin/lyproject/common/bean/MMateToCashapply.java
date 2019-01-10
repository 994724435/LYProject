package com.liaoyin.lyproject.common.bean;

/**
 * 作者：
 * 时间：2018/9/28 22:31
 * 描述：匹配_提现实例
 */
public class MMateToCashapply {
    private Integer id;//提现id
    private Integer price;//剩余提现金额
    private Integer userId;//提现人id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public MMateToCashapply() {
    }

    public MMateToCashapply(Integer id, Integer price, Integer userId) {
        this.id = id;
        this.price = price;
        this.userId = userId;
    }
}
