package com.liaoyin.lyproject.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.CommonDate;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.common.bean.MMateToCashapply;
import com.liaoyin.lyproject.common.bean.MMateToPlatoon;
import com.liaoyin.lyproject.common.bean.SessionBean;
import com.liaoyin.lyproject.common.util.AESUtils;
import com.liaoyin.lyproject.dao.*;
import com.liaoyin.lyproject.entity.*;
import com.liaoyin.lyproject.exception.BusinessException;
import com.liaoyin.lyproject.util.RedisUtil;
import com.liaoyin.lyproject.util.RestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 作者：
 * 时间：2018/9/30 11:08
 * 描述：
 */
@Service
@Slf4j
public class TSystemService {

    @Resource
    private SUserLoginLogMapper userLoginLogMapper;
    @Resource
    private MSmsCodeMapper smsCodeMapper;
    @Resource
    private TSystemUserMapper systemUserMapper;
    @Resource
    private TSystemUserOperationLogMapper systemUserOperationLogMapper;
    @Resource
    private SUserMapper userMapper;
    @Resource
    private MPlatoonOrderMapper platoonOrderMapper;
    @Resource
    private MCashapplyOrderMapper cashapplyOrderMapper;
    @Resource
    private MMateMapper mateMapper;
    @Resource
    private TSystemUserLockVoteMapper systemUserLockVoteMapper;
    @Resource
    private MRobMapper robMapper;
    @Resource
    private MRobOrderMapper robOrderMapper;
    @Resource
    private SAccountMapper accountMapper;
    @Resource
    private TUserIntegralRecordMapper userIntegralRecordMapper;
    @Autowired
    private MMateService mateService;
    @Resource
    private SAccountRecordMapper accountRecordMapper;
    @Resource
    private SDictMapper dictMapper;
    @Autowired
    private SAccountRecordService accountRecordService;
    @Autowired
    private SUserService userService;

    /**
     * 作者：
     * 时间： 2018/9/30 16:17
     * 描述： 系统用户敏感信息过滤
     **/
    public static TSystemUser userInfoFilter(TSystemUser user) {
        user.setUserpassword(null);
        return user;
    }

    public JsonRestResponse selectUserLoginLog(String key, String startDate, String endDate, Integer startPage,
                                               Integer pageSize, HttpServletRequest request) {
        PageHelper.startPage(startPage, pageSize);
        List<SUserLoginLog> lists = userLoginLogMapper.selectUserLoginLog(key, CommonDate.StringTodateYMD(startDate),
                CommonDate.StringTodateYMD(endDate));
        return RestUtil.createResponseData(new PageInfo(lists));
    }

    public JsonRestResponse systemUserLogin(String phone, String password, String code, String ip, HttpServletRequest request) {
        Object rc = RedisUtil.get(phone + Config.SYSTEMUSERLOGIN);
        if (Common.isNull(rc)) {
            throw new BusinessException("common.sendCode.not");
        }
        if (!Common.isEqual(rc.toString(), code)) {
            throw new BusinessException("common.sendCode.Code.Erro");
        }
        MSmsCode smsCode = smsCodeMapper.selectCodeNewest(phone, Config.SYSTEMUSERLOGIN);
        if (Common.isNull(smsCode)) {
            throw new BusinessException("common.sendCode.not");
        }
        if (!Common.isEqual(smsCode.getCode(), code)) {
            throw new BusinessException("common.sendCode.Code.Erro");
        }

//        if (!Config.loginIpIsTrue(ip)) {
//            throw new BusinessException("common.systemlogin.ip.erro");
//        }
        SDict dict = dictMapper.selectDict("DICT_IP");
        if (!ip.equals(dict.getDisplayvalue().toString())){
            throw new BusinessException("common.systemlogin.ip.erro");
        }
        TSystemUser tu = systemUserMapper.selectSystemUserPhone(phone);
        if (Common.isNull(tu)) {
            throw new BusinessException("common.systemlogin.loginAccount.not");
        }
        if (!Common.isEqual(Common.personEncrypt3(password), tu.getUserpassword())) {
            throw new BusinessException("common.user.passwordErro");
        }
        if (tu.getIslock()!=0){
            throw new BusinessException("common.systemuser.lock");
        }
        tu.setLastlogindate(new Date());
        systemUserMapper.updateByPrimaryKeySelective(tu);
        TSystemUserOperationLog logs = new TSystemUserOperationLog();
        logs.setUseraccount(phone);
        logs.setUserpassword(Common.personEncrypt3(password));
        logs.setRealname(tu.getRealname());
        logs.setStatus(0);
        logs.setRequestip(request.getRemoteAddr());
        logs.setRequestthread(Thread.currentThread().getName());
        logs.setCreatedate(new Date());
        logs.setMessage("后台系统登录！");
        systemUserOperationLogMapper.insertSelective(logs);
        String token = tu.getToken();
        if (Common.isNull(token) || Common.isNull(RedisUtil.get(token+Config._PCSYSTEM))) {
            try {
                token = AESUtils.encrypt(String.valueOf(Common.numberOnlyRoomId()), String.valueOf(tu.getId()));
                //将该token绑定到用户库对应的字段中
                tu.setToken(token);
                systemUserMapper.updateByPrimaryKeySelective(tu);
                //1.将用户对应的信息注入userInfo
                SessionBean userInfo = new SessionBean();
                userInfo.setUid(tu.getId());
                userInfo.setUserAccount(tu.getUseraccount());
                userInfo.setUserNickName(tu.getRealname());

                //2.将token、用户信息放入Redis，并设置对应的过期时间（7天）
                RedisUtil.set(token+Config._PCSYSTEM, JSON.toJSONString(userInfo), Long.valueOf(7 * 24 * 60 * 60));
            } catch (Exception e) {
                e.printStackTrace();
                log.error("token块儿出现异常");
            }
        } else {
            //存在时表示当前库中的token是有效的，在这里不做任何操作，如需做操作请根据业务来做
        }
        return RestUtil.createResponse(userInfoFilter(tu));
    }

    public JsonRestResponse selectSystemUserLoginLog(String key, String startDate, String endDate, Integer startPage,
                                                     Integer pageSize, HttpServletRequest request, Integer status) {
        PageHelper.startPage(startPage, pageSize);
        List<TSystemUserOperationLog> lists = systemUserOperationLogMapper.selectSystemUserLoginLog(key, CommonDate.StringTodateYMD(startDate),
                CommonDate.StringTodateYMD(endDate), status);
        return RestUtil.createResponseData(new PageInfo(lists));
    }

    public JsonRestResponse queryUser(String key, Integer status, Integer refereeId, Integer startPage, Integer pageSize,
                                      Integer isDelete) {
        PageHelper.startPage(startPage, pageSize);
        List<Map<String, Object>> users = userMapper.selectUserListMapSystem(key, status, refereeId,isDelete,null);
        PageInfo<Map<String, Object>> p = new PageInfo<>(users);
        return RestUtil.createResponseData(p);
    }

    public JsonRestResponse selectPlatoonOrderNumAndCashapplyOrderNum(HttpServletRequest request){
        Map<String,Integer> data = new HashMap<>();
//        data.put("platoonOrderNum",MMateListener.getPlatoon().size());
//        data.put("cashapplyOrderNum",MMateListener.getCashapply().size());
        return RestUtil.createResponse(data);
    }

    public JsonRestResponse selectPlatoonOrderAndCashapplyOrder(String key, String startDate, String endDate,
                                                                Integer startPage, Integer pageSize, HttpServletRequest
                                                                        request, Integer status,Integer mold) {
        PageHelper.startPage(startPage, pageSize);
        List<Map<String, Object>> datas = null;
        switch (status) {
            case 0://排单
                datas = platoonOrderMapper.selectPlatoonOrderSystem(key, CommonDate.StringTodateYMD(startDate),
                        CommonDate.StringTodateYMD(endDate),mold);
                break;
            case 1://提现
                datas = cashapplyOrderMapper.selectCashapplyOrderSystem(key, CommonDate.StringTodateYMD(startDate),
                        CommonDate.StringTodateYMD(endDate),mold);
                break;
        }
        return RestUtil.createResponseData(new PageInfo(datas));
    }

