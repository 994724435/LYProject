//package com.liaoyin.lyproject.listener;
//
//import com.liaoyin.lyproject.common.CommonDate;
//import com.liaoyin.lyproject.dao.MJobMapper;
//import com.liaoyin.lyproject.dao.MOverTimeJobMapper;
//import com.liaoyin.lyproject.dao.SUserMapper;
//import com.liaoyin.lyproject.entity.MJob;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
///**
// * 作者：
// * 时间：2018/10/24 9:28
// * 描述：任务定时器
// */
//@Component
//@Slf4j
//public class SignCancellationService {
//
//    @Resource
//    private SUserMapper userMapper;
//    @Resource
//    private MJobMapper jobMapper;
//    @Resource
//    private MOverTimeJobMapper overTimeJobMapper;
//
//    /**
//     * 作者：
//     * 时间： 2018/10/24 9:29
//     * 描述： 凌晨一点在指定时间内未排单的用户执行封号
//     **/
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void notPlanToonOrderTime(){
////        log.info("---------------------定时任务开始执行，当前时间："+CommonDate.dateToString(new Date())+"---------------------");
////        //直接更新
////        userMapper.updateNotPlanToonOrderTimeUser();
////        userMapper.updateNotPlanToonOrderTimeUserTwo();
////        log.info("---------------------定时任务结束执行，当前时间："+CommonDate.dateToString(new Date())+"---------------------");
//        //将错误数据删除
//        List<Map<String,Object>> datas = jobMapper.selectErroData();
//        for (int i = 0; i < datas.size(); i++) {
//            List<MJob> d2 = jobMapper.selectJobListToMateId((Integer) datas.get(i).get("mateId"));
//            for (int j = 1; j < d2.size(); j++) {
//                jobMapper.deleteByPrimaryKey(d2.get(j).getId());
//            }
//        }
//    }
//}
