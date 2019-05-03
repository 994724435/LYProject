package com.liaoyin.lyproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.base.service.BaseService;
import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.common.bean.MMateToCashapply;
import com.liaoyin.lyproject.common.bean.SessionBean;
import com.liaoyin.lyproject.dao.*;
import com.liaoyin.lyproject.entity.*;
import com.liaoyin.lyproject.exception.BusinessException;
import com.liaoyin.lyproject.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 作者：
 * 时间：2018/9/28 21:22
 * 描述：
 */
@Service
public class MCashapplyOrderService extends BaseService<MCashapplyOrderMapper,MCashapplyOrder> {

    @Resource
    private SAccountMapper accountMapper;
    @Resource
    private SAccountRecordService accountRecordService;
    @Autowired
    private SUserService userService;
    @Resource
    private MMateMapper mateMapper;
    @Resource
    private SDictMapper dictMapper;
    @Resource
    private MPlatoonOrderMapper platoonOrderMapper;
    @Resource
    private MCashapplyOrderMapper cashapplyOrderMapper;
    @Resource
    private SAccountRecordMapper accountRecordMapper;

    @Transactional
    public synchronized JsonRestResponse insetCashapply(HttpServletRequest request, Integer price) {
        SessionBean bean = Config.getSessionBean(request);
        if (Common.isNull(bean)){
            throw new BusinessException("common.tokenInvalid");
        }
        if (!Config.cashapplyOrderIsTrue(price)){
            throw new BusinessException("common_cashapply_price_erro");
        }

        System.out.println("提现用户数据："+bean.getUserAccount() + bean.getUid().toString());

        //是否注册半年
//        int userid = accountMapper.getHalfYearUid(bean.getUserAccount());
//        System.out.println("注册半年的用户ID："+ userid);
//        if(userid == 0 ){
//           int refreenum = accountMapper.refereeNum(bean.getUserAccount());
//           int childAccountNum =accountMapper.childAccountNum(bean.getUserAccount());
//
//            System.out.println("用户推荐有效个数："+ refreenum);
//           if(refreenum >= childAccountNum && refreenum > 0 ){
//
//           }else {
//               throw new BusinessException("有效推荐人数至少"+ childAccountNum +"个");
//           }
//        }

        SAccount account = accountMapper.selectAccountUserId(bean.getUid());
        if (account.getCanprice()<price){
            throw new BusinessException("common_cashapply_price_not");
        }
        if (Common.isEqual(account.getStatus(),1)){
            throw new BusinessException("common.account.status.erro");
        }
        //判定资料度是否合格
        if (!userService.userInfoIsPerfect(bean.getUid(),request)){
            return RestUtil.createResponseErro(request.getSession().getAttribute("userInfoIsPerfectMsg").toString());
        }
        accountMapper.updateAccountPriceToReduce(bean.getUid(),price);
        accountRecordService.saveAccountRecord(Config.cashapplyPrice,price,account.getTotalprice()-price,
                0,0,3,null,bean.getUid(),account.getId());
        MCashapplyOrder co = new MCashapplyOrder();
        co.setPrice(price);
        co.setStatus(0);
        co.setMateprice(0);
        co.setRemainprice(price);
        co.setUserid(bean.getUid());
        co.setCreatedate(new Date());
        co.setMold(0);
        this.mapper.insertSelective(co);
        //co = this.mapper.selectOne(co);
        //注入连接池
        //MMateListener.setCashapply(new MMateToCashapply(co.getId(),price,bean.getUid()));
        //检测是否达到了提现封禁操作
        isCashapplyClosure(bean.getUid());
        //自检程序
        MCashapplyOrder cos = new MCashapplyOrder();
        co.setPrice(price);
        co.setStatus(0);
        co.setMateprice(0);
        co.setRemainprice(price);
        co.setUserid(bean.getUid());
        co.setCreatedate(new Date());
        co.setMold(0);
        selfChecking(cos);
        return RestUtil.createResponse();
    }

    /**
     * 作者：
     * 时间： 2018/11/21 15:32
     * 描述： 自检程序
     **/
    public synchronized void selfChecking(MCashapplyOrder cos){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    SAccount account = accountMapper.selectAccountUserId(cos.getUserid());
                    List<MCashapplyOrder> pp = cashapplyOrderMapper.select(cos);
                    for (int i = 1; i < pp.size(); i++) {
                        cashapplyOrderMapper.deleteByPrimaryKey(pp.get(i).getId());
                        account.setTotalprice(account.getTotalprice()+pp.get(i).getPrice());
                        account.setCanprice(account.getCanprice()+pp.get(i).getPrice());
                        accountMapper.updateByPrimaryKeySelective(account);
                    }
                    SAccountRecord record = new SAccountRecord();
                    record.setCreatedate(cos.getCreatedate());
                    record.setRecordprice(cos.getPrice());
                    record.setRecordstatus(0);
                    record.setRecordtype(0);
                    record.setRecordtouserid(cos.getUserid());
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
     * 时间： 2018/11/29 14:18
     * 描述： 提现封禁
     **/
    @Transactional
    public void isCashapplyClosure(Integer userId){
        SDict dict = dictMapper.selectDict(Config.DICT_CASHAPPLY_CLOSURE);
        if (Common.isEqual(dict.getRealvalue().intValue(),0)){
            SAccount account = accountMapper.selectAccountUserId(userId);
            SAccount a2 = new SAccount();
            a2.setAlipayaccount(account.getAlipayaccount());
            a2.setWxpayaccount(account.getWxpayaccount());
            a2.setBankaccount(account.getBankaccount());
            List<SAccount> a3 = accountMapper.select(a2);
            if (a3.size()>3){
                StringBuilder userIds = new StringBuilder();
                for (SAccount ac:a3) {
                    if (Common.isNull(userIds)){
                        userIds.append(ac.getUserid());
                    }else {
                        userIds.append(",").append(ac.getUserid());
                    }
                }
                List<MPlatoonOrder> p1 = platoonOrderMapper.selectUserPlantoonOrdersUserIds(userIds.toString());
                if (p1.size()/a3.size()>0.66){
                    for (SAccount ac:a3) {
                        this.mapper.updateCashapplyMold(ac.getUserid(),1);
                        accountMapper.updateAccountStatusToUserId(ac.getUserid(),1);
                    }
                }
            }
        }
    }

    public JsonRestResponse queryUserCashapplyOrderRecord(Integer userId,HttpServletRequest request, Integer startPage, Integer pageSize,
                                                          Integer cashapplyStatus) {
        PageHelper.startPage(startPage, pageSize);
        List<Map<String,Object>> datas = this.mapper.queryUserCashapplyOrderRecord(userId,cashapplyStatus);
        PageInfo<Map<String, Object>> p = new PageInfo<>(datas);
        return RestUtil.createResponseData(p);
    }

    public JsonRestResponse givePlatoon(HttpServletRequest request, Integer cashapplyId) {
        MCashapplyOrder cashapplyOrder = this.mapper.selectByPrimaryKey(cashapplyId);
        if (Common.isNull(cashapplyOrder)){
            throw new BusinessException("common.parm.erro");
        }
        if (!Common.isEqual(Config.getSessionBean(request).getUid(),cashapplyOrder.getUserid())){
            throw new BusinessException("common.erro");
        }
        List<MMate> list = mateMapper.selectMateToUser(null,cashapplyId,null,null,null,null);
        if (list.size()>0){
            throw new BusinessException("common.deletemate.erro");
        }
        cashapplyOrder.setStatus(2);
        this.mapper.updateByPrimaryKeySelective(cashapplyOrder);
        return RestUtil.createResponse();
    }
}
