package com.liaoyin.lyproject.job.bean;

import com.liaoyin.lyproject.dao.MMateMapper;
import com.liaoyin.lyproject.dao.MOverTimeJobMapper;
import com.liaoyin.lyproject.dao.SUserMapper;

/**
 * 作者：
 * 时间：2018/10/22 10:24
 * 描述：打款超时传参对象
 */
public class ReceivablesTimeBean {
    private Integer userId;//用户id
    private Integer mateId;//匹配id
    private SUserMapper userMapper;
    private MMateMapper mateMapper;
    private MOverTimeJobMapper overTimeJobMapper;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMateId() {
        return mateId;
    }

    public void setMateId(Integer mateId) {
        this.mateId = mateId;
    }

    public SUserMapper getUserMapper() {
        return userMapper;
    }

    public void setUserMapper(SUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public MMateMapper getMateMapper() {
        return mateMapper;
    }

    public void setMateMapper(MMateMapper mateMapper) {
        this.mateMapper = mateMapper;
    }

    public MOverTimeJobMapper getOverTimeJobMapper() {
        return overTimeJobMapper;
    }

    public void setOverTimeJobMapper(MOverTimeJobMapper overTimeJobMapper) {
        this.overTimeJobMapper = overTimeJobMapper;
    }
}