    public synchronized JsonRestResponse matePlatoonOrderAndCashapplyOrder(Integer num,Integer cashapplyId,
                                                                           Integer status,HttpServletRequest request) {
        if (Common.isNull(status)||(status!=0 && status!=1)){
            throw new BusinessException("common.parm.erro");
        }
        List<MPlatoonOrder> mp_list = platoonOrderMapper.selectMplatoonOrderNot();
        if (mp_list.size() == 0) {
            throw new BusinessException("common.mate.platoon.not");
        }
        Integer price = 0;//权重金额
        switch (status){
            case 0://根据数值匹配
                if (Common.isNull(num)){
                    throw new BusinessException("common.parm.erro");
                }
                List<MCashapplyOrder> mc_list = cashapplyOrderMapper.selectCashapplyOrderNot();
                if (mc_list.size() == 0) {
                    throw new BusinessException("common.mate.caspply.not");
                }
                int forNum = mc_list.size();//默认循环对列数
                //对列数大于指定数时，循环指定数
                if (forNum > num) {
                    forNum = num;
                }
                for (int i = 0; i < forNum;i++) {
                    for (int j = 0; j < mc_list.size();j++) {
                        System.out.println("排单："+mp_list.get(i).getRemainprice());
                        System.out.println("提现："+mc_list.get(j).getRemainprice());
                        if (mp_list.get(i).getRemainprice()<=0){
                            continue;
                        }
                        if (mc_list.get(j).getRemainprice()<=0){
                            continue;
                        }
                        //如果抢单和提现是同一个人  跳过此次匹配
                        if (Common.isEqual(mp_list.get(i).getUserid(),mc_list.get(j).getUserid())){
                            continue;
                        }
                        if (mp_list.get(i).getRemainprice() < mc_list.get(j).getRemainprice()) {//提现池金额大
                            price = mp_list.get(i).getRemainprice();
                            //注入匹配记录
                            MMate mate = new MMate();
                            mate.setPrice(price);
                            mate.setStatus(0);
                            mate.setPlantoonuserid(mp_list.get(i).getUserid());
                            mate.setCashapplyuserid(mc_list.get(j).getUserid());
                            mate.setCreatedate(new Date());
                            mate.setType(0);
                            mate.setPlanToonId(mp_list.get(i).getId());
                            mate.setCashapplyId(mc_list.get(j).getId());
                            mateMapper.insertSelective(mate);
                            //注入工作域
                            mateService.overtimeJob(0,mp_list.get(i).getUserid(),mateMapper.selectOne(mate).getId());
                            //排单更新
                            //MPlatoonOrder mp = platoonOrderMapper.selectByPrimaryKey(mp_list.get(i).getId());
                            mp_list.get(i).setMateprice(mp_list.get(i).getMateprice() + price);
                            mp_list.get(i).setRemainprice(mp_list.get(i).getRemainprice() - price);
                            mp_list.get(i).setStatus(1);
                            platoonOrderMapper.updateByPrimaryKeySelective(mp_list.get(i));
                            //提现更新
                            //MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(mc_list.get(j).getId());
                            mc_list.get(j).setMateprice(mc_list.get(j).getMateprice() + price);
                            mc_list.get(j).setRemainprice(mc_list.get(j).getRemainprice() - price);
                            cashapplyOrderMapper.updateByPrimaryKeySelective(mc_list.get(j));
                            break;
                        } else if (mp_list.get(i).getRemainprice() > mc_list.get(j).getRemainprice()) {//排单池金额大
                            price = mc_list.get(j).getRemainprice();
                            //注入匹配记录
                            MMate mate = new MMate();
                            mate.setPrice(price);
                            mate.setStatus(0);
                            mate.setPlantoonuserid(mp_list.get(i).getUserid());
                            mate.setCashapplyuserid(mc_list.get(j).getUserid());
                            mate.setCreatedate(new Date());
                            mate.setType(0);
                            mate.setPlanToonId(mp_list.get(i).getId());
                            mate.setCashapplyId(mc_list.get(j).getId());
                            mateMapper.insertSelective(mate);
                            //注入工作域
                            mateService.overtimeJob(0,mp_list.get(i).getUserid(),mateMapper.selectOne(mate).getId());
                            //排单更新
                            //MPlatoonOrder mp = platoonOrderMapper.selectByPrimaryKey(mp_list.get(i).getId());
                            mp_list.get(i).setMateprice(mp_list.get(i).getMateprice() + price);
                            mp_list.get(i).setRemainprice(mp_list.get(i).getRemainprice() - price);
                            platoonOrderMapper.updateByPrimaryKeySelective(mp_list.get(i));
                            //提现更新
                            //MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(mc_list.get(j).getId());
                            mc_list.get(j).setStatus(1);
                            mc_list.get(j).setMateprice(mc_list.get(j).getMateprice() + price);
                            mc_list.get(j).setRemainprice(mc_list.get(j).getRemainprice() - price);
                            cashapplyOrderMapper.updateByPrimaryKeySelective(mc_list.get(j));
                        } else {//两者相等
                            price = mc_list.get(j).getRemainprice();
                            //注入匹配记录
                            MMate mate = new MMate();
                            mate.setPrice(price);
                            mate.setStatus(0);
                            mate.setPlantoonuserid(mp_list.get(i).getUserid());
                            mate.setCashapplyuserid(mc_list.get(j).getUserid());
                            mate.setCreatedate(new Date());
                            mate.setType(0);
                            mate.setPlanToonId(mp_list.get(i).getId());
                            mate.setCashapplyId(mc_list.get(j).getId());
                            mateMapper.insertSelective(mate);
                            //注入工作域
                            mateService.overtimeJob(0,mp_list.get(i).getUserid(),mateMapper.selectOne(mate).getId());
                            //排单更新
                            //MPlatoonOrder mp = platoonOrderMapper.selectByPrimaryKey(mp_list.get(i).getId());
                            mp_list.get(i).setStatus(1);
                            mp_list.get(i).setMateprice(mp_list.get(i).getMateprice() + price);
                            mp_list.get(i).setRemainprice(mp_list.get(i).getRemainprice() - price);
                            platoonOrderMapper.updateByPrimaryKeySelective(mp_list.get(i));
                            //提现更新
                            //MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(mc_list.get(j).getId());
                            mc_list.get(j).setStatus(1);
                            mc_list.get(j).setMateprice(mc_list.get(j).getMateprice() + price);
                            mc_list.get(j).setRemainprice(mc_list.get(j).getRemainprice() - price);
                            cashapplyOrderMapper.updateByPrimaryKeySelective(mc_list.get(j));
                            break;
                        }
                    }
                }
                break;
            case 1://指定提现记录匹配
                if (Common.isNull(cashapplyId)){
                    throw new BusinessException("common.parm.erro");
                }
                MCashapplyOrder nowCashapply = cashapplyOrderMapper.selectByPrimaryKey(cashapplyId);
                if (Common.isNull(nowCashapply)){
                    throw new BusinessException("common.mate.caspply.not.erro");
                }
                if (!Common.isEqual(nowCashapply.getStatus(),0)){
                    throw new BusinessException("common.mate.cashapplyId.not");
                }
                if (Common.isEqual(nowCashapply.getMold(),1)){
                    throw new BusinessException("common.cashapply.mold.erro");
                }
                for (int i = 0; i < mp_list.size();i++) {
                    if (mp_list.get(i).getRemainprice()<=0){
                        continue;
                    }
                    if (Common.isEqual(mp_list.get(i).getUserid(),nowCashapply.getUserid())) {
                        continue;
                    }
                    if (mp_list.get(i).getRemainprice() < nowCashapply.getRemainprice()) {//提现池金额大
                        price = mp_list.get(i).getRemainprice();
                        //注入匹配记录
                        MMate mate = new MMate();
                        mate.setPrice(price);
                        mate.setStatus(0);
                        mate.setPlantoonuserid(mp_list.get(i).getUserid());
                        mate.setCashapplyuserid(nowCashapply.getUserid());
                        mate.setCreatedate(new Date());
                        mate.setType(0);
                        mate.setPlanToonId(mp_list.get(i).getId());
                        mate.setCashapplyId(nowCashapply.getId());
                        mateMapper.insertSelective(mate);
                        //注入工作域
                        mateService.overtimeJob(0,mp_list.get(i).getUserid(),mateMapper.selectOne(mate).getId());
                        //排单更新
                        //MPlatoonOrder mp = platoonOrderMapper.selectByPrimaryKey(mp_list.get(i).getId());
                        mp_list.get(i).setMateprice(mp_list.get(i).getMateprice() + price);
                        mp_list.get(i).setRemainprice(mp_list.get(i).getRemainprice() - price);
                        mp_list.get(i).setStatus(1);
                        platoonOrderMapper.updateByPrimaryKeySelective(mp_list.get(i));
                        //提现更新
                        //MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(nowCashapply.getId());
                        nowCashapply.setMateprice(nowCashapply.getMateprice() + price);
                        nowCashapply.setRemainprice(nowCashapply.getRemainprice() - price);
                        cashapplyOrderMapper.updateByPrimaryKeySelective(nowCashapply);
                    } else if (mp_list.get(i).getRemainprice() > nowCashapply.getRemainprice()) {//排单池金额大
                        price = nowCashapply.getRemainprice();
                        //注入匹配记录
                        MMate mate = new MMate();
                        mate.setPrice(price);
                        mate.setStatus(0);
                        mate.setPlantoonuserid(mp_list.get(i).getUserid());
                        mate.setCashapplyuserid(nowCashapply.getUserid());
                        mate.setCreatedate(new Date());
                        mate.setType(0);
                        mate.setPlanToonId(mp_list.get(i).getId());
                        mate.setCashapplyId(nowCashapply.getId());
                        mateMapper.insertSelective(mate);
                        //注入工作域
                        mateService.overtimeJob(0,mp_list.get(i).getUserid(),mateMapper.selectOne(mate).getId());
                        //排单更新
                        //MPlatoonOrder mp = platoonOrderMapper.selectByPrimaryKey(mp_list.get(i).getId());
                        mp_list.get(i).setMateprice(mp_list.get(i).getMateprice() + price);
                        mp_list.get(i).setRemainprice(mp_list.get(i).getRemainprice() - price);
                        platoonOrderMapper.updateByPrimaryKeySelective(mp_list.get(i));
                        //提现更新
                        //MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(nowCashapply.getId());
                        nowCashapply.setStatus(1);
                        nowCashapply.setMateprice(nowCashapply.getMateprice() + price);
                        nowCashapply.setRemainprice(nowCashapply.getRemainprice() - price);
                        cashapplyOrderMapper.updateByPrimaryKeySelective(nowCashapply);
                        break;
                    } else {//两者相等
                        price = nowCashapply.getRemainprice();
                        //注入匹配记录
                        MMate mate = new MMate();
                        mate.setPrice(price);
                        mate.setStatus(0);
                        mate.setPlantoonuserid(mp_list.get(i).getUserid());
                        mate.setCashapplyuserid(nowCashapply.getUserid());
                        mate.setCreatedate(new Date());
                        mate.setType(0);
                        mate.setPlanToonId(mp_list.get(i).getId());
                        mate.setCashapplyId(nowCashapply.getId());
                        mateMapper.insertSelective(mate);
                        //注入工作域
                        mateService.overtimeJob(0,mp_list.get(i).getUserid(),mateMapper.selectOne(mate).getId());
                        //排单更新
                        //MPlatoonOrder mp = platoonOrderMapper.selectByPrimaryKey(mp_list.get(i).getId());
                        mp_list.get(i).setMateprice(mp_list.get(i).getMateprice() + price);
                        mp_list.get(i).setRemainprice(mp_list.get(i).getRemainprice() - price);
                        mp_list.get(i).setStatus(1);
                        platoonOrderMapper.updateByPrimaryKeySelective(mp_list.get(i));
                        //提现更新
                        //MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(nowCashapply.getId());
                        nowCashapply.setStatus(1);
                        nowCashapply.setMateprice(nowCashapply.getMateprice() + price);
                        nowCashapply.setRemainprice(nowCashapply.getRemainprice() - price);
                        cashapplyOrderMapper.updateByPrimaryKeySelective(nowCashapply);
                        break;
                    }
                }
                break;
        }
//        //排单队列
//        List<MMateToPlatoon> mp_list = MMateListener.getPlatoon();
//        //提现队列
//        List<MMateToCashapply> mc_list = MMateListener.getCashapply();
//        switch (status){
//            case 0://根据数值匹配
//                if (Common.isNull(num)){
//                    throw new BusinessException("common.parm.erro");
//                }
//                if (mp_list.size() == 0) {
//                    throw new BusinessException("common.mate.platoon.not");
//                }
//                if (mc_list.size() == 0) {
//                    throw new BusinessException("common.mate.caspply.not");
//                }
//                int forNum = mc_list.size();//默认循环对列数
//                //对列数大于指定数时，循环指定数
//                if (forNum > num) {
//                    forNum = num;
//                }
//                for (int i = 0; i < forNum;) {
//                    for (int j = 0; j < mc_list.size();) {
//                        //如果抢单和提现是同一个人  跳过此次匹配
//                        if (Common.isEqual(mp_list.get(i).getUserId(),mc_list.get(j).getUserId())){
//                            j++;
//                            continue;
//                        }
//                        if (mp_list.get(i).getPrice() < mc_list.get(j).getPrice()) {//提现池金额大
//                            Integer nowPrice = mc_list.get(j).getPrice() - mp_list.get(i).getPrice();
//                            //注入匹配记录
//                            MMate mate = new MMate();
//                            mate.setPrice(mp_list.get(i).getPrice());
//                            mate.setStatus(0);
//                            mate.setPlantoonuserid(mp_list.get(i).getUserId());
//                            mate.setCashapplyuserid(mc_list.get(j).getUserId());
//                            mate.setCreatedate(new Date());
//                            mate.setType(0);
//                            mate.setPlanToonId(mp_list.get(i).getId());
//                            mate.setCashapplyId(mc_list.get(j).getId());
//                            mateMapper.insertSelective(mate);
//                            //排单更新
//                            MPlatoonOrder mp = platoonOrderMapper.selectByPrimaryKey(mp_list.get(i).getId());
//                            mp.setMateprice(mp.getMateprice() + mp_list.get(i).getPrice());
//                            mp.setRemainprice(mp.getRemainprice() - mp_list.get(i).getPrice());
//                            mp.setStatus(1);
//                            platoonOrderMapper.updateByPrimaryKeySelective(mp);
//                            //提现更新
//                            MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(mc_list.get(j).getId());
//                            mc.setMateprice(mc.getMateprice() + mp_list.get(i).getPrice());
//                            mc.setRemainprice(mc.getRemainprice() - mp_list.get(i).getPrice());
//                            cashapplyOrderMapper.updateByPrimaryKeySelective(mc);
//                            //从匹配池中移除
//                            mp_list.remove(i);
//                            //重新添加到序列
//                            mc_list.get(j).setPrice(nowPrice);
//                            break;
//                        } else if (mp_list.get(i).getPrice() > mc_list.get(j).getPrice()) {//排单池金额大
//                            Integer nowPrice = mp_list.get(i).getPrice() - mc_list.get(j).getPrice();
//                            //注入匹配记录
//                            MMate mate = new MMate();
//                            mate.setPrice(mc_list.get(j).getPrice());
//                            mate.setStatus(0);
//                            mate.setPlantoonuserid(mp_list.get(i).getUserId());
//                            mate.setCashapplyuserid(mc_list.get(j).getUserId());
//                            mate.setCreatedate(new Date());
//                            mate.setType(0);
//                            mate.setPlanToonId(mp_list.get(i).getId());
//                            mate.setCashapplyId(mc_list.get(j).getId());
//                            mateMapper.insertSelective(mate);
//                            //排单更新
//                            MPlatoonOrder mp = platoonOrderMapper.selectByPrimaryKey(mp_list.get(i).getId());
//                            mp.setMateprice(mp.getMateprice() + mc_list.get(j).getPrice());
//                            mp.setRemainprice(mp.getRemainprice() - mc_list.get(j).getPrice());
//                            platoonOrderMapper.updateByPrimaryKeySelective(mp);
//                            //提现更新
//                            MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(mc_list.get(j).getId());
//                            mc.setStatus(1);
//                            mc.setMateprice(mc.getMateprice() + mc_list.get(j).getPrice());
//                            mc.setRemainprice(mc.getRemainprice() - mc_list.get(j).getPrice());
//                            cashapplyOrderMapper.updateByPrimaryKeySelective(mc);
//                            //从匹配池中移除
//                            mc_list.remove(j);
//                            //重新添加到序列
//                            mp_list.get(i).setPrice(nowPrice);
//                        } else {//两者相等
//                            //注入匹配记录
//                            MMate mate = new MMate();
//                            mate.setPrice(mc_list.get(j).getPrice());
//                            mate.setStatus(0);
//                            mate.setPlantoonuserid(mp_list.get(i).getUserId());
//                            mate.setCashapplyuserid(mc_list.get(j).getUserId());
//                            mate.setCreatedate(new Date());
//                            mate.setType(0);
//                            mate.setPlanToonId(mp_list.get(i).getId());
//                            mate.setCashapplyId(mc_list.get(j).getId());
//                            mateMapper.insertSelective(mate);
//                            //排单更新
//                            MPlatoonOrder mp = platoonOrderMapper.selectByPrimaryKey(mp_list.get(i).getId());
//                            mp.setStatus(1);
//                            mp.setMateprice(mp.getMateprice() + mp_list.get(i).getPrice());
//                            mp.setRemainprice(mp.getRemainprice() - mp_list.get(i).getPrice());
//                            platoonOrderMapper.updateByPrimaryKeySelective(mp);
//                            //提现更新
//                            MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(mc_list.get(j).getId());
//                            mc.setStatus(1);
//                            mc.setMateprice(mc.getMateprice() + mc_list.get(j).getPrice());
//                            mc.setRemainprice(mc.getRemainprice() - mc_list.get(j).getPrice());
//                            cashapplyOrderMapper.updateByPrimaryKeySelective(mc);
//                            //从匹配池中移除
//                            mp_list.remove(i);
//                            mc_list.remove(j);
//                            break;
//                        }
//                    }
//                }
//                break;
//            case 1://指定提现记录匹配
//                if (Common.isNull(cashapplyId)){
//                    throw new BusinessException("common.parm.erro");
//                }
//                //获取提现记录在队列中的下标
//                Integer cashapplyIndex = MMateListener.getCashapplyIndex(cashapplyId);
//                if (Common.isNull(cashapplyIndex)){
//                    throw new BusinessException("common.mate.cashapplyId.not");
//                }
//                MMateToCashapply nowCashapply = mc_list.get(cashapplyIndex);
//                for (int i = 0; i < mp_list.size();) {
//                    if (Common.isEqual(mp_list.get(i).getUserId(),nowCashapply.getUserId())) {
//                        i++;
//                        continue;
//                    }
//                    if (mp_list.get(i).getPrice() < nowCashapply.getPrice()) {//提现池金额大
//                        Integer nowPrice = nowCashapply.getPrice() - mp_list.get(i).getPrice();
//                        //注入匹配记录
//                        MMate mate = new MMate();
//                        mate.setPrice(mp_list.get(i).getPrice());
//                        mate.setStatus(0);
//                        mate.setPlantoonuserid(mp_list.get(i).getUserId());
//                        mate.setCashapplyuserid(nowCashapply.getUserId());
//                        mate.setCreatedate(new Date());
//                        mate.setType(0);
//                        mate.setPlanToonId(mp_list.get(i).getId());
//                        mate.setCashapplyId(nowCashapply.getId());
//                        mateMapper.insertSelective(mate);
//                        //排单更新
//                        MPlatoonOrder mp = platoonOrderMapper.selectByPrimaryKey(mp_list.get(i).getId());
//                        mp.setMateprice(mp.getMateprice() + mp_list.get(i).getPrice());
//                        mp.setRemainprice(mp.getRemainprice() - mp_list.get(i).getPrice());
//                        mp.setStatus(1);
//                        platoonOrderMapper.updateByPrimaryKeySelective(mp);
//                        //提现更新
//                        MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(nowCashapply.getId());
//                        mc.setMateprice(mc.getMateprice() + mp_list.get(i).getPrice());
//                        mc.setRemainprice(mc.getRemainprice() - mp_list.get(i).getPrice());
//                        cashapplyOrderMapper.updateByPrimaryKeySelective(mc);
//                        //从匹配池中移除
//                        mp_list.remove(i);
//                        //重新添加到序列
//                        nowCashapply.setPrice(nowPrice);
//                    } else if (mp_list.get(i).getPrice() > nowCashapply.getPrice()) {//排单池金额大
//                        Integer nowPrice = mp_list.get(i).getPrice() - nowCashapply.getPrice();
//                        //注入匹配记录
//                        MMate mate = new MMate();
//                        mate.setPrice(nowCashapply.getPrice());
//                        mate.setStatus(0);
//                        mate.setPlantoonuserid(mp_list.get(i).getUserId());
//                        mate.setCashapplyuserid(nowCashapply.getUserId());
//                        mate.setCreatedate(new Date());
//                        mate.setType(0);
//                        mate.setPlanToonId(mp_list.get(i).getId());
//                        mate.setCashapplyId(nowCashapply.getId());
//                        mateMapper.insertSelective(mate);
//                        //排单更新
//                        MPlatoonOrder mp = platoonOrderMapper.selectByPrimaryKey(mp_list.get(i).getId());
//                        mp.setMateprice(mp.getMateprice() + nowCashapply.getPrice());
//                        mp.setRemainprice(mp.getRemainprice() - nowCashapply.getPrice());
//                        platoonOrderMapper.updateByPrimaryKeySelective(mp);
//                        //提现更新
//                        MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(nowCashapply.getId());
//                        mc.setStatus(1);
//                        mc.setMateprice(mc.getMateprice() + nowCashapply.getPrice());
//                        mc.setRemainprice(mc.getRemainprice() - nowCashapply.getPrice());
//                        cashapplyOrderMapper.updateByPrimaryKeySelective(mc);
//                        //从匹配池中移除
//                        mc_list.remove(cashapplyIndex);
//                        //重新添加到序列
//                        mp_list.get(i).setPrice(nowPrice);
//                        break;
//                    } else {//两者相等
//                        //注入匹配记录
//                        MMate mate = new MMate();
//                        mate.setPrice(nowCashapply.getPrice());
//                        mate.setStatus(0);
//                        mate.setPlantoonuserid(mp_list.get(i).getUserId());
//                        mate.setCashapplyuserid(nowCashapply.getUserId());
//                        mate.setCreatedate(new Date());
//                        mate.setType(0);
//                        mate.setPlanToonId(mp_list.get(i).getId());
//                        mate.setCashapplyId(nowCashapply.getId());
//                        mateMapper.insertSelective(mate);
//                        //排单更新
//                        MPlatoonOrder mp = platoonOrderMapper.selectByPrimaryKey(mp_list.get(i).getId());
//                        mp.setMateprice(mp.getMateprice() + mp_list.get(i).getPrice());
//                        mp.setRemainprice(mp.getRemainprice() - mp_list.get(i).getPrice());
//                        mp.setStatus(1);
//                        platoonOrderMapper.updateByPrimaryKeySelective(mp);
//                        //提现更新
//                        MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(nowCashapply.getId());
//                        mc.setStatus(1);
//                        mc.setMateprice(mc.getMateprice() + nowCashapply.getPrice());
//                        mc.setRemainprice(mc.getRemainprice() - nowCashapply.getPrice());
//                        cashapplyOrderMapper.updateByPrimaryKeySelective(mc);
//                        //从匹配池中移除
//                        mp_list.remove(i);
//                        mc_list.remove(cashapplyIndex);
//                        break;
//                    }
//                }
//                break;
//        }
        return RestUtil.createResponse();
    }

