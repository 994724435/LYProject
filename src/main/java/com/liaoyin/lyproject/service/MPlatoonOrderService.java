package com.liaoyin.lyproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.base.service.BaseService;
import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.common.bean.MMateToPlatoon;
import com.liaoyin.lyproject.common.bean.SessionBean;
import com.liaoyin.lyproject.dao.*;
import com.liaoyin.lyproject.entity.*;
import com.liaoyin.lyproject.exception.BusinessException;
import com.liaoyin.lyproject.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 作者：
 * 时间：2018/9/28 20:03
 * 描述：
 */
@Service
public class MPlatoonOrderService extends BaseService<MPlatoonOrderMapper,MPlatoonOrder> {


    @Resource
    private SDictMapper dictMapper;
    @Resource
    private SAccountMapper accountMapper;
    @Resource
    private SAccountRecordService accountRecordService;
    @Resource
    private SUserMapper userMapper;
    @Resource
    private MMateMapper mateMapper;
    @Autowired
    private SUserService userService;
    @Resource
    private MPlatoonOrderMapper platoonOrderMapper;
    @Resource
    private SAccountRecordMapper accountRecordMapper;
    @Resource
    private MCashapplyOrderMapper cashapplyOrderMapper;

    public JsonRestResponse userPlatoonOrder(HttpServletRequest request, Integer price) {
        SessionBean bean = Config.getSessionBean(request);
        if (Common.isNull(bean)){
            throw new BusinessException("common.tokenInvalid");
        }
        if (!Config.platoonOrderIsTrue(price)){
            throw new BusinessException("common_platoon_price_erro");
        }

        SUser user = userMapper.selectByPrimaryKey(bean.getUid());
        //判断是否会撞单
        if (user.getMold()==1){
            if (this.mapper.selectMplatoonOrderRecord(bean.getUid(),null,0).size()>0){
                throw new BusinessException("common.platoon.exist");
            }
        }else {
            if (this.mapper.selectMplatoonOrderRecord(bean.getUid(),null,0).size()>1){
                throw new BusinessException("common.platoon.erro");
            }
        }
        //判定资料度是否合格
        if (!userService.userInfoIsPerfect(bean.getUid(),request)){
            return RestUtil.createResponseErro(request.getSession().getAttribute("userInfoIsPerfectMsg").toString());
        }
        //获取排单最大值
        BigDecimal maxPrice = this.mapper.selectUserPlantMax(bean.getUid());
        Integer maxPrice_b = maxPrice.intValue()/2;
        if (maxPrice_b>price){
            return RestUtil.createResponseErro("排单失败，最少排"+maxPrice_b+"元");
        }
        //系统设定值
        SDict platMax = dictMapper.selectDict(Config.DICT_PLATOON_PRICEMAX);
        if (Common.isNull(platMax)){
            throw new BusinessException("common.dict.not");
        }
        SDict refereePrice = dictMapper.selectDict(Config.DICT_REFEREE_PRICE);
        if (Common.isNull(refereePrice)){
            throw new BusinessException("common.dict.not");
        }
        SDict platoonMaxPrice = dictMapper.selectDict(Config.DICT_PLATOON_MAX_PRICE);
        if (Common.isNull(platoonMaxPrice)){
            throw new BusinessException("common.dict.not");
        }
        Integer maxNum = platMax.getRealvalue().intValue();
        //根据推荐人数设定值
        SAccount account = accountMapper.selectAccountUserId(bean.getUid());
        maxNum+=account.getRefereeNum()*refereePrice.getRealvalue().intValue();
        if (maxNum>platoonMaxPrice.getRealvalue().intValue()){
            maxNum = platoonMaxPrice.getRealvalue().intValue();
        }
        if (price>maxNum){
            return RestUtil.createResponseErro("排单失败，最高可排"+maxNum+"元");
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
            accountRecordService.saveAccountRecord(Config.platoonIntgral,intgral,account.getIntegral()-intgral,
                    0,1,1,null,bean.getUid(),account.getId());
        }
        MPlatoonOrder po = new MPlatoonOrder();
        po.setPrice(price);
        po.setMateprice(0);
        po.setRemainprice(price);
        po.setUserid(bean.getUid());
        po.setStatus(0);
        po.setCreatedate(new Date());
        this.mapper.insertSelective(po);
        //自检程序
        MPlatoonOrder ps = new MPlatoonOrder();
        ps.setPrice(price);
        ps.setMateprice(0);
        ps.setRemainprice(price);
        ps.setUserid(bean.getUid());
        ps.setStatus(0);
        ps.setCreatedate(new Date());
        selfChecking(ps,intgral);
        //解封
        if (Common.isEqual(account.getStatus(),1)){
            cashapplyLiftBan(account);
        }
        //po = this.mapper.selectOne(po);
        //注入池
        //MMateListener.setPlatoon(new MMateToPlatoon(po.getId(),price,bean.getUid()));
        return RestUtil.createResponse();
    }

