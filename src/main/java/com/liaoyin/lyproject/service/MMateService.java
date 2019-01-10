package com.liaoyin.lyproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.base.service.BaseService;
import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.CommonDate;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.common.bean.SessionBean;
import com.liaoyin.lyproject.dao.*;
import com.liaoyin.lyproject.entity.*;
import com.liaoyin.lyproject.exception.BusinessException;
import com.liaoyin.lyproject.job.MCashapplyJob;
import com.liaoyin.lyproject.job.MakeRriceTimeJob;
import com.liaoyin.lyproject.job.ReceivablesTimeJob;
import com.liaoyin.lyproject.job.bean.MCashapplyBean;
import com.liaoyin.lyproject.job.bean.MakePriceTimeBean;
import com.liaoyin.lyproject.job.bean.ReceivablesTimeBean;
import com.liaoyin.lyproject.util.RestUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 作者：
 * 时间：2018/10/4 16:23
 * 描述：
 */
@Slf4j
@Service
public class MMateService extends BaseService<MMateMapper,MMate> {

    @Resource
    private MMateMapper mateMapper;
    @Resource
    private SDictMapper dictMapper;
    @Resource
    private SAccountMapper accountMapper;
    @Autowired
    private SAccountRecordService accountRecordService;
    @Resource
    private MJobMapper jobMapper;
    @Resource
    private SUserMapper userMapper;
    @Resource
    private MOverTimeJobMapper overTimeJobMapper;
    @Resource
    private MRobOrderMapper robOrderMapper;
    @Autowired
    private SAccountService accountService;

    public JsonRestResponse selectMateRecord(Integer planToonUserId, Integer cashapplyUserId, Integer status,
                                             Integer startPage, Integer pageSize, HttpServletRequest request,
                                             Integer planToonId,Integer cashapplyId,Integer type) {
//        if (Common.isNull(planToonUserId) && Common.isNull(cashapplyUserId)){
//            throw new BusinessException("common.parm.erro");
//        }
        PageHelper.startPage(startPage, pageSize);
        List<MMate> datas = mateMapper.selectMateToUser(planToonUserId,cashapplyUserId,status,planToonId,cashapplyId,type);
        return RestUtil.createResponseData(new PageInfo(datas),
                mateMapper.selectMateToUserSumPrice(planToonUserId,cashapplyUserId,status,planToonId,cashapplyId,type));
    }

    public JsonRestResponse selectMateRecordDetail(Integer mateId, HttpServletRequest request) {
        Map<String,Object> data = mateMapper.selectMateRecordDetail(mateId);
        return RestUtil.createResponse(data);
    }