    public synchronized JsonRestResponse userSeriesOperation(Integer userId, Integer status, HttpServletRequest request) {
        SUser user = userMapper.selectByPrimaryKey(userId);
        if (Common.isNull(user)){
            throw new BusinessException("common.userInfo.notExistent");
        }
        if (Common.isEqual(status,4)){
            accountMapper.updateAccountStatusToUserId(userId,1);
            cashapplyOrderMapper.updateCashapplyMold(userId,1);
        }else{
            accountMapper.updateAccountStatusToUserId(userId,0);
            cashapplyOrderMapper.updateCashapplyMold(userId,0);
        }
        user.setStatus(status);
        userMapper.updateByPrimaryKeySelective(user);
        return RestUtil.createResponse(SUserService.userInfoFilter(user));
    }

    public JsonRestResponse createSystemUserLockVote(Integer systemUserId, HttpServletRequest request) {
        SessionBean bean = Config.getSessionBeanToSystem(request);
        if (Common.isNull(bean)){
            throw new BusinessException("common.tokenInvalid");
        }
        TSystemUser user = systemUserMapper.selectByPrimaryKey(systemUserId);
        if (Common.isNull(user)){
            throw new BusinessException("common.userInfo.notExistent");
        }
        if (user.getIslock()!=0){
            throw new BusinessException("common.systemuser.vote.lock");
        }
        TSystemUserLockVote userLockVote = systemUserLockVoteMapper.selectUserLockVoteLockUserAndStatus(systemUserId);
        if (!Common.isNull(userLockVote)){
            throw new BusinessException("common.systemuser.vote.erro.voteIsTrue");
        }
        TSystemUserLockVote nowLockVote = systemUserLockVoteMapper.selectUserLockVoteLockStatus();
        if (!Common.isNull(nowLockVote)){
            throw new BusinessException("common.systemuser.vote.erro.now");
        }
        TSystemUserLockVote vote = new TSystemUserLockVote();
        vote.setCreateuserid(bean.getUid());
        vote.setLockuserid(systemUserId);
        vote.setNotagreeuserids(systemUserId.toString());
        vote.setPartakeuserids(Config.getSessionBeanToSystemAll(request,systemUserId));
        vote.setProcesseduserids(String.valueOf(bean.getUid()));
        vote.setAgreeuserids(String.valueOf(bean.getUid()));
        vote.setAgreenum(1);
        vote.setNotagreenum(0);
        vote.setStatus(0);
        vote.setCreatedate(new Date());
        vote.setBody("申请封锁系统用户：‘"+user.getRealname()+"’");
        systemUserLockVoteMapper.insertSelective(vote);
        return RestUtil.createResponse();
    }

