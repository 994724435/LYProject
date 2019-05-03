package com.liaoyin.lyproject.common;

import com.alibaba.fastjson.JSON;
import com.liaoyin.lyproject.common.bean.SessionBean;
import com.liaoyin.lyproject.util.RedisUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

/**
 * 时间：2018/7/16 12:05
 * 描述：
 */
public class Config {


    //端口号
    public static final int port_websocket = 8087;
    public static final int port_socket = 8086;

    //客户端标识
    public static final String _PC = "_PC";
    public static final String _Android = "_Android";
    public static final String _IOS = "_IOS";
    public static final String _PCSYSTEM = "_PCSYSTEM";
    //请求生命周期，用于请求重复提交,单位：秒
    public static final int requestLifeCycle = 10;
    //自定义注解权证
    public static final String FiterAnnotaion = "_filterAnnotaion";
    public static final String RepeatRequest = "_repeatRequest";
    //controller路径
    public static final String controllerUrl = "com.liaoyin.lyproject.api";
    //工作域权重值
    public static final String _makePriceTime = "_makePriceTime";
    public static final String _receivablesTime = "_receivablesTime";
    public static final String _mateTime = "_mateTime";
    //后台管理用户电话
    public static final String[] ps = {"18119323313","18338222207","13479379401","18076196667","15393216067","18883287644","17392100391","18037355552"};

