package com.liaoyin.lyproject.service;

import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.base.service.BaseService;
import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.CommonDate;
import com.liaoyin.lyproject.common.CommonParamter;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.common.util.EncryptUtils;
import com.liaoyin.lyproject.common.util.PostCommon;
import com.liaoyin.lyproject.dao.MSmsCodeMapper;
import com.liaoyin.lyproject.entity.MSmsCode;
import com.liaoyin.lyproject.exception.BusinessException;
import com.liaoyin.lyproject.util.RedisUtil;
import com.liaoyin.lyproject.util.RestUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：
 * 时间：2018/9/25 9:46
 * 描述：
 */
@Service
public class MSmsCodeService extends BaseService<MSmsCodeMapper,MSmsCode> {

    public synchronized JsonRestResponse sendCode(String phone, String type) {
        if (phone.length()!=11){
            throw new BusinessException("common.phone.erro");
        }
        if (!Common.isEqual(type, Config.SYSTEMUSERLOGIN)){
            throw new BusinessException("common.parm.erro");
        }
        if (!Common.isExist(Config.ps,phone)){
            throw new BusinessException("common.parm.erro");
        }
        Object code = Common.randomNumber(6);
        Map<String, Object> datas = new LinkedHashMap<>();
        datas.put("CorpID", "CQJS003210");
        datas.put("Pwd", "xsy@123");
        datas.put("Mobile", phone);
        datas.put("Content", "您的验证码为："+code);
        datas.put("Cell", "");
        datas.put("SendTime", "");
        try {
            String returnDate = PostCommon.jsonPost(CommonParamter.KYUrl,datas,"GB2312");
            System.out.println("返回值："+returnDate);
            Integer returnCode = getContext(returnDate);
            if (returnCode>0) {
                MSmsCode ms = new MSmsCode();
                ms.setCode(code.toString());
                ms.setPhone(phone);
                ms.setType(type);
                ms.setCreatedate(new Date());
                this.insert(ms);
                RedisUtil.set(phone+type,code,Long.valueOf(15*60));
                return RestUtil.createResponseCode("common.smscode.ok");
            }else{
                Map<String,Object> erroData = new LinkedHashMap<>();
                erroData.put("code",returnCode);
                erroData.put("codeMsg",CommonParamter.KYErroCode);
                return RestUtil.createResponse("common.sendCode.Erro",erroData);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new BusinessException("common.sendCode.Erro");
        }
    }

    /**
     * 作者：
     * 时间： 2018/10/9 10:58
     * 描述： 读取节点内的内容
     **/
    public static Integer getContext(String html) {
        return Integer.valueOf(html.substring(html.indexOf("http://tempuri.org/\">")+"http://tempuri.org/\">".length(),html.indexOf("</string")));
    }

    public JsonRestResponse validateCodeIsOk(String phone, String type, String code) {
        Object rc = RedisUtil.get(phone+type);
        if (Common.isNull(rc)){
            throw new BusinessException("common.sendCode.not");
        }
        MSmsCode smsCode = this.mapper.selectCodeNewest(phone,type);
        if (Common.isNull(smsCode)){
            throw new BusinessException("common.sendCode.not");
        }
        if (!Common.isEqual(code,rc.toString()) || !Common.isEqual(code,smsCode.getCode())){
            throw new BusinessException("common.sendCode.Code.Erro");
        }
        return RestUtil.createResponse();
    }

    /**
     * 作者：
     * 时间： 2018/10/22 15:54
     * 描述： 发送匹配信息
     **/
    public synchronized static void sendMsg(String phone,Integer status){
        String content = "";
        switch (status){
            case 0://发货通知
                content = "亲爱的会员，您帐号"+phone+"申请的订单已经成功签单，请您登陆后台操作，及时发货。一切以后台信息为准！-"+CommonDate.dateToString(new Date());
                break;
            case 1://收货
                content = "亲爱的会员，您帐号"+phone+"申请的订单已经成功签单，请您登陆后台操作，收货后请及时确认。一切以后台信息为准！-"+CommonDate.dateToString(new Date());
                break;
        }
        Map<String, Object> datas = new LinkedHashMap<>();
        datas.put("CorpID", "CQJS003210");
        datas.put("Pwd", "xsy@123");
        datas.put("Mobile", phone);
        datas.put("Content", content);
        datas.put("Cell", "");
        datas.put("SendTime", "");
        try {
            String returnDate = PostCommon.jsonPost(CommonParamter.KYUrl,datas,"GB2312");
            System.out.println("返回值："+returnDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