    public synchronized JsonRestResponse launchRob(Integer price, HttpServletRequest request) {
        SessionBean bean = Config.getSessionBeanToSystem(request);
        if (Common.isNull(bean)){
            throw new BusinessException("common.tokenInvalid");
        }
        if (Common.isNull(price)){
            throw new BusinessException("common.rob.price.not");
        }
        MRob nowRob = robMapper.selectRobStatus(0);
        if (!Common.isNull(nowRob)){
            throw new BusinessException("common.rob.isExist");
        }
        MRob rob = new MRob();
        rob.setPrice(price);
        rob.setMateprice(0);
        rob.setRemainprice(price);
        rob.setSystemuserid(bean.getUid());
        rob.setStatus(0);
        rob.setBatchnum(Common.numberOnlyRoomId());
        rob.setCreatedate(new Date());
        robMapper.insertSelective(rob);
        RedisUtil.set(Config.robKey,JSON.toJSONString(robMapper.selectOne(rob)));
        return RestUtil.createResponse();
    }

    public JsonRestResponse selectRob(Integer startPage, Integer pageSize, HttpServletRequest request) {
        PageHelper.startPage(startPage, pageSize);
        List<Map<String, Object>> datas = robMapper.selectRobAllToMap();
        return RestUtil.createResponseData(new PageInfo(datas));
    }

    public JsonRestResponse selectRobOrder(String key, String batchNum, Integer startPage, Integer pageSize,
                                           HttpServletRequest request,Integer status) {
        PageHelper.startPage(startPage, pageSize);
        List<Map<String, Object>> datas = robOrderMapper.selectRobOrderAllMap(key,batchNum,status);
        return RestUtil.createResponseData(new PageInfo(datas));
    }

