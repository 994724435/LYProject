package com.liaoyin.lyproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.base.service.BaseService;
import com.liaoyin.lyproject.dao.SAccountMapper;
import com.liaoyin.lyproject.dao.SAccountRecordMapper;
import com.liaoyin.lyproject.entity.SAccount;
import com.liaoyin.lyproject.entity.SAccountRecord;
import com.liaoyin.lyproject.util.RestUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 作者：
 * 时间：2018/9/25 17:20
 * 描述：
 */
@Service
public class SAccountService extends BaseService<SAccountMapper,SAccount> {

    @Resource
    private SAccountRecordMapper accountRecordMapper;


    public JsonRestResponse queryUserAccount(Integer userId) {

        return RestUtil.createResponse(this.mapper.selectAccountUserId(userId));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SAccount selectAccountForUpdate(Integer userId){
        return this.mapper.selectAccountForUpdate(userId);
    }

    public JsonRestResponse queryUserAccountRecord(Integer userId, Integer recordtype, Integer recordmold, Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        List<SAccountRecord> records = accountRecordMapper.selectAccountRecordToUserId(userId,recordtype,recordmold);
        PageInfo<SAccountRecord> p = new PageInfo<>(records);
        return RestUtil.createResponse(p);
    }
}