    /**
     * 作者：
     * 时间： 2018/11/21 15:32
     * 描述： 自检程序
     **/
    public void selfChecking(MPlatoonOrder ps,Integer intgral){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    SAccount account = accountMapper.selectAccountUserId(ps.getUserid());
                    List<MPlatoonOrder> pp = platoonOrderMapper.select(ps);
                    for (int i = 1; i < pp.size(); i++) {
                        platoonOrderMapper.deleteByPrimaryKey(pp.get(i).getId());
                        account.setIntegral(account.getIntegral()+intgral);
                        accountMapper.updateByPrimaryKeySelective(account);
                    }
                    SAccountRecord record = new SAccountRecord();
                    record.setCreatedate(ps.getCreatedate());
                    record.setRecordprice(intgral);
                    record.setRecordstatus(0);
                    record.setRecordtype(1);
                    record.setRecordtouserid(ps.getUserid());
                    List<SAccountRecord> lr = accountRecordMapper.select(record);
                    for (int i = 1; i < lr.size(); i++) {
                        accountRecordMapper.deleteByPrimaryKey(lr.get(i).getId());
                    }
                } catch (Exception e) {
                    System.out.println("自检程序出现异常");
                	e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 作者：
     * 时间： 2018/11/22 17:02
     * 描述： 提现解禁
     **/
    public void cashapplyLiftBan(SAccount account){
        SAccount a = new SAccount();
        a.setAlipayaccount(account.getAlipayaccount());
        a.setWxpayaccount(account.getWxpayaccount());
        a.setBankaccount(account.getBankaccount());
        List<SAccount> la = accountMapper.select(a);
        List<Integer> a1 = new ArrayList<>();
        List<Integer> c2 = new ArrayList<>();
        for (SAccount aa:la) {
            a1.add(aa.getUserid());
            MPlatoonOrder p1 = platoonOrderMapper.selectUserEndDatePlantoonOrders(aa.getUserid());
            MCashapplyOrder c1 = cashapplyOrderMapper.selectCashapplyEndDate(aa.getUserid());
            if (c1.getCreatedate().getTime()>p1.getCreatedate().getTime()){
                c2.add(aa.getUserid());
            }
        }
        Double dd = (double)c2.size()/(double) a1.size();
        if (dd<0.66){
            for (SAccount aa:la) {
                cashapplyOrderMapper.updateCashapplyMold(Integer.valueOf(aa.getUserid()),0);
                accountMapper.updateAccountStatusToUserId(Integer.valueOf(aa.getUserid()),0);
            }
        }
    }

    public JsonRestResponse queryUserPlatoonOrderRecord(Integer userId,HttpServletRequest request, Integer startPage, Integer pageSize,
                                                        Integer status,Integer platoonStatus) {
        PageHelper.startPage(startPage, pageSize);
        List<Map<String,Object>> datas = this.mapper.selectMplatoonOrderRecord(userId,status,platoonStatus);
        PageInfo<Map<String, Object>> p = new PageInfo<>(datas);
        return RestUtil.createResponseData(p);
    }

    public JsonRestResponse givePlatoon(HttpServletRequest request, Integer platoonId) {
        MPlatoonOrder platoonOrder = this.mapper.selectByPrimaryKey(platoonId);
        if (Common.isNull(platoonOrder)){
            throw new BusinessException("common.parm.erro");
        }
        if (!Common.isEqual(Config.getSessionBean(request).getUid(),platoonOrder.getUserid())){
            throw new BusinessException("common.erro");
        }
        List<MMate> list = mateMapper.selectMateToUser(platoonId,null,null,null,null,0);
        if (list.size()>0){
            throw new BusinessException("common.deletemate.erro");
        }
        platoonOrder.setStatus(2);
        this.mapper.updateByPrimaryKeySelective(platoonOrder);
        return RestUtil.createResponse();
    }
}
