//package com.liaoyin.lyproject.service;
//
//import com.liaoyin.lyproject.common.Config;
//import com.liaoyin.lyproject.dao.*;
//import com.liaoyin.lyproject.entity.*;
//import com.liaoyin.lyproject.job.MCashapplyJob;
//import com.liaoyin.lyproject.job.MakeRriceTimeJob;
//import com.liaoyin.lyproject.job.ReceivablesTimeJob;
//import com.liaoyin.lyproject.job.bean.MCashapplyBean;
//import com.liaoyin.lyproject.job.bean.MakePriceTimeBean;
//import com.liaoyin.lyproject.job.bean.ReceivablesTimeBean;
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.*;
//import org.quartz.impl.StdSchedulerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
///**
// * 作者：
// * 时间：2018/9/28 22:26
// * 描述：匹配监听
// */
//@Component
//@Slf4j
//public class MMateListener implements ApplicationRunner {
//
//    @Resource
//    private MPlatoonOrderMapper platoonOrderMapper;
//    @Resource
//    private MCashapplyOrderMapper cashapplyOrderMapper;
//    @Resource
//    private MMateMapper mateMapper;
//    @Resource
//    private MRobMapper robMapper;
//    @Resource
//    private MJobMapper jobMapper;
//    @Resource
//    private SDictMapper dictMapper;
//    @Resource
//    private SAccountMapper accountMapper;
//    @Autowired
//    private SAccountRecordService accountRecordService;
//    @Resource
//    private MOverTimeJobMapper overTimeJobMapper;
//    @Resource
//    private SUserMapper userMapper;
//    @Autowired
//    private SAccountService accountService;
//
////    //排单池实例
////    private static final List<MMateToPlatoon> mp_list = new ArrayList<>();
////    //提现池实例
////    private static final List<MMateToCashapply> mc_list = new ArrayList<>();
//
//    /**
//     * 作者：
//     * 时间： 2018/10/4 16:00
//     * 描述： 服务启动时，将未匹配的排单、提现等信息注入进匹配池
//     **/
//    public void run(ApplicationArguments args){
////        //注入排单
////        List<MPlatoonOrder> platoonOrders = platoonOrderMapper.selectMplatoonOrderNot();
////        for (int i = 0; i < platoonOrders.size(); i++) {
////            MPlatoonOrder platoonOrder = platoonOrders.get(i);
////            setPlatoon(new MMateToPlatoon(platoonOrder.getId(),platoonOrder.getRemainprice(),platoonOrder.getUserid()));
////        }
////        //注入提现
////        List<MCashapplyOrder> cashapplyOrders = cashapplyOrderMapper.selectCashapplyOrderNot();
////        for (int i = 0; i < cashapplyOrders.size(); i++) {
////            MCashapplyOrder cashapplyOrder = cashapplyOrders.get(i);
////            setCashapply(new MMateToCashapply(cashapplyOrder.getId(),cashapplyOrder.getRemainprice(),cashapplyOrder.getUserid()));
////        }
////        //注入抢单
////        MRob nowRob = robMapper.selectRobStatus(0);
////        if (!Common.isNull(nowRob)){
////            RedisUtil.set(Config.robKey,JSON.toJSONString(nowRob));
////        }
//        //先将错误数据删除
//        List<Map<String,Object>> datas = jobMapper.selectErroData();
//        for (int i = 0; i < datas.size(); i++) {
//            List<MJob> d2 = jobMapper.selectJobListToMateId((Integer) datas.get(i).get("mateId"));
//            for (int j = 1; j < d2.size(); j++) {
//                jobMapper.deleteByPrimaryKey(d2.get(j).getId());
//            }
//        }
//
//        //注入提现到账工作域
//        List<MJob> jobs = jobMapper.selectJob();
//        for (int i = 0; i < jobs.size(); i++) {
//            long time = System.currentTimeMillis();
//            if (jobs.get(i).getOverdate().getTime()<time){
//                jobs.get(i).setOverdate(new Date(time+10000));
//            }
//            try {
//                log.info("--------------注入工作域开始--------------");
//                //任务调度_开始
//                MCashapplyBean cb = new MCashapplyBean();
//                cb.setMateId(jobs.get(i).getMateid());
//                cb.setMateMapper(mateMapper);
//                cb.setDictMapper(dictMapper);
//                cb.setAccountMapper(accountMapper);
//                cb.setAccountService(accountService);
//                cb.setAccountRecordService(accountRecordService);
//                cb.setJobMapper(jobMapper);
//                SchedulerFactory sf_season = new StdSchedulerFactory();
//                Scheduler scheduler_season = sf_season.getScheduler();
//                String only = Config._mateTime+jobs.get(i).getMateid();
//                JobDetail job = JobBuilder.newJob(MCashapplyJob.class).withIdentity(only,only).build();
//                job.getJobDataMap().put("cashBean",cb);
//                CronTrigger trigger_season = TriggerBuilder.newTrigger()
//                        .withIdentity(only, only)
//                        //设置Cron表达式
//                        .withSchedule(CronScheduleBuilder.cronSchedule(Config.expressionDate(jobs.get(i).getOverdate())))
//                        //.withSchedule(CronScheduleBuilder.cronSchedule("0 55 15 11 10 ? 2018"))
//                        .build();
//                //把作业和触发器注册到任务调度中
//                scheduler_season.scheduleJob(job, trigger_season);
//                // 启动调度
//                scheduler_season.start();
//                log.info("--------------注入工作域结束--------------");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        //注入超时工作域
//        List<MOverTimeJob> timeJobs = overTimeJobMapper.selectOverTimeJobAll();
//        for (int i = 0; i < timeJobs.size(); i++) {
//            MOverTimeJob job = timeJobs.get(i);
//            try {
//                MMate mate = mateMapper.selectByPrimaryKey(job.getMateid());
//                switch (job.getMold()){
//                    case 0://打款超时
//                        MakePriceTimeBean bean = new MakePriceTimeBean();
//                        bean.setMateId(job.getMateid());
//                        bean.setUserId(mate.getPlantoonuserid());
//                        bean.setUserMapper(userMapper);
//                        bean.setMateMapper(mateMapper);
//                        bean.setOverTimeJobMapper(overTimeJobMapper);
//                        SchedulerFactory sf_season = new StdSchedulerFactory();
//                        Scheduler scheduler_season = sf_season.getScheduler();
//                        String only = Config._makePriceTime+job.getMateid();
//                        JobDetail csjobs = JobBuilder.newJob(MakeRriceTimeJob.class).withIdentity(only,only).build();
//                        csjobs.getJobDataMap().put("makePriceTimeBean",bean);
//                        CronTrigger trigger_season = TriggerBuilder.newTrigger()
//                                .withIdentity(only, only)
//                                //设置Cron表达式
//                                .withSchedule(CronScheduleBuilder.cronSchedule(Config.expressionDate(job.getOverdate())))
//                                //.withSchedule(CronScheduleBuilder.cronSchedule("0 55 15 11 10 ? 2018"))
//                                .build();
//                        //把作业和触发器注册到任务调度中
//                        scheduler_season.scheduleJob(csjobs, trigger_season);
//                        // 启动调度
//                        scheduler_season.start();
//                        break;
//                    case 1://确认收款超时
//                        ReceivablesTimeBean timeBean = new ReceivablesTimeBean();
//                        timeBean.setMateId(job.getMateid());
//                        timeBean.setUserId(mate.getCashapplyuserid());
//                        timeBean.setUserMapper(userMapper);
//                        timeBean.setMateMapper(mateMapper);
//                        timeBean.setOverTimeJobMapper(overTimeJobMapper);
//                        SchedulerFactory rb_season = new StdSchedulerFactory();
//                        Scheduler receivables_season = rb_season.getScheduler();
//                        String only2 = Config._receivablesTime+job.getMateid();
//                        JobDetail rb_job = JobBuilder.newJob(ReceivablesTimeJob.class).withIdentity(only2,only2).build();
//                        rb_job.getJobDataMap().put("receivablesTimeBean",timeBean);
//                        CronTrigger rb_trigger_season = TriggerBuilder.newTrigger()
//                                .withIdentity(only2, only2)
//                                //设置Cron表达式
//                                .withSchedule(CronScheduleBuilder.cronSchedule(Config.expressionDate(job.getOverdate())))
//                                //.withSchedule(CronScheduleBuilder.cronSchedule("0 55 15 11 10 ? 2018"))
//                                .build();
//                        //把作业和触发器注册到任务调度中
//                        receivables_season.scheduleJob(rb_job, rb_trigger_season);
//                        // 启动调度
//                        receivables_season.start();
//                        break;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                log.error("注入超时工作出现异常");
//            }
//        }
//
//    }
//
////    @Override
////    public void run(ApplicationArguments args) throws Exception {
////        //开启一个新线程来监听匹配
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                log.info("匹配池已启动！");
////                while (true){//开启新线程匹配
////                    for (int i = 0; i < mp_list.size();) {
////                        for (int j = 0; j < mc_list.size();) {
////                            if (mp_list.get(i).getPrice()<mc_list.get(j).getPrice()){//提现池金额大
////                                Integer nowPrice = mc_list.get(j).getPrice()-mp_list.get(i).getPrice();
////                                //注入匹配记录
////                                MMate mate = new MMate();
////                                mate.setPrice(mp_list.get(i).getPrice());
////                                mate.setStatus(0);
////                                mate.setPlantoonuserid(mp_list.get(i).getUserId());
////                                mate.setCashapplyuserid(mc_list.get(j).getUserId());
////                                mate.setCreatedate(new Date());
////                                mateMapper.insertSelective(mate);
////                                //排单更新
////                                MPlatoonOrder mp = platoonOrderMapper.selectByPrimaryKey(mp_list.get(i).getId());
////                                mp.setMateprice(mp.getMateprice()+mp_list.get(i).getPrice());
////                                mp.setRemainprice(mp.getRemainprice()-mp_list.get(i).getPrice());
////                                mp.setStatus(1);
////                                platoonOrderMapper.updateByPrimaryKeySelective(mp);
////                                //提现更新
////                                MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(mc_list.get(j).getId());
////                                mc.setMateprice(mc.getMateprice()+mp_list.get(i).getPrice());
////                                mc.setRemainprice(mc.getRemainprice()-mp_list.get(i).getPrice());
////                                cashapplyOrderMapper.updateByPrimaryKeySelective(mc);
////                                //从匹配池中移除
////                                mp_list.remove(i);
////                                //重新添加到序列
////                                mc_list.get(j).setPrice(nowPrice);
////                                break;
////                            }else if (mp_list.get(i).getPrice()>mc_list.get(j).getPrice()){//排单池金额大
////                                Integer nowPrice = mp_list.get(i).getPrice()-mc_list.get(j).getPrice();
////                                //注入匹配记录
////                                MMate mate = new MMate();
////                                mate.setPrice(mc_list.get(j).getPrice());
////                                mate.setStatus(0);
////                                mate.setPlantoonuserid(mp_list.get(i).getUserId());
////                                mate.setCashapplyuserid(mc_list.get(j).getUserId());
////                                mate.setCreatedate(new Date());
////                                mateMapper.insertSelective(mate);
////                                //排单更新
////                                MPlatoonOrder mp = platoonOrderMapper.selectByPrimaryKey(mp_list.get(i).getId());
////                                mp.setMateprice(mp.getMateprice()+mc_list.get(j).getPrice());
////                                mp.setRemainprice(mp.getRemainprice()-mc_list.get(j).getPrice());
////                                platoonOrderMapper.updateByPrimaryKeySelective(mp);
////                                //提现更新
////                                MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(mc_list.get(j).getId());
////                                mc.setStatus(1);
////                                mc.setMateprice(mc.getMateprice()+mc_list.get(j).getPrice());
////                                mc.setRemainprice(mc.getRemainprice()-mc_list.get(j).getPrice());
////                                cashapplyOrderMapper.updateByPrimaryKeySelective(mc);
////                                //从匹配池中移除
////                                mc_list.remove(j);
////                                //重新添加到序列
////                                mp_list.get(i).setPrice(nowPrice);
////                            }else{//两者相等
////                                //注入匹配记录
////                                MMate mate = new MMate();
////                                mate.setPrice(mc_list.get(j).getPrice());
////                                mate.setStatus(0);
////                                mate.setPlantoonuserid(mp_list.get(i).getUserId());
////                                mate.setCashapplyuserid(mc_list.get(j).getUserId());
////                                mate.setCreatedate(new Date());
////                                mateMapper.insertSelective(mate);
////                                //排单更新
////                                MPlatoonOrder mp = platoonOrderMapper.selectByPrimaryKey(mp_list.get(i).getId());
////                                mp.setMateprice(mp.getMateprice()+mp_list.get(i).getPrice());
////                                mp.setRemainprice(mp.getRemainprice()-mp_list.get(i).getPrice());
////                                platoonOrderMapper.updateByPrimaryKeySelective(mp);
////                                //提现更新
////                                MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(mc_list.get(j).getId());
////                                mc.setStatus(1);
////                                mc.setMateprice(mc.getMateprice()+mc_list.get(j).getPrice());
////                                mc.setRemainprice(mc.getRemainprice()-mc_list.get(j).getPrice());
////                                cashapplyOrderMapper.updateByPrimaryKeySelective(mc);
////                                //从匹配池中移除
////                                mp_list.remove(i);
////                                mc_list.remove(j);
////                                break;
////                            }
////
////                        }
////                    }
////                }
////            }
////        }).start();
////    }
////    /**
////     * 作者：
////     * 时间： 2018/9/28 22:35
////     * 描述： 排单对象注入排队列
////     **/
////    public static void setPlatoon(MMateToPlatoon mp){
////        mp_list.add(mp);
////    }
////
////    /**
////     * 作者：
////     * 时间： 2018/10/4 13:41
////     * 描述： 获取排单队列
////     **/
////    public static List<MMateToPlatoon> getPlatoon(){return mp_list;}
////
////    /**
////     * 作者：
////     * 时间： 2018/9/28 22:36
////     * 描述： 提现对象注入排队列
////     **/
////    public static void setCashapply(MMateToCashapply mc){
////        mc_list.add(mc);
////    }
////
////    /**
////     * 作者：
////     * 时间： 2018/10/4 13:42
////     * 描述： 获取提现队列
////     **/
////    public static List<MMateToCashapply> getCashapply(){return mc_list;}
////
////    /**
////     * 作者：
////     * 时间： 2018/10/4 15:33
////     * 描述： 根据排单记录id获取在队列中的下标
////     **/
////    public static Integer getPlatoonIndex(Integer platoonId){
////        Integer platoonIndex = null;
////        for (int i = 0; i < mp_list.size(); i++) {
////            if (platoonId==mp_list.get(i).getId()){
////                platoonIndex = i;
////                break;
////            }
////        }
////        return platoonIndex;
////    }
////
////    /**
////     * 作者：
////     * 时间： 2018/10/4 15:33
////     * 描述： 根据提现记录id获取在队列中的下标
////     **/
////    public static Integer getCashapplyIndex(Integer cashapplyId){
////        Integer cashapplyIndex = null;
////        for (int i = 0; i < mc_list.size(); i++) {
////            if (cashapplyId==mc_list.get(i).getId()){
////                cashapplyIndex = i;
////                break;
////            }
////        }
////        return cashapplyIndex;
////    }
//}