    //Redis获取SessionBean
    public static SessionBean getSessionBean(String key) {
        Object data = RedisUtil.get(key);
        if (Common.isNull(data))return null;
        return JSON.parseObject(data.toString(),SessionBean.class);
    }
    //Request获取SessionBean
    public static SessionBean getSessionBean(HttpServletRequest request) {
        String key = request.getHeader("FilterToken_MT");
        Object data = RedisUtil.get(key);
        System.out.println("data:"+data);
        if (Common.isNull(data))return null;
        return JSON.parseObject(data.toString(),SessionBean.class);
    }
    //Request获取SessionBean
    public static SessionBean getSessionBeanToSystem(HttpServletRequest request) {
        String key = request.getHeader("FilterToken_MT");
        Object data = RedisUtil.get(key+Config._PCSYSTEM);
        if (Common.isNull(data))return null;
        return JSON.parseObject(data.toString(),SessionBean.class);
    }
    //Request获取除开当前登录用户的其他后台人员id
    public static String getSessionBeanToSystemAll(HttpServletRequest request,Integer systemUserId) {
        SessionBean bean = getSessionBeanToSystem(request);
        if (Common.isNull(bean)) return null;
        Integer[] ids = {1,2,3,4};
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < ids.length; i++) {
            Integer id = ids[i];
            if (id!=bean.getUid() && id != systemUserId){
                data.append(id).append(",");
            }
        }
        return data.substring(0,data.length()-1);
    }

    //字典表系列编码
    public static final String DICT_SUNACCOUNT_NUM = "SUNACCOUNT_NUM";//子账号限制数
    public static final String DICT_CASHAPPLY_MULTIPLE = "DICT_CASHAPPLY_MULTIPLE";//提现整数倍
    public static final String DICT_CASHAPPLY_PRICEMIN = "DICT_CASHAPPLY_PRICEMIN";//提现最低值
    public static final String DICT_CASHAPPLY_PRICEMAX = "DICT_CASHAPPLY_PRICEMAX";//提现最高值
    public static final String DICT_PLATOON_PRICEMAX = "DICT_PLATOON_PRICEMAX";//排单最高值
    public static final String DICT_INTGRAL_RATION = "DICT_INTGRAL_RATION";//排单扣除积分比例
    public static final String DICT_REFEREE_PRICE = "DICT_REFEREE_PRICE";//推荐人每增加一个，增加的排单金额
    public static final String DICT_PLATOON_MAX_PRICE = "DICT_PLATOON_MAX_PRICE";//排单最终最大值(基于推荐人增额后的)
    public static final String DICT_MATE_PLATOON_RATIO = "DICT_MATE_PLATOON_RATIO";//排单利润比例(百分比)
    public static final String DICT_MATE_ROB_RATIO = "DICT_MATE_ROB_RATIO";//抢单利润比例（百分比）
    public static final String DICT_MATE_TIME = "DICT_MATE_TIME";//排单、抢单到账时间（小时整数）
    public static final String DICT_MAKEPRICE_TIME = "DICT_MAKEPRICE_TIME";//打款超时时间（小时整数）
    public static final String DICT_RECEIVABLES_TIME = "DICT_RECEIVABLES_TIME";//收款超时时间（小时整数）
    public static final String DICT_NOTPLATOONT_TIME = "DICT_NOTPLATOONT_TIME";//指定时间内未排单（小时整数）
    public static final String DICT_VERSION_NUM = "DICT_VERSION_NUM";//版本号
    public static final String DICT_ROBORDER_TIME = "DICT_ROBORDER_TIME";//用户重复抢单时间限制（小时整数）
    public static final String DICT_CASHAPPLY_CLOSURE = "DICT_CASHAPPLY_CLOSURE";//防撤资开关：0-开 1-关




    //Redis业务键
    public static final String robKey = "rob_key";


    //其他提示
    public static final String sysTitle = "系统提示";
    public static final String platoonIntgral = "排单扣除积分";
    public static final String robIntgral = "抢单扣除积分";
    public static final String cashapplyPrice = "提现扣除余额";
    public static final String giftIntegral_ZS="积分赠送";
    public static final String giftIntegral_JS="推荐人赠送";

    public static final String REGISTER = "REGISTER";// 注册时验证码类型
    public static final String UPDATEPASSWORD = "UPDATEPASSWORD";// 修改密码验证码类型
    public static final String FORGETPASSWORD = "FORGETPASSWORD";// 忘记验证码类型
    public static final String SAVESUNACCOUNT = "SAVESUNACCOUNT";// 添加子账号验证码类型
    public static final String SYSTEMUSERLOGIN = "SYSTEMUSERLOGIN";// 系统用户登录
    public static final String UPDATEUSERINFO = "UPDATEUSERINFO";//修改用户资料

    //公共方法
    //判断排单金额是否符合要求
    public static boolean platoonOrderIsTrue(Integer price){
        if (Common.isNull(price))return false;
        if (price<=0)return false;
        if (price%1000!=0){
            return false;
        }
        return true;
    }
    //判断提现金额是否符合要求
    public static boolean cashapplyOrderIsTrue(Integer price){
        if (Common.isNull(price))return false;
        if (price<1000)return false;
        if (price<=0)return false;
        if (price%100!=0){
            return false;
        }
        return true;
    }
    //登录ip是否合法
    public static boolean loginIpIsTrue(String ip){
        String[] ips = {"27.224.229.161","27.224.228.17","183.205.5.6","27.224.224.144","47.75.142.63"};
        return Arrays.asList(ips).contains(ip);
    }

    //生成任务调度表达式_小时
    public static String expressionHour(Integer hour,Date de){
        Date date = CommonDate.appointDateHour(de,hour);
        StringBuilder bds = new StringBuilder();
        bds.append(date.getSeconds()).append(" ").append(date.getMinutes())
                .append(" ").append(date.getHours()).append(" ")
                .append(CommonDate.appointDateDay(date)).append(" ").append(CommonDate.appointDateMonth(date))
                .append(" ? ").append(CommonDate.appointDateYear(date));
        return bds.toString();
    }



    //生成任务调度表达式_指定时间
    public static String expressionDate(Date date){
        StringBuilder bds = new StringBuilder();
        bds.append(date.getSeconds()).append(" ").append(date.getMinutes())
                .append(" ").append(date.getHours()).append(" ")
                .append(CommonDate.appointDateDay(date)).append(" ").append(CommonDate.appointDateMonth(date))
                .append(" ? ").append(CommonDate.appointDateYear(date));
        return bds.toString();
    }



    //客服id
    public static Integer[] kfids = {5,9,13,14};
    public static Integer khid(String phone){
        if (Common.isEqual("13479379401",phone)){
            return 5;
        }else if(Common.isEqual("18119323313",phone)){
            return 13;
        }else if(Common.isEqual("18076196667",phone)){
            return 14;
        }else if(Common.isEqual("18338222207",phone)){
            return 9;
        }
        return -1;
    }
    public static Integer pdid(String phone){
        if (Common.isEqual("18093276567",phone)){
            return 333;
        }else if(Common.isEqual("18184318719",phone)){
            return 14;
        }else if(Common.isEqual("18037355552",phone)){
            return 87;
        }else if(Common.isEqual("13970362988",phone)){
            return 1080;
        }
        return null;
    }

}
