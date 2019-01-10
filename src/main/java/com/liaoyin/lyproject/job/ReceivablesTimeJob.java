package com.liaoyin.lyproject.job;

import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.entity.MMate;
import com.liaoyin.lyproject.entity.MOverTimeJob;
import com.liaoyin.lyproject.entity.SUser;
import com.liaoyin.lyproject.job.bean.MakePriceTimeBean;
import com.liaoyin.lyproject.job.bean.ReceivablesTimeBean;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 作者：
 * 时间：2018/10/22 10:23
 * 描述：确认收款超时工作调度
 */
@Slf4j
public class ReceivablesTimeJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("------------确认收款超时任务调度开始执行------------");
        ReceivablesTimeBean bean = (ReceivablesTimeBean) jobExecutionContext.getJobDetail().getJobDataMap().get("receivablesTimeBean");
        MOverTimeJob overTimeJob = bean.getOverTimeJobMapper().selectOverTimeJobToMateIdAndStatusAll(bean.getMateId(),1).get(0);
        overTimeJob.setStatus(1);
        bean.getOverTimeJobMapper().updateByPrimaryKeySelective(overTimeJob);
        MMate mate = bean.getMateMapper().selectByPrimaryKey(bean.getMateId());
        if (!Common.isNull(mate)){
            if (Common.isEqual(mate.getStatus(),1)){
                //封号操作
                SUser user = bean.getUserMapper().selectByPrimaryKey(bean.getUserId());
                user.setStatus(3);
                bean.getUserMapper().updateByPrimaryKeySelective(user);
                //匹配记录状态操作
                mate.setMold(2);
                bean.getMateMapper().updateByPrimaryKeySelective(mate);
            }
        }
        log.info("------------确认收款超时任务调度开结束执行------------");
    }
}