    public synchronized JsonRestResponse specifyRobOrderMate(Integer id, HttpServletRequest request) {
        MRobOrder robOrder = robOrderMapper.selectByPrimaryKey(id);
        if (robOrder.getStatus()==1){
            throw new BusinessException("common.erro");
        }
        //提现队列
        List<MCashapplyOrder> mc_list = cashapplyOrderMapper.selectCashapplyOrderNot();
        if (mc_list.size()==0){
            throw new BusinessException("common.mate.caspply.not");
        }
        Integer price = 0;//权重值
        for (int i = 0; i < mc_list.size();i++) {
            if (mc_list.get(i).getRemainprice()<=0){
                continue;
            }
            //如果抢单和提现是同一个人  跳过此次匹配
            if (Common.isEqual(mc_list.get(i).getUserid(),robOrder.getUserid())) {
                continue;
            }
            if (robOrder.getRemainprice()>mc_list.get(i).getRemainprice()){//匹配金额大于当前提现金额
                price = mc_list.get(i).getRemainprice();
                //匹配记录
                MMate mate = new MMate();
                mate.setPrice(price);
                mate.setStatus(0);
                mate.setPlantoonuserid(robOrder.getUserid());
                mate.setCashapplyuserid(mc_list.get(i).getUserid());
                mate.setCreatedate(new Date());
                mate.setType(1);
                mate.setPlanToonId(robOrder.getId());
                mate.setCashapplyId(mc_list.get(i).getId());
                mateMapper.insertSelective(mate);
                //注入工作域
                mateService.overtimeJob(0,robOrder.getUserid(),mateMapper.selectOne(mate).getId());
                //抢单更新
                robOrder.setRemainprice(robOrder.getRemainprice()-price);
                robOrder.setMateprice(robOrder.getMateprice()+price);
                robOrderMapper.updateByPrimaryKeySelective(robOrder);
                //提现更新
                //MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(mc_list.get(i).getId());
                mc_list.get(i).setStatus(1);
                mc_list.get(i).setMateprice(mc_list.get(i).getMateprice() + price);
                mc_list.get(i).setRemainprice(mc_list.get(i).getRemainprice() - price);
                cashapplyOrderMapper.updateByPrimaryKeySelective(mc_list.get(i));
            }else if(robOrder.getRemainprice()<=mc_list.get(i).getRemainprice()){
                price = robOrder.getRemainprice();
                //匹配记录
                MMate mate = new MMate();
                mate.setPrice(price);
                mate.setStatus(0);
                mate.setPlantoonuserid(robOrder.getUserid());
                mate.setCashapplyuserid(mc_list.get(i).getUserid());
                mate.setCreatedate(new Date());
                mate.setType(1);
                mate.setPlanToonId(robOrder.getId());
                mate.setCashapplyId(mc_list.get(i).getId());
                mateMapper.insertSelective(mate);
                //注入工作域
                mateService.overtimeJob(0,robOrder.getUserid(),mateMapper.selectOne(mate).getId());
                //抢单更新_在这里直接匹配完
                robOrder.setRemainprice(0);
                robOrder.setMateprice(robOrder.getPrice());
                robOrder.setStatus(1);
                robOrderMapper.updateByPrimaryKeySelective(robOrder);
                //提现更新
                //MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(mc_list.get(i).getId());
                mc_list.get(i).setMateprice(mc_list.get(i).getMateprice() + price);
                mc_list.get(i).setRemainprice(mc_list.get(i).getRemainprice() - price);
                //更新提现池数据
                if (robOrder.getRemainprice()==mc_list.get(i).getRemainprice()){
                    mc_list.get(i).setStatus(1);
                }
                cashapplyOrderMapper.updateByPrimaryKeySelective(mc_list.get(i));
                break;
            }
        }
//        //提现队列
//        List<MMateToCashapply> mc_list = MMateListener.getCashapply();
//        if (mc_list.size()==0){
//            throw new BusinessException("common.mate.caspply.not");
//        }
//        for (int i = 0; i < mc_list.size();) {
//            //如果抢单和提现是同一个人  跳过此次匹配
//            if (Common.isEqual(mc_list.get(i).getUserId(),robOrder.getUserid())) {
//                i++;continue;
//            }
//            if (robOrder.getRemainprice()>mc_list.get(i).getPrice()){//匹配金额大于当前提现金额
//                //匹配记录
//                MMate mate = new MMate();
//                mate.setPrice(mc_list.get(i).getPrice());
//                mate.setStatus(0);
//                mate.setPlantoonuserid(robOrder.getUserid());
//                mate.setCashapplyuserid(mc_list.get(i).getUserId());
//                mate.setCreatedate(new Date());
//                mate.setType(1);
//                mate.setPlanToonId(robOrder.getId());
//                mate.setCashapplyId(mc_list.get(i).getId());
//                mateMapper.insertSelective(mate);
//                //抢单更新
//                robOrder.setRemainprice(robOrder.getRemainprice()-mc_list.get(i).getPrice());
//                robOrder.setMateprice(robOrder.getMateprice()+mc_list.get(i).getPrice());
//                robOrderMapper.updateByPrimaryKeySelective(robOrder);
//                //提现更新
//                MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(mc_list.get(i).getId());
//                mc.setStatus(1);
//                mc.setMateprice(mc.getMateprice() + mc_list.get(i).getPrice());
//                mc.setRemainprice(mc.getRemainprice() - mc_list.get(i).getPrice());
//                cashapplyOrderMapper.updateByPrimaryKeySelective(mc);
//                //从提现池中删除
//                mc_list.remove(i);
//            }else if(robOrder.getRemainprice()<=mc_list.get(i).getPrice()){
//                //匹配记录
//                MMate mate = new MMate();
//                mate.setPrice(robOrder.getRemainprice());
//                mate.setStatus(0);
//                mate.setPlantoonuserid(robOrder.getUserid());
//                mate.setCashapplyuserid(mc_list.get(i).getUserId());
//                mate.setCreatedate(new Date());
//                mate.setType(1);
//                mate.setPlanToonId(robOrder.getId());
//                mate.setCashapplyId(mc_list.get(i).getId());
//                mateMapper.insertSelective(mate);
//                //抢单更新_在这里直接匹配完
//                robOrder.setRemainprice(0);
//                robOrder.setMateprice(robOrder.getPrice());
//                robOrder.setStatus(1);
//                robOrderMapper.updateByPrimaryKeySelective(robOrder);
//                //提现更新
//                MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(mc_list.get(i).getId());
//                mc.setMateprice(mc.getMateprice() + robOrder.getRemainprice());
//                mc.setRemainprice(mc.getRemainprice() - robOrder.getRemainprice());
//                //更新提现池数据
//                if (robOrder.getRemainprice()<mc_list.get(i).getPrice()){
//                    mc_list.get(i).setPrice(mc_list.get(i).getPrice()-robOrder.getRemainprice());
//                }else {
//                    mc.setStatus(1);
//                    //从提现池中删除
//                    mc_list.remove(i);
//                }
//                cashapplyOrderMapper.updateByPrimaryKeySelective(mc);
//                break;
//            }
//        }
        return RestUtil.createResponse();
    }

    public JsonRestResponse selectSystemUserAll(HttpServletRequest request) {
        return RestUtil.createResponse(systemUserMapper.selectSystemUserAll());
    }

    public JsonRestResponse selectSystemUserLockVote(HttpServletRequest request,Integer startPage,Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        List<Map<String, Object>> datas = systemUserLockVoteMapper.selectSystemUserLockVote(String.valueOf(Config.getSessionBeanToSystem(request).getUid()));
        return RestUtil.createResponseData(new PageInfo(datas));
    }

    public JsonRestResponse systemUserVote(HttpServletRequest request, Integer status) {
        SessionBean bean = Config.getSessionBeanToSystem(request);
        if (Common.isNull(status)){
            throw new BusinessException("common.parm.erro");
        }
        TSystemUserLockVote lockVote = systemUserLockVoteMapper.selectUserLockVoteLockStatus();
        if (Common.isNull(lockVote)){
            throw new BusinessException("common.lock.over");
        }
        if (lockVote.getPartakeuserids().indexOf(bean.getUid())==-1){
            throw new BusinessException("common.lock.user.notPower");
        }
        if (lockVote.getProcesseduserids().indexOf(bean.getUid())==-1){
            throw new BusinessException("common.lock.user.isHandle");
        }
        lockVote.setProcesseduserids(lockVote.getProcesseduserids().concat(","+bean.getUid()));
        switch (status){
            case 0://同意
                lockVote.setAgreeuserids(lockVote.getAgreeuserids().concat(","+bean.getUid()));
                lockVote.setAgreenum(lockVote.getAgreenum()+1);
                break;
            case 1://不同意
                lockVote.setNotagreeuserids(lockVote.getNotagreeuserids().concat(","+bean.getUid()));
                lockVote.setNotagreenum(lockVote.getNotagreenum()+1);
                break;
        }
        if (lockVote.getProcesseduserids().split(",").length==4){
            lockVote.setStatus(1);
        }
        systemUserLockVoteMapper.updateByPrimaryKeySelective(lockVote);
        return RestUtil.createResponse(lockVote);
    }