    public synchronized JsonRestResponse paymentUploadVoucher(Integer mateId,String prooffile, HttpServletRequest request)
    throws Exception{
        if (Common.isNull(prooffile)){
            throw new BusinessException("common.img.null");
        }
        SessionBean bean = Config.getSessionBean(request);
        MMate mate = this.mapper.selectByPrimaryKey(mateId);
        if (Common.isNull(mate)){
            throw new BusinessException("common.parm.erro");
        }
        if (mate.getStatus()!=0){
            throw new BusinessException("common.erro");
        }
        if (!Common.isEqual(bean.getUid(),mate.getPlantoonuserid())){
            throw new BusinessException("common.erro");
        }
        SDict dict = dictMapper.selectDict(Config.DICT_MATE_TIME);
        if (Common.isNull(dict)){
            throw new BusinessException("common.dict.not");
        }
        MJob j = jobMapper.selectJobToMateId(mateId);
        if (!Common.isNull(j)){
            throw new BusinessException("common.erro");
        }
        mate.setStatus(1);
        mate.setProoffile(prooffile);
        mate.setMakePriceDate(new Date());
        mate.setMold(0);
        this.mapper.updateByPrimaryKeySelective(mate);
        Date date = new Date();
        //任务调度_入库
        MJob jo = new MJob();
        jo.setMateid(mateId);
        jo.setOverdate(CommonDate.appointDateHour(date,dict.getRealvalue().intValue()));
        jo.setStatus(0);
        jobMapper.insertSelective(jo);
        //任务调度_开始
        MCashapplyBean cb = new MCashapplyBean();
        cb.setMateId(mateId);
        cb.setMateMapper(this.mapper);
        cb.setDictMapper(dictMapper);
        cb.setAccountService(accountService);
        cb.setAccountMapper(accountMapper);
        cb.setAccountRecordService(accountRecordService);
        cb.setJobMapper(jobMapper);
        SchedulerFactory sf_season = new StdSchedulerFactory();
        Scheduler scheduler_season = sf_season.getScheduler();
        String only = Config._mateTime+mateId;
        JobDetail job = JobBuilder.newJob(MCashapplyJob.class).withIdentity(only,only).build();
        job.getJobDataMap().put("cashBean",cb);
        CronTrigger trigger_season = TriggerBuilder.newTrigger()
                .withIdentity(only, only)
                //设置Cron表达式
                .withSchedule(CronScheduleBuilder.cronSchedule(Config.expressionHour(dict.getRealvalue().intValue(),date)))
                //.withSchedule(CronScheduleBuilder.cronSchedule("0 55 15 11 10 ? 2018"))
                .build();
        //把作业和触发器注册到任务调度中
        scheduler_season.scheduleJob(job, trigger_season);
        // 启动调度
        scheduler_season.start();
        //注入确认收款超时工作调度
        this.overtimeJob(1,mate.getCashapplyuserid(),mate.getId());
        //停止打款超时工作调度
        String stopOnly = Config._makePriceTime+mateId;
        JobDetail stopJob = JobBuilder.newJob(MakeRriceTimeJob.class).withIdentity(stopOnly,stopOnly).build();
        scheduler_season.pauseJob(stopJob.getKey());
        MOverTimeJob timeJob = overTimeJobMapper.selectOverTimeJobToMateIdAndStatus(mateId,0);
        timeJob.setStatus(2);
        overTimeJobMapper.updateByPrimaryKeySelective(timeJob);
        return RestUtil.createResponse(mate);
    }

    public synchronized JsonRestResponse confirmGathering(HttpServletRequest request, Integer mateId) {
        SessionBean bean = Config.getSessionBean(request);
        MMate mate = this.mapper.selectByPrimaryKey(mateId);
        if (Common.isNull(mate)){
            throw new BusinessException("common.parm.erro");
        }
        if (mate.getStatus()==0){
            throw new BusinessException("common.mate.confirm.price.not");
        }
        if (mate.getStatus()==2){
            throw new BusinessException("common.erro");
        }
        if (!Common.isEqual(mate.getCashapplyuserid(),bean.getUid())){
            throw new BusinessException("common.erro");
        }
        mate.setStatus(2);
        mate.setMold(0);
        mate.setConfirmPriceDate(new Date());
        this.mapper.updateByPrimaryKeySelective(mate);
        if (Common.isEqual(mate.getType(),1)){
            //判定抢单是否全部完成
            MRobOrder robOrder = robOrderMapper.selectByPrimaryKey(mate.getPlanToonId());
            BigDecimal allPrice = mateMapper.selectMateToUserSumPrice(null,null,2,mate.getPlanToonId(),
                    null,1);
            if (!Common.isNull(robOrder)&&!Common.isNull(allPrice)){
                if (allPrice.intValue()>=robOrder.getPrice()){
                    robOrder.setStatus(2);
                    robOrderMapper.updateByPrimaryKeySelective(robOrder);
                }
            }

        }
        try {
            //停止收款超时调度
            SchedulerFactory rb_season = new StdSchedulerFactory();
            Scheduler receivables_season = rb_season.getScheduler();
            String only2 = Config._receivablesTime+mateId;
            JobDetail rb_job = JobBuilder.newJob(ReceivablesTimeJob.class).withIdentity(only2,only2).build();
            receivables_season.pauseJob(rb_job.getKey());
            MOverTimeJob timeJob = overTimeJobMapper.selectOverTimeJobToMateIdAndStatus(mateId,1);
            timeJob.setStatus(2);
            overTimeJobMapper.updateByPrimaryKeySelective(timeJob);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("停止收款超时调度出现异常");
        }
        return RestUtil.createResponse(mate);
    }

