package com.liaoyin.lyproject.common.bean;

/**
 * 作者：
 * 时间：2018/9/28 22:30
 * 描述：匹配_排单实例
 */
public class MMateToPlatoon {
    private Integer id;//排单id
    private Integer price;//剩余排单金额
    private Integer userId;//排单人id

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

    public MMateToPlatoon() {
    }

    public MMateToPlatoon(Integer id, Integer price, Integer userId) {
        this.id = id;
        this.price = price;
        this.userId = userId;
    }
}
