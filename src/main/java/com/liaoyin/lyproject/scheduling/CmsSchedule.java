//package com.liaoyin.lyproject.scheduling;
//
//import com.alibaba.fastjson.JSONObject;
//import com.liaoyin.lyproject.base.constant.VariableConstants;
//import com.liaoyin.lyproject.service.StatsNewsService;
//import com.liaoyin.lyproject.service.StatsSystemService;
//import com.liaoyin.lyproject.service.StatsUnionsService;
//import com.liaoyin.lyproject.utilcms.HttpClientApiUtil;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//
//@Component
//@EnableScheduling
//@PropertySource("classpath:com/liaoyin/lyproject/scheduling/scheduling.properties")
//public class CmsSchedule {
//
//    @Resource
//    private StatsSystemService lyprojectSystemService;
//    @Resource
//    private StatsNewsService lyprojectNewsService;
//
//    @Value("${api.cms.lyproject.system.url}")
//    private String apiCmsStatsSystemUrl;
//    @Value("${api.cms.lyproject.news.url}")
//    private String apiCmsStatsNewsUrl;
//
//    //统计首页头部数据接口调用
////    @Scheduled(cron = "${scheduling.oneday}")
//    public void getStatsSystem()
//    {
//        String status = "0";
//        //接口循环调用，满足条件退出
//        for(int i=0; i< VariableConstants.API_GET_TIMES;i++) {
//            JSONObject jsonObject = null;
//            String result = null;
//            try {
//                result = HttpClientApiUtil.post(apiCmsStatsSystemUrl, null);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (StringUtils.isNotBlank(result)) {
//                result = com.liaoyin.lyproject.utilsms.Base64Util.decodeStr(result);
//                jsonObject = JSONObject.parseObject(result);
//                if (jsonObject.get("status") != null && "1".equals(jsonObject.get("status").toString())) {
//                    this.lyprojectSystemService.putStatsSystem(jsonObject);
//                    break;
//                }
//            }
//        }
//    }
//    //新闻发布数据CMS接口调用
//    @Scheduled(cron = "${scheduling.oneday}")
//    public void getStatsNews() {
//        String status = "0";
//        //接口循环调用，满足条件退出
//        for (int i = 0; i < VariableConstants.API_GET_TIMES; i++) {
//            JSONObject jsonObject = null;
//            String result = null;
//            try {
//                result = HttpClientApiUtil.post(apiCmsStatsNewsUrl, null);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (StringUtils.isNotBlank(result)) {
//                result = com.liaoyin.lyproject.utilsms.Base64Util.decodeStr(result);
//                jsonObject = JSONObject.parseObject(result);
//                if (jsonObject.get("status") != null && "1".equals(jsonObject.get("status").toString())) {
//                    this.lyprojectNewsService.putStatsNews(jsonObject);
//                    break;
//                }
//            }
//        }
//    }
//}