    @Transactional
    public JsonRestResponse updateUserInfo(Integer userId,HttpServletRequest request, String userNickName, String twopassword,
                                           String alipayAccount, String wxpayAccount, String bankAccount,
                                           String realName,String bankName,Integer mold,Integer isDelete,
                                           String password,String userAccount,Integer refereeId) {
        SUser user = userMapper.selectByPrimaryKey(userId);
        if (Common.isNull(user)){
            throw new BusinessException("common.parm.erro");
        }
//        if (!Common.isNull(userNickName)){
//            SUser su = userMapper.selectUserNickName(userNickName);
//            if (!Common.isNull(su)){
//                if (su.getId()!=userId)throw new BusinessException("common.userInfo.nickName.exist");
//            }
//        }

        if (!Common.isNull(userNickName)||!Common.isNull(twopassword)||
                !Common.isNull(realName)||!Common.isNull(mold)||!Common.isNull(isDelete)
                ||!Common.isNull(password)||!Common.isNull(userAccount)||!Common.isNull(refereeId)){
            user.setUsernickname(userNickName);
            user.setTwopassword(Common.personEncrypt(twopassword));
            user.setRealName(realName);
            user.setMold(mold);
            user.setIsdelete(isDelete);
            user.setPassword(Common.personEncrypt(password));
            user.setUseraccount(userAccount);
            if (!Common.isNull(refereeId)){
                if (!Common.isEqual(refereeId,user.getRefereeId())){
                    if (!Common.isNull(user.getRefereeId())){
                        SAccount account = accountMapper.selectAccountUserId(user.getRefereeId());
                        if (account.getRefereeNum()>0){
                            account.setRefereeNum(account.getRefereeNum()-1);
                            accountMapper.updateByPrimaryKeySelective(account);
                        }
                    }
                    SAccount a2 = accountMapper.selectAccountUserId(refereeId);
                    a2.setRefereeNum(a2.getRefereeNum()+1);
                    accountMapper.updateByPrimaryKeySelective(a2);
                }
            }

            user.setRefereeId(refereeId);
            userMapper.updateByPrimaryKeySelective(user);
        }
        if (!Common.isNull(alipayAccount)||!Common.isNull(wxpayAccount)||!Common.isNull(bankAccount)||!Common.isNull(bankName)){
            SAccount account = accountMapper.selectAccountUserId(userId);
            if (!userService.duplicationOfData(account)){
                throw new BusinessException("common.duplication.account");
            }
            account.setAlipayaccount(alipayAccount);
            account.setWxpayaccount(wxpayAccount);
            account.setBankaccount(bankAccount);
            account.setBankName(bankName);
            accountMapper.updateByPrimaryKeySelective(account);
        }
        return RestUtil.createResponse();
    }

    public JsonRestResponse selectMateRecordToAll(String key, String startDate, String endDate, Integer startPage,
                                                  Integer pageSize, HttpServletRequest request, Integer status,
                                                  Integer mold) {
        PageHelper.startPage(startPage, pageSize);
        List<Map<String,Object>> datas = mateMapper.selectMateRecordToAll(key,CommonDate.StringTodateYMD(startDate),
                CommonDate.StringTodateYMD(endDate),status,mold);
        return RestUtil.createResponseData(new PageInfo(datas));
    }

    public synchronized JsonRestResponse rechargeIntegral(Integer userId, Integer integral, HttpServletRequest request,
                                                          Integer status) {
        SessionBean bean = Config.getSessionBeanToSystem(request);
        if (Common.isNull(bean)){
            throw new BusinessException("common.tokenInvalid");
        }
        if (integral.intValue()<0){
            throw new BusinessException("common.number.erro");
        }
        SUser user = userMapper.selectByPrimaryKey(userId);
        if (Common.isNull(user)){
            throw new BusinessException("common.userInfo.notExistent");
        }

        SAccount account = accountMapper.selectAccountUserId(userId);
        switch (status){
            case 0://增加
                account.setIntegral(account.getIntegral()+integral);
                break;
            case 1://减少
                if (integral>account.getIntegral()){
                    RestUtil.createResponseErro("最多只能减少"+account.getIntegral()+"积分");
                }
                account.setIntegral(account.getIntegral()-integral);
                break;
        }
        accountMapper.updateByPrimaryKeySelective(account);
        TUserIntegralRecord record = new TUserIntegralRecord();
        record.setIntergral(integral);
        record.setSystemuserid(bean.getUid());
        record.setUserid(userId);
        record.setCreatedate(new Date());
        record.setStatus(status);
        userIntegralRecordMapper.insertSelective(record);
        return RestUtil.createResponse();
    }

    public JsonRestResponse queryUserRechargeIntegralRecord(Integer userId, Integer systenUserId, String key,
                                                            Integer startPage, Integer pageSize,String startDate,
                                                            String endDate,Integer status) {
        PageHelper.startPage(startPage, pageSize);
        List<Map<String,Object>> datas = userIntegralRecordMapper.queryUserRechargeIntegralRecord(userId,systenUserId,
                key,CommonDate.StringTodateYMD(startDate),CommonDate.StringTodateYMD(endDate),status);
        return RestUtil.createResponseData(new PageInfo(datas),userIntegralRecordMapper.queryUserRechargeIntegralRecordBigDecimal(userId,systenUserId,
                key,CommonDate.StringTodateYMD(startDate),CommonDate.StringTodateYMD(endDate),status));
    }

    public JsonRestResponse statisticsHomeData(String startDate, String endDate) {
        Map<String,Object> homeData = new LinkedHashMap<>();
        Map<String,Object> platoonPrice = platoonOrderMapper.selectSumPlatoonPriceToStatus(CommonDate.StringToDate(startDate),
                CommonDate.StringToDate(endDate));
        homeData.put("zjqk",mateMapper.selectHomeZJS(CommonDate.StringToDate(startDate),CommonDate.StringToDate(endDate)));
        homeData.put("pdzjs",platoonPrice.get("price"));
        homeData.put("yppdjes",platoonPrice.get("matePrice"));
        homeData.put("wjczjs",platoonPrice.get("remainPrice"));
        homeData.put("ccjes",mateMapper.selectHomeCSZJS(CommonDate.StringToDate(startDate),CommonDate.StringToDate(endDate)).get("remainPrice"));
        homeData.put("yccjes",platoonPrice.get("cashapplyPrice"));
        homeData.put("registerNum",mateMapper.selectHomeZCRS(CommonDate.StringToDate(startDate),CommonDate.StringToDate(endDate)));
        homeData.put("cczjes",mateMapper.selectHomeCCZJES());
        homeData.put("pdzjes",mateMapper.selectHomePDZJES());
        homeData.put("plantUserNum",platoonOrderMapper.selectPlantUserNum());
        homeData.put("plantLiveNum",platoonOrderMapper.selectPlantUserLiveNum());
        homeData.put("plantSonNum",platoonOrderMapper.selectPlantUserSonNum());
        return RestUtil.createResponse(homeData);
    }

    public JsonRestResponse deletePlatoon(Integer platoonId) {
        MPlatoonOrder platoonOrder = platoonOrderMapper.selectByPrimaryKey(platoonId);
        if (Common.isNull(platoonOrder)){
            throw new BusinessException("common.parm.erro");
        }
        List<MMate> list = mateMapper.selectMateToUser(platoonId,null,null,null,null,0);
        if (list.size()>0){
            throw new BusinessException("common.deletemate.erro");
        }
        platoonOrderMapper.deleteByPrimaryKey(platoonId);
        return RestUtil.createResponse();
    }

    public JsonRestResponse deleteCashpply(Integer cashapplyId) {
        MCashapplyOrder cashapplyOrder = cashapplyOrderMapper.selectByPrimaryKey(cashapplyId);
        if (Common.isNull(cashapplyOrder)){
            throw new BusinessException("common.parm.erro");
        }
        List<MMate> list = mateMapper.selectMateToUser(null,cashapplyId,null,null,null,null);
        if (list.size()>0){
            throw new BusinessException("common.deletemate.erro");
        }
        cashapplyOrderMapper.deleteByPrimaryKey(cashapplyId);
        return RestUtil.createResponse();
    }

    public JsonRestResponse overtimeRecordOperation(Integer mateId,Integer type, HttpServletRequest request) {
        MMate mate = mateMapper.selectByPrimaryKey(mateId);
        if (Common.isNull(mate)){
            throw new BusinessException("common.parm.erro");
        }
        switch (type){
            case 0://删除匹配
                if (!Common.isEqual(mate.getMold(),1)){
                    throw new BusinessException("common.erro");
                }
                //删除排单
                platoonOrderMapper.deleteByPrimaryKey(mate.getPlanToonId());
                //提现打回匹配池
                MCashapplyOrder cashapplyOrder = cashapplyOrderMapper.selectByPrimaryKey(mate.getCashapplyId());
                cashapplyOrder.setStatus(0);
                cashapplyOrder.setMateprice(cashapplyOrder.getMateprice()-mate.getPrice());
                cashapplyOrder.setRemainprice(cashapplyOrder.getRemainprice()+mate.getPrice());
                cashapplyOrderMapper.updateByPrimaryKeySelective(cashapplyOrder);
                //用户执行二次冻结，以防工作域没生效
                SUser planToonUser = userMapper.selectByPrimaryKey(mate.getPlantoonuserid());
                planToonUser.setStatus(2);
                userMapper.updateByPrimaryKeySelective(planToonUser);
                //删除匹配
                mateMapper.delete(mate);
                break;
            case 1://强制确认收款
                if (!Common.isEqual(mate.getMold(),2)){
                    throw new BusinessException("common.erro");
                }
                mate.setMold(0);
                mate.setStatus(2);
                mate.setConfirmPriceDate(new Date());
                mateMapper.updateByPrimaryKeySelective(mate);
                //用户执行二次封号，以防工作域没生效
                SUser cashapplyUser = userMapper.selectByPrimaryKey(mate.getCashapplyuserid());
                cashapplyUser.setStatus(3);
                userMapper.updateByPrimaryKeySelective(cashapplyUser);
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
                break;
        }
        return RestUtil.createResponse();
    }

