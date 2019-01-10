package com.liaoyin.lyproject.job.bean;

import com.liaoyin.lyproject.dao.*;

/**
 * 作者：
 * 时间：2018/10/22 9:39
 * 描述：打款超时传参对象
 */
public class MakePriceTimeBean {
    private Integer userId;//用户id
    private Integer mateId;//匹配id
    private MMateMapper mateMapper;
    private SUserMapper userMapper;
    private MOverTimeJobMapper overTimeJobMapper;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public MMateMapper getMateMapper() {
        return mateMapper;
    }

    public void setMateMapper(MMateMapper mateMapper) {
        this.mateMapper = mateMapper;
    }

    public SUserMapper getUserMapper() {
        return userMapper;
    }

    public void setUserMapper(SUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Integer getMateId() {
        return mateId;
    }

    public void setMateId(Integer mateId) {
        this.mateId = mateId;
    }

    public MOverTimeJobMapper getOverTimeJobMapper() {
        return overTimeJobMapper;
    }

    public void setOverTimeJobMapper(MOverTimeJobMapper overTimeJobMapper) {
        this.overTimeJobMapper = overTimeJobMapper;
    }
}
