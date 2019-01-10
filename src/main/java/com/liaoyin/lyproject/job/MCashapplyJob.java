package com.liaoyin.lyproject.job;

import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.entity.MJob;
import com.liaoyin.lyproject.entity.MMate;
import com.liaoyin.lyproject.entity.SAccount;
import com.liaoyin.lyproject.entity.SDict;
import com.liaoyin.lyproject.job.bean.MCashapplyBean;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 作者：
 * 时间：2018/10/11 9:46
 * 描述：提现工作调度
 */
@Slf4j
public class MCashapplyJob implements Job {

    @Override
    public synchronized void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("------------提现到账任务调度开始执行------------");
        MCashapplyBean cb = (MCashapplyBean) jobExecutionContext.getJobDetail().getJobDataMap().get("cashBean");
        MJob job = cb.getJobMapper().selectJobToMateId(cb.getMateId());
        if (job.getStatus()==0){
            MMate mate = cb.getMateMapper().selectByPrimaryKey(cb.getMateId());
            SAccount account = cb.getAccountService().selectAccountForUpdate(mate.getPlantoonuserid());
            String dictCode = "";
            String accountRecordBody = "";
            Integer accountRecordMold = 0;
            switch (mate.getType()){
                case 0://排单
                    dictCode = Config.DICT_MATE_PLATOON_RATIO;
                    accountRecordBody = "排单到账";
                    break;
                case 1://抢单
                    dictCode = Config.DICT_MATE_ROB_RATIO;
                    accountRecordBody = "抢单到账";
                    accountRecordMold = 1;
                    break;
            }
            int i = 1;
            log.info("账户执行了"+i+"次");
            i++;
            SDict dict = cb.getDictMapper().selectDict(dictCode);
            Double rp = mate.getPrice()*(dict.getRealvalue()/100);
            Integer allPrice = mate.getPrice() + rp.intValue();
            account.setTotalprice(account.getTotalprice()+allPrice);
            account.setCanprice(account.getCanprice()+allPrice);
            cb.getAccountMapper().updateByPrimaryKeySelective(account);
            cb.getAccountRecordService().saveAccountRecord(accountRecordBody,allPrice,account.getTotalprice(),
                    1,0,accountRecordMold,mate.getId().toString(),mate.getPlantoonuserid(),account.getId());
            if (!Common.isNull(job)){
                job.setStatus(1);
                cb.getJobMapper().updateByPrimaryKeySelective(job);
            }
        }
        log.info("------------提现到账任务制度结束------------");

    }
}