    public synchronized JsonRestResponse specifiedMatching(Integer planToonId, Integer cashapplyId,
                                                           Integer status,HttpServletRequest request) {
        SessionBean bean = Config.getSessionBeanToSystem(request);
        if (Common.isNull(bean)){
            throw new BusinessException("common.tokenInvalid");
        }
        if (!Common.isEqual(status,0)&&!Common.isEqual(status,1)){
            throw new BusinessException("common.erro");
        }

        MCashapplyOrder cashapplyOrder = cashapplyOrderMapper.selectByPrimaryKey(cashapplyId);
        if (Common.isNull(cashapplyOrder)){
            throw new BusinessException("common.parm.erro");
        }
        if (!Common.isEqual(cashapplyOrder.getStatus(),0)){
            throw new BusinessException("common.mate.cashapplyId.not");
        }
        if (Common.isEqual(cashapplyOrder.getMold(),1)){
            throw new BusinessException("common.cashapply.mold.erro");
        }
        switch (status){
            case 0://排单与提现匹配的
                MPlatoonOrder platoonOrder = platoonOrderMapper.selectByPrimaryKey(planToonId);
                if (Common.isNull(platoonOrder)){
                    throw new BusinessException("common.parm.erro");
                }
                if (!Common.isEqual(platoonOrder.getStatus(),0)){
                    throw new BusinessException("common.mate.plantoon.not");
                }
                if (Common.isEqual(platoonOrder.getUserid(),cashapplyOrder.getUserid())){
                    throw new BusinessException("common.mate.cashapply.platoon.istrue");
                }
                Integer price = 0;
                if (platoonOrder.getRemainprice() < cashapplyOrder.getRemainprice()) {//提现池金额大
                    price = platoonOrder.getRemainprice();
                    //注入匹配记录
                    MMate mate = new MMate();
                    mate.setPrice(price);
                    mate.setStatus(0);
                    mate.setPlantoonuserid(platoonOrder.getUserid());
                    mate.setCashapplyuserid(cashapplyOrder.getUserid());
                    mate.setCreatedate(new Date());
                    mate.setType(0);
                    mate.setPlanToonId(platoonOrder.getId());
                    mate.setCashapplyId(cashapplyOrder.getId());
                    mateMapper.insertSelective(mate);
                    //注入工作域
                    mateService.overtimeJob(0,platoonOrder.getUserid(),mateMapper.selectOne(mate).getId());
                    //排单更新
                    platoonOrder.setMateprice(platoonOrder.getMateprice() + price);
                    platoonOrder.setRemainprice(platoonOrder.getRemainprice() - price);
                    platoonOrder.setStatus(1);
                    platoonOrderMapper.updateByPrimaryKeySelective(platoonOrder);
                    //提现更新
                    cashapplyOrder.setMateprice(cashapplyOrder.getMateprice() + price);
                    cashapplyOrder.setRemainprice(cashapplyOrder.getRemainprice() - price);
                    cashapplyOrderMapper.updateByPrimaryKeySelective(cashapplyOrder);
                } else if (platoonOrder.getRemainprice() > cashapplyOrder.getRemainprice()) {//排单池金额大
                    price = cashapplyOrder.getRemainprice();
                    //注入匹配记录
                    MMate mate = new MMate();
                    mate.setPrice(price);
                    mate.setStatus(0);
                    mate.setPlantoonuserid(platoonOrder.getUserid());
                    mate.setCashapplyuserid(cashapplyOrder.getUserid());
                    mate.setCreatedate(new Date());
                    mate.setType(0);
                    mate.setPlanToonId(platoonOrder.getId());
                    mate.setCashapplyId(cashapplyOrder.getId());
                    mateMapper.insertSelective(mate);
                    //注入工作域
                    mateService.overtimeJob(0,platoonOrder.getUserid(),mateMapper.selectOne(mate).getId());
                    //排单更新
                    platoonOrder.setMateprice(platoonOrder.getMateprice() + price);
                    platoonOrder.setRemainprice(platoonOrder.getRemainprice() - price);
                    platoonOrderMapper.updateByPrimaryKeySelective(platoonOrder);
                    //提现更新
                    cashapplyOrder.setStatus(1);
                    cashapplyOrder.setMateprice(cashapplyOrder.getMateprice() + price);
                    cashapplyOrder.setRemainprice(cashapplyOrder.getRemainprice() - price);
                    cashapplyOrderMapper.updateByPrimaryKeySelective(cashapplyOrder);
                } else {//两者相等
                    price = cashapplyOrder.getRemainprice();
                    //注入匹配记录
                    MMate mate = new MMate();
                    mate.setPrice(price);
                    mate.setStatus(0);
                    mate.setPlantoonuserid(platoonOrder.getUserid());
                    mate.setCashapplyuserid(cashapplyOrder.getUserid());
                    mate.setCreatedate(new Date());
                    mate.setType(0);
                    mate.setPlanToonId(platoonOrder.getId());
                    mate.setCashapplyId(cashapplyOrder.getId());
                    mateMapper.insertSelective(mate);
                    //注入工作域
                    mateService.overtimeJob(0,platoonOrder.getUserid(),mateMapper.selectOne(mate).getId());
                    //排单更新
                    platoonOrder.setStatus(1);
                    platoonOrder.setMateprice(platoonOrder.getMateprice() + price);
                    platoonOrder.setRemainprice(platoonOrder.getRemainprice() - price);
                    platoonOrderMapper.updateByPrimaryKeySelective(platoonOrder);
                    //提现更新
                    cashapplyOrder.setStatus(1);
                    cashapplyOrder.setMateprice(cashapplyOrder.getMateprice() + price);
                    cashapplyOrder.setRemainprice(cashapplyOrder.getRemainprice() - price);
                    cashapplyOrderMapper.updateByPrimaryKeySelective(cashapplyOrder);
                }
                break;
            case 1://抢单与提现匹配的
                MRobOrder robOrder = robOrderMapper.selectByPrimaryKey(planToonId);
                if (Common.isNull(robOrder)){
                    throw new BusinessException("common.parm.erro");
                }
                if (!Common.isEqual(robOrder.getStatus(),0)){
                    throw new BusinessException("common.mate.roborder.not");
                }
                if (Common.isEqual(robOrder.getUserid(),cashapplyOrder.getUserid())){
                    throw new BusinessException("common.mate.cashapply.roborder.istrue");
                }
                if (robOrder.getRemainprice()>cashapplyOrder.getRemainprice()){//匹配金额大于当前提现金额
                    price = cashapplyOrder.getRemainprice();
                    //匹配记录
                    MMate mate = new MMate();
                    mate.setPrice(price);
                    mate.setStatus(0);
                    mate.setPlantoonuserid(robOrder.getUserid());
                    mate.setCashapplyuserid(cashapplyOrder.getUserid());
                    mate.setCreatedate(new Date());
                    mate.setType(1);
                    mate.setPlanToonId(robOrder.getId());
                    mate.setCashapplyId(cashapplyOrder.getId());
                    mateMapper.insertSelective(mate);
                    //注入工作域
                    mateService.overtimeJob(0,robOrder.getUserid(),mateMapper.selectOne(mate).getId());
                    //抢单更新
                    robOrder.setRemainprice(robOrder.getRemainprice()-price);
                    robOrder.setMateprice(robOrder.getMateprice()+price);
                    robOrderMapper.updateByPrimaryKeySelective(robOrder);
                    //提现更新
                    cashapplyOrder.setStatus(1);
                    cashapplyOrder.setMateprice(cashapplyOrder.getMateprice() + price);
                    cashapplyOrder.setRemainprice(cashapplyOrder.getRemainprice() - price);
                    cashapplyOrderMapper.updateByPrimaryKeySelective(cashapplyOrder);
                }else if(robOrder.getRemainprice()<=cashapplyOrder.getRemainprice()){
                    price = robOrder.getRemainprice();
                    //匹配记录
                    MMate mate = new MMate();
                    mate.setPrice(price);
                    mate.setStatus(0);
                    mate.setPlantoonuserid(robOrder.getUserid());
                    mate.setCashapplyuserid(cashapplyOrder.getUserid());
                    mate.setCreatedate(new Date());
                    mate.setType(1);
                    mate.setPlanToonId(robOrder.getId());
                    mate.setCashapplyId(cashapplyOrder.getId());
                    mateMapper.insertSelective(mate);
                    //注入工作域
                    mateService.overtimeJob(0,robOrder.getUserid(),mateMapper.selectOne(mate).getId());
                    //抢单更新_在这里直接匹配完
                    robOrder.setRemainprice(0);
                    robOrder.setMateprice(robOrder.getPrice());
                    robOrder.setStatus(1);
                    robOrderMapper.updateByPrimaryKeySelective(robOrder);
                    //提现更新
                    //MCashapplyOrder mc = cashapplyOrderMapper.selectByPrimaryKey(mc_list.get(i).getId());
                    cashapplyOrder.setMateprice(cashapplyOrder.getMateprice() + price);
                    cashapplyOrder.setRemainprice(cashapplyOrder.getRemainprice() - price);
                    //更新提现池数据
                    if (Common.isEqual(robOrder.getRemainprice(),cashapplyOrder.getRemainprice())){
                        cashapplyOrder.setStatus(1);
                    }
                    cashapplyOrderMapper.updateByPrimaryKeySelective(cashapplyOrder);
                }
                break;
        }
        return RestUtil.createResponse();
    }

