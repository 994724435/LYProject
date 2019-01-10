package com.liaoyin.lyproject.job;

import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.entity.MMate;
import com.liaoyin.lyproject.entity.MOverTimeJob;
import com.liaoyin.lyproject.entity.SUser;
import com.liaoyin.lyproject.job.bean.MakePriceTimeBean;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 作者：
 * 时间：2018/10/22 9:30
 * 描述：打款超时工作调度
 */
@Slf4j
public class MakeRriceTimeJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("------------打款超时任务调度开始执行------------");
        MakePriceTimeBean bean = (MakePriceTimeBean) jobExecutionContext.getJobDetail().getJobDataMap().get("makePriceTimeBean");
        MOverTimeJob overTimeJob = bean.getOverTimeJobMapper().selectOverTimeJobToMateIdAndStatusAll(bean.getMateId(),0).get(0);
        overTimeJob.setStatus(1);
        bean.getOverTimeJobMapper().updateByPrimaryKeySelective(overTimeJob);
        MMate mate = bean.getMateMapper().selectByPrimaryKey(bean.getMateId());
        if (!Common.isNull(mate)){
            if (Common.isEqual(mate.getStatus(),0)){
                //冻结操作
                SUser user = bean.getUserMapper().selectByPrimaryKey(bean.getUserId());
                user.setStatus(2);
                bean.getUserMapper().updateByPrimaryKeySelective(user);
                //匹配记录状态操作
                mate.setMold(1);
                bean.getMateMapper().updateByPrimaryKeySelective(mate);
            }
        }
        log.info("------------打款超时任务调度开结束执行------------");
    }
}