    /**
     * 作者：
     * 时间： 2018/10/22 10:39
     * 描述： 超时工作调度
     **/
    public synchronized void overtimeJob(Integer status,Integer userId,Integer mateId){
        log.info("--------------注入超时工作域开始--------------");
        try {
            Date date = new Date();
            //任务调度入库
            MOverTimeJob timeJob = new MOverTimeJob();
            timeJob.setMateid(mateId);
            timeJob.setMold(status);
            timeJob.setStatus(0);
            switch (status){
                case 0://打款超时
                    MMate mate = mateMapper.selectByPrimaryKey(mateId);
                    MSmsCodeService.sendMsg(userMapper.selectByPrimaryKey(mate.getPlantoonuserid()).getUseraccount(),0);
                    MSmsCodeService.sendMsg(userMapper.selectByPrimaryKey(mate.getCashapplyuserid()).getUseraccount(),1);
                    SDict make_dict = dictMapper.selectDict(Config.DICT_MAKEPRICE_TIME);
                    timeJob.setOverdate(CommonDate.appointDateHour(date,make_dict.getRealvalue().intValue()));
                    MakePriceTimeBean bean = new MakePriceTimeBean();
                    bean.setMateId(mateId);
                    bean.setUserId(userId);
                    bean.setUserMapper(userMapper);
                    bean.setMateMapper(mateMapper);
                    bean.setOverTimeJobMapper(overTimeJobMapper);
                    SchedulerFactory sf_season = new StdSchedulerFactory();
                    Scheduler scheduler_season = sf_season.getScheduler();
                    String only = Config._makePriceTime+mateId;
                    JobDetail job = JobBuilder.newJob(MakeRriceTimeJob.class).withIdentity(only,only).build();
                    job.getJobDataMap().put("makePriceTimeBean",bean);
                    CronTrigger trigger_season = TriggerBuilder.newTrigger()
                            .withIdentity(only, only)
                            //设置Cron表达式
                            .withSchedule(CronScheduleBuilder.cronSchedule(Config.expressionHour(make_dict.getRealvalue().intValue(),date)))
                            //.withSchedule(CronScheduleBuilder.cronSchedule("0 55 15 11 10 ? 2018"))
                            .build();
                    //把作业和触发器注册到任务调度中
                    scheduler_season.scheduleJob(job, trigger_season);
                    // 启动调度
                    scheduler_season.start();
                    break;
                case 1://确认收款超时
                    SDict receivables_dict = dictMapper.selectDict(Config.DICT_RECEIVABLES_TIME);
                    timeJob.setOverdate(CommonDate.appointDateHour(date,receivables_dict.getRealvalue().intValue()));
                    ReceivablesTimeBean timeBean = new ReceivablesTimeBean();
                    timeBean.setMateId(mateId);
                    timeBean.setUserId(userId);
                    timeBean.setUserMapper(userMapper);
                    timeBean.setMateMapper(mateMapper);
                    timeBean.setOverTimeJobMapper(overTimeJobMapper);
                    SchedulerFactory rb_season = new StdSchedulerFactory();
                    Scheduler receivables_season = rb_season.getScheduler();
                    String only2 = Config._receivablesTime+mateId;
                    JobDetail rb_job = JobBuilder.newJob(ReceivablesTimeJob.class).withIdentity(only2,only2).build();
                    rb_job.getJobDataMap().put("receivablesTimeBean",timeBean);
                    CronTrigger rb_trigger_season = TriggerBuilder.newTrigger()
                            .withIdentity(only2, only2)
                            //设置Cron表达式
                            .withSchedule(CronScheduleBuilder.cronSchedule(Config.expressionHour(receivables_dict.getRealvalue().intValue(),date)))
                            //.withSchedule(CronScheduleBuilder.cronSchedule("0 55 15 11 10 ? 2018"))
                            .build();
                    //把作业和触发器注册到任务调度中
                    receivables_season.scheduleJob(rb_job, rb_trigger_season);
                    // 启动调度
                    receivables_season.start();
                    break;
            }
            overTimeJobMapper.insertSelective(timeJob);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("--------------注入超时工作域出现异常--------------");
        }
        log.info("--------------注入超时工作域结束--------------");
    }
}
