package com.liaoyin.lyproject.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.base.service.BaseService;
import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.common.bean.SessionBean;
import com.liaoyin.lyproject.dao.*;
import com.liaoyin.lyproject.entity.*;
import com.liaoyin.lyproject.exception.BusinessException;
import com.liaoyin.lyproject.util.RedisUtil;
import com.liaoyin.lyproject.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 作者：
 * 时间：2018/10/9 15:56
 * 描述：
 */
@Service
public class MRobService extends BaseService<MRobMapper,MRob> {

    @Resource
    private SUserMapper userMapper;
    @Resource
    private SDictMapper dictMapper;
    @Resource
    private SAccountMapper accountMapper;
    @Autowired
    private SAccountRecordService accountRecordService;
    @Resource
    private MRobOrderMapper robOrderMapper;
    @Resource
    private MPlatoonOrderMapper platoonOrderMapper;


    public JsonRestResponse selectRob(HttpServletRequest request) {
        Object data = RedisUtil.get(Config.robKey);
        if (Common.isNull(data))return RestUtil.createResponse();
        return RestUtil.createResponse(JSON.parseObject(data.toString(),MRob.class));
    }

    public synchronized JsonRestResponse executeRob(Integer price, HttpServletRequest request) {
        Object data = RedisUtil.get(Config.robKey);
        if (Common.isNull(data)){
            throw new BusinessException("common.rob.not");
        }
        SessionBean bean = Config.getSessionBean(request);
        if (Common.isNull(bean)){
            throw new BusinessException("common.tokenInvalid");
        }
        if (!Config.platoonOrderIsTrue(price)){
            throw new BusinessException("common_platoon_price_erro");
        }
        //只能抢一单_根据最后一次时间来判断
        MRobOrder ro = robOrderMapper.selectUserRobOrderDateDesc(bean.getUid());
        if (!Common.isNull(ro)){
            SDict roTime = dictMapper.selectDict(Config.DICT_ROBORDER_TIME);
            if (Common.isNull(roTime)){
                throw new BusinessException("common.dict.not");
            }
            int dt = roTime.getRealvalue().intValue()*60*60*1000;
            if (ro.getCreatedate().getTime()+dt>System.currentTimeMillis()){
                return RestUtil.createResponseErro("抢单失败，"+roTime.getRealvalue().intValue()+"小时内不能重复抢单");
            }
        }
        MPlatoonOrder platoonOrder = platoonOrderMapper.selectUserEndDatePlantoonOrder(bean.getUid());
        if (Common.isNull(platoonOrder)){
            throw new BusinessException("common.robOrder.not.plantOrder");
        }
        if (price>platoonOrder.getPrice()/2){
            throw new BusinessException("common.robOrder.plantOrder.erro");
        }
//        SUser user = userMapper.selectByPrimaryKey(bean.getUid());
//        //判断是否会撞单
//        if (user.getMold()==1){
//            if (robOrderMapper.selectUserAllRobOrderToStatus(bean.getUid(),0).size()>0){
//                throw new BusinessException("common.platoon.exist");
//            }
//        }else {
//            if (robOrderMapper.selectUserAllRobOrderToStatus(bean.getUid(),0).size()>1){
//                throw new BusinessException("common.platoon.erro");
//            }
//        }
        //获取排单最大值
        //系统设定值
//        SDict platMax = dictMapper.selectDict(Config.DICT_PLATOON_PRICEMAX);
//        if (Common.isNull(platMax)){
//            throw new BusinessException("common.dict.not");
//        }
//        SDict refereePrice = dictMapper.selectDict(Config.DICT_REFEREE_PRICE);
//        if (Common.isNull(refereePrice)){
//            throw new BusinessException("common.dict.not");
//        }
//        SDict platoonMaxPrice = dictMapper.selectDict(Config.DICT_PLATOON_MAX_PRICE);
//        if (Common.isNull(platoonMaxPrice)){
//            throw new BusinessException("common.dict.not");
//        }
//        Integer maxNum = platMax.getRealvalue().intValue();
//        //根据推荐人数设定值
//        SAccount account = accountMapper.selectAccountUserId(bean.getUid());
//        maxNum+=account.getRefereeNum()*refereePrice.getRealvalue().intValue();
//        if (maxNum>platoonMaxPrice.getRealvalue().intValue()){
//            maxNum = platoonMaxPrice.getRealvalue().intValue();
//        }
//        if (price>maxNum){
//            return RestUtil.createResponseErro("抢单失败，最高限额"+maxNum+"元");
//        }
        SAccount account = accountMapper.selectAccountUserId(bean.getUid());
        MRob rob = JSON.parseObject(data.toString(),MRob.class);
        if (rob.getRemainprice()<price){
            return RestUtil.createResponseErro("抢单失败，当前可抢金额为"+rob.getRemainprice()+"元");
        }
        //获取扣除积分比例
        SDict intgralRatio = dictMapper.selectDict(Config.DICT_INTGRAL_RATION);
        if (intgralRatio.getRealvalue()<0 && intgralRatio.getRealvalue()>1){
            throw new BusinessException("common.dict.erro");
        }
        //扣除积分值
        Double intgralDouble = price*intgralRatio.getRealvalue();
        Integer intgral = intgralDouble.intValue();
        if (intgral>0){
            if (account.getIntegral()<intgral){
                throw new BusinessException("common.intgral.not");
            }
            accountMapper.updateAccountIntgralToReduce(bean.getUid(),intgral);
            accountRecordService.saveAccountRecord(Config.robIntgral,intgral,account.getIntegral()-intgral,
                    0,1,1,null,bean.getUid(),account.getId());
        }
        rob.setMateprice(rob.getMateprice()+price);
        rob.setRemainprice(rob.getRemainprice()-price);
        if (rob.getRemainprice()<=0){
            rob.setStatus(1);
            RedisUtil.del(Config.robKey);
        }else{
            RedisUtil.set(Config.robKey,JSON.toJSONString(rob));
        }
        this.mapper.updateByPrimaryKeySelective(rob);
        MRobOrder robOrder = new MRobOrder();
        robOrder.setPrice(price);
        robOrder.setMateprice(0);
        robOrder.setRemainprice(price);
        robOrder.setUserid(bean.getUid());
        robOrder.setBatchnum(rob.getBatchnum());
        robOrder.setStatus(0);
        robOrder.setCreatedate(new Date());
        robOrderMapper.insertSelective(robOrder);
        return RestUtil.createResponse();
    }

    public JsonRestResponse selectUserRobOrderRecord(Integer userId,HttpServletRequest request,
                                                     Integer startPage, Integer pageSize,
                                                     Integer status) {
        PageHelper.startPage(startPage, pageSize);
        List<Map<String, Object>> users = robOrderMapper.selectUserRobOrderRecord(userId,status);
        PageInfo<Map<String, Object>> p = new PageInfo<>(users);
        return RestUtil.createResponseData(p,robOrderMapper.selectUserRobOrderRecordSumPrice(userId,status));
    }
}