    public JsonRestResponse selectSystemUserIntegralNum(String phone, String startDate, String endDate,
                                                        Integer startPage,Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        List<Map<String,Object>> datas = accountRecordMapper.selectSystemUserIntegralRecord(Config.khid(phone),
                CommonDate.StringTodateYMD(startDate),CommonDate.StringTodateYMD(endDate));
        return RestUtil.createResponseData(new PageInfo(datas),accountRecordMapper.selectSystemUserIntegralNum(Config.khid(phone),
                CommonDate.StringTodateYMD(startDate),CommonDate.StringTodateYMD(endDate)));
    }

    public synchronized JsonRestResponse platformReceipt(Integer mateId, Integer userId, HttpServletRequest request) {
        MMate mate = mateMapper.selectByPrimaryKey(mateId);
        if (Common.isNull(mate)){
            throw new BusinessException("common.parm.erro");
        }
        if (!Common.isEqual(mate.getMold(),1)){
            throw new BusinessException("common.erro");
        }
        SUser user = userMapper.selectByPrimaryKey(userId);
        if (Common.isNull(user)){
            throw new BusinessException("common.parm.erro");
        }
        if (Common.isEqual(mate.getPlantoonuserid(),userId)){
            throw new BusinessException("common.receipt.erro");
        }
        if (Common.isEqual(mate.getCashapplyuserid(),userId)){
            throw new BusinessException("common.receipt.cashapply.erro");
        }

        //获取扣除积分比例
        SDict intgralRatio = dictMapper.selectDict(Config.DICT_INTGRAL_RATION);
        if (intgralRatio.getRealvalue()<0 && intgralRatio.getRealvalue()>1){
            throw new BusinessException("common.dict.erro");
        }
        //扣除积分值
        Double intgralDouble = mate.getPrice()*intgralRatio.getRealvalue();
        Integer intgral = intgralDouble.intValue();
        if (intgral>0){
            SAccount account = accountMapper.selectAccountUserId(userId);
            if (account.getIntegral()<intgral){
                throw new BusinessException("common.intgral.not");
            }
            accountMapper.updateAccountIntgralToReduce(userId,intgral);
            accountRecordService.saveAccountRecord(Config.platoonIntgral,intgral,account.getIntegral()-intgral,
                    0,1,1,null,userId,account.getId());
        }
        //删除原单
        platoonOrderMapper.deleteByPrimaryKey(mate.getPlanToonId());
        //用户执行二次冻结，以防工作域没生效
        SUser planToonUser = userMapper.selectByPrimaryKey(mate.getPlantoonuserid());
        planToonUser.setStatus(2);
        userMapper.updateByPrimaryKeySelective(planToonUser);
        //产生新单
        MPlatoonOrder po = new MPlatoonOrder();
        po.setPrice(mate.getPrice());
        po.setMateprice(mate.getPrice());
        po.setRemainprice(0);
        po.setUserid(userId);
        po.setStatus(1);
        po.setCreatedate(new Date());
        platoonOrderMapper.insertSelective(po);
        //接单指定匹配
        mate.setPlanToonId(platoonOrderMapper.selectOne(po).getId());
        mate.setPlantoonuserid(userId);
        mate.setMold(0);
        mate.setType(0);
        mateMapper.updateByPrimaryKeySelective(mate);
        MSmsCodeService.sendMsg(userMapper.selectByPrimaryKey(mate.getPlantoonuserid()).getUseraccount(),0);
        MSmsCodeService.sendMsg(userMapper.selectByPrimaryKey(mate.getCashapplyuserid()).getUseraccount(),1);
        return RestUtil.createResponse();
    }

    public synchronized JsonRestResponse distributeOrder(Integer oid, Integer status, String phone, HttpServletRequest request) {
        Integer uid = Config.pdid(phone);
        if (Common.isNull(uid)){
            throw new BusinessException("common.erro");
        }
        Integer price = -1;
        Integer pid = -1;
        Integer puserId = -1;
        switch (status){
            case 0://排单
                MPlatoonOrder platoonOrder = platoonOrderMapper.selectByPrimaryKey(oid);
                if (Common.isNull(platoonOrder)){
                    throw new BusinessException("common.parm.erro");
                }
                if (!Common.isEqual(platoonOrder.getStatus(),0)){
                    throw new BusinessException("common.mate.plantoon.not");
                }
                price = platoonOrder.getRemainprice();
                pid = platoonOrder.getId();
                puserId = platoonOrder.getUserid();
                platoonOrder.setStatus(1);
                platoonOrder.setMateprice(platoonOrder.getMateprice()+price);
                platoonOrder.setRemainprice(platoonOrder.getRemainprice()-price);
                platoonOrderMapper.updateByPrimaryKeySelective(platoonOrder);

                break;
            case 1://抢单
                MRobOrder robOrder = robOrderMapper.selectByPrimaryKey(oid);
                if (Common.isNull(robOrder)){
                    throw new BusinessException("common.parm.erro");
                }
                if (!Common.isEqual(robOrder.getStatus(),0)){
                    throw new BusinessException("common.mate.roborder.not");
                }
                price = robOrder.getRemainprice();
                pid = robOrder.getId();
                puserId = robOrder.getUserid();
                robOrder.setStatus(1);
                robOrder.setMateprice(robOrder.getMateprice()+price);
                robOrder.setRemainprice(robOrder.getRemainprice()-price);
                robOrderMapper.updateByPrimaryKeySelective(robOrder);
                break;
        }
        MCashapplyOrder cashapplyOrder = new MCashapplyOrder();
        cashapplyOrder.setPrice(price);
        cashapplyOrder.setMateprice(0);
        cashapplyOrder.setRemainprice(price);
        cashapplyOrder.setStatus(1);
        cashapplyOrder.setCreatedate(new Date());
        cashapplyOrder.setUserid(uid);
        cashapplyOrderMapper.insertSelective(cashapplyOrder);
        MMate mate = new MMate();
        mate.setPrice(price);
        mate.setMold(0);
        mate.setStatus(0);
        mate.setPlanToonId(pid);
        mate.setPlantoonuserid(puserId);
        mate.setCashapplyId(cashapplyOrderMapper.selectOne(cashapplyOrder).getId());
        mate.setCashapplyuserid(uid);
        mate.setType(status);
        mate.setCreatedate(new Date());
        mateMapper.insertSelective(mate);
        //短信 通知
        MSmsCodeService.sendMsg(userMapper.selectByPrimaryKey(mate.getPlantoonuserid()).getUseraccount(),0);
        MSmsCodeService.sendMsg(userMapper.selectByPrimaryKey(mate.getCashapplyuserid()).getUseraccount(),1);
        return RestUtil.createResponse();
    }

    public JsonRestResponse selectDistributeOrderUser() {
        return RestUtil.createResponse(userMapper.selectDistributeOrderUser());
    }

    public JsonRestResponse selectCahsapplyOverPlanToonDate(String key, String startDate, String endDate,
                                                            Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        List<Map<String,Object>> datas = cashapplyOrderMapper.selectCahsapplyOverPlanToonDate(key,CommonDate.StringTodateYMD(startDate),
                CommonDate.StringTodateYMD(endDate));
        PageInfo<Map<String, Object>> p = new PageInfo<>(datas);
        return RestUtil.createResponseData(p);
    }

    public JsonRestResponse selectWithdrawing(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        List<Map<String,Object>> datas = userMapper.selectWithdrawing();
        return RestUtil.createResponseData(new PageInfo<>(datas));
    }

    public JsonRestResponse selectWithdrawingUser(String userIds, String userIdsTwo, Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        List<Map<String,Object>> datas = userMapper.selectWithdrawingUser(userIds);
        if (!Common.isNull(userIdsTwo)){
            String[] str = userIdsTwo.split(",");
            for (int i = 0; i < datas.size(); i++) {
                if (Common.isExist(str,String.valueOf(datas.get(i).get("id")))){
                    datas.get(i).put("isDrawing",1);
                }
            }
        }
        return RestUtil.createResponseData(new PageInfo<>(datas));
    }

    public JsonRestResponse cashapplyClosure(Integer status,String userIds) {
        if (Common.isNull(userIds)){
            throw new BusinessException("common.parm.erro");
        }
        String[] ids = userIds.split(",");
        for (String id:ids) {
            cashapplyOrderMapper.updateCashapplyMold(Integer.valueOf(id),status);
            accountMapper.updateAccountStatusToUserId(Integer.valueOf(id),status);
        }
        return RestUtil.createResponse();
    }

    public JsonRestResponse selectRegisterWarning(HttpServletRequest request, Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        List<Map<String,Object>> datas = userMapper.selectRegisterWarning();
        return RestUtil.createResponseData(new PageInfo<>(datas));
    }

    public JsonRestResponse selectWarningUsers(HttpServletRequest request, Integer startPage, Integer pageSize,
                                               Integer userId) {
        PageHelper.startPage(startPage, pageSize);
        List<Map<String,Object>> datas = userMapper.selectWarningUsers(userId);
        return RestUtil.createResponseData(new PageInfo<>(datas));
    }
}
