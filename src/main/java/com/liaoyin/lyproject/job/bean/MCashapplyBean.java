package com.liaoyin.lyproject.job.bean;

import com.liaoyin.lyproject.dao.MJobMapper;
import com.liaoyin.lyproject.dao.MMateMapper;
import com.liaoyin.lyproject.dao.SAccountMapper;
import com.liaoyin.lyproject.dao.SDictMapper;
import com.liaoyin.lyproject.service.SAccountRecordService;
import com.liaoyin.lyproject.service.SAccountService;

/**
 * 作者：
 * 时间：2018/10/11 9:50
 * 描述：任务调度传参对象
 */
public class MCashapplyBean {
    private Integer mateId;
    private MMateMapper mateMapper;
    private SDictMapper dictMapper;
    private SAccountService accountService;
    private SAccountMapper accountMapper;
    private SAccountRecordService accountRecordService;
    private MJobMapper jobMapper;
    public Integer getMateId() {
        return mateId;
    }

    public void setMateId(Integer mateId) {
        this.mateId = mateId;
    }

    public MMateMapper getMateMapper() {
        return mateMapper;
    }

    public void setMateMapper(MMateMapper mateMapper) {
        this.mateMapper = mateMapper;
    }

    public SDictMapper getDictMapper() {
        return dictMapper;
    }

    public void setDictMapper(SDictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    public SAccountRecordService getAccountRecordService() {
        return accountRecordService;
    }

    public void setAccountRecordService(SAccountRecordService accountRecordService) {
        this.accountRecordService = accountRecordService;
    }

    public MJobMapper getJobMapper() {
        return jobMapper;
    }

    public void setJobMapper(MJobMapper jobMapper) {
        this.jobMapper = jobMapper;
    }

    public SAccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(SAccountService accountService) {
        this.accountService = accountService;
    }

    public SAccountMapper getAccountMapper() {
        return accountMapper;
    }

    public void setAccountMapper(SAccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }
}
