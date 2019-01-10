package com.liaoyin.lyproject.service;

import com.liaoyin.lyproject.base.service.BaseService;
import com.liaoyin.lyproject.dao.SAccountRecordMapper;
import com.liaoyin.lyproject.entity.SAccountRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 作者：
 * 时间：2018/9/28 20:49
 * 描述：
 */
@Service
@Slf4j
public class SAccountRecordService extends BaseService<SAccountRecordMapper,SAccountRecord> {

    /**
     * 作者：
     * 时间： 2018/9/28 20:56
     * 描述：
     * recordbody详情
     * recordprice操作金额
     * recordnowprice增加或减少后的金额
     * recordstatus增加或减少标识：0-减少  1-增加
     * recordtype当前记录对应账户类型：0-金额  1-积分
     * recordmold账单类型：0-排单 1-抢单 2-收款  3-提现  4-赠送(接收)积分
     * recordtoobject当前记录对应对象
     * recordtouserid当前记录对应的用户id
     * recordtoaccountid关联账户
     **/
    public synchronized void saveAccountRecord(String recordbody,Integer recordprice,Integer recordnowprice,
                                  Integer recordstatus,Integer recordtype,Integer recordmold,
                                  String recordtoobject,Integer recordtouserid,Integer recordtoaccountid
                                  ){
        int i = 1;
        log.info("账户记录执行了"+i+"次");
        i++;
        SAccountRecord record = new SAccountRecord();
        record.setRecordbody(recordbody);
        record.setRecordprice(recordprice);
        record.setRecordnowprice(recordnowprice);
        record.setRecordstatus(recordstatus);
        record.setRecordtype(recordtype);
        record.setRecordmold(recordmold);
        record.setRecordtoobject(recordtoobject);
        record.setRecordtouserid(recordtouserid);
        record.setRecordtoaccountid(recordtoaccountid);
        record.setCreatedate(new Date());
        this.mapper.insertSelective(record);
    }

}
