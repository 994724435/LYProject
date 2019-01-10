package com.liaoyin.lyproject.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.base.service.BaseService;
import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.common.bean.SessionBean;
import com.liaoyin.lyproject.common.util.AESUtils;
import com.liaoyin.lyproject.common.util.IpAdrressUtil;
import com.liaoyin.lyproject.dao.*;
import com.liaoyin.lyproject.entity.*;
import com.liaoyin.lyproject.exception.BusinessException;
import com.liaoyin.lyproject.util.RedisUtil;
import com.liaoyin.lyproject.util.RestUtil;
import com.liaoyin.lyproject.ztest.Test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 作者：
 * 时间：2018/9/21 16:28
 * 描述：
 */
@Service
@Slf4j
public class SUserService extends BaseService<SUserMapper, SUser> {
    @Resource
    private SAccountMapper accountMapper;
    @Resource
    private MSmsCodeMapper smsCodeMapper;
    @Resource
    private SDictMapper dictMapper;
    @Resource
    private SUserLoginLogMapper userLoginLogMapper;
    @Autowired
    private SAccountRecordService accountRecordService;
    @Resource
    private SAccountRecordMapper accountRecordMapper;
    @Resource
    private MCashapplyOrderMapper cashapplyOrderMapper;
    @Resource
    private MMateMapper mateMapper;


    public JsonRestResponse userRegister(SUser user, String code,Integer refereeId,SAccount account) {
        if (user.getUseraccount().length() != 11) {
            throw new BusinessException("common.userPhoneLength.erro");
        }
        if (user.getPassword().length() < 6 || user.getPassword().length() > 18) {
            throw new BusinessException("common.userPasswordLength.erro");
        }
        if (!Common.isContainNumber(user.getPassword()) || !Common.isContainLetter(user.getPassword())) {
            throw new BusinessException("common.userPasswordNotNumLetter.erro");
        }
        if (Common.isNull(refereeId)){
            throw new BusinessException("common.userregister.referee.not");
        }
//        Object rc = RedisUtil.get(user.getUseraccount() + Config.REGISTER);
//        if (Common.isNull(rc)) {
//            throw new BusinessException("common.sendCode.not");
//        }
//        if (!Common.isEqual(rc.toString(), code)) {
//            throw new BusinessException("common.sendCode.Code.Erro");
//        }
//        MSmsCode smsCode = smsCodeMapper.selectCodeNewest(user.getUseraccount(), Config.REGISTER);
//        if (Common.isNull(smsCode)) {
//            throw new BusinessException("common.sendCode.not");
//        }
//        if (!Common.isEqual(smsCode.getCode(), code)) {
//            throw new BusinessException("common.sendCode.Code.Erro");
//        }
        if (Common.isNull(account.getAlipayaccount())&&Common.isNull(account.getWxpayaccount())){
            throw new BusinessException("common.account.true.not");
        }
        SUser dataUser = this.mapper.selectUserPhone(user.getUseraccount());
        if (!Common.isNull(dataUser)) {
            throw new BusinessException("common.userPhone");
        }
        if (!duplicationOfData(account)){
            throw new BusinessException("common.duplication.account");
        }
        SUser refreeUser = this.mapper.selectByPrimaryKey(refereeId);
       if (Common.isNull(refreeUser)){
           throw new BusinessException("common.refree.account.delete");
        }
        if (Common.isEqual(refreeUser.getIsdelete(),1)){
            throw new BusinessException("common.refree.account.delete");
        }
        if (!Common.isEqual(refreeUser.getStatus(),1)){
            return RestUtil.createResponseErro("推荐二维码已失效,(推荐人"+userStatusMsg(refreeUser.getStatus())+")");
        }
        //新增用户
        String uuid = UUID.randomUUID().toString();
        user.setArrangement(0);
        user.setStatus(0);
        user.setIsdelete(0);
        user.setMold(1);
        user.setSocketUuid(uuid);
        user.setUsernickname("用户_" + uuid.replaceAll("-", ""));
        user.setToken(Common.getToken(user.getUseraccount()));
        user.setCreatedate(new Date());
        user.setPassword(Common.personEncrypt(user.getPassword()));
        user.setRefereeId(refereeId);
        this.insert(user);
        SUser su = this.selectOne(user);
        //新增账户
        account.setIntegral(0);
        account.setTotalprice(0);
        account.setCanprice(0);
        account.setUserid(su.getId());
        account.setRefereeNum(0);
        account.setStatus(0);
        accountMapper.insert(account);
        //推荐人操作
        SAccount refereeAccount = accountMapper.selectAccountUserId(refereeId);
        if (!Common.isNull(refereeAccount)){
            refereeAccount.setRefereeNum(refereeAccount.getRefereeNum()+1);
            accountMapper.updateByPrimaryKeySelective(refereeAccount);
        }
        return RestUtil.createResponse(su);
    }

    public JsonRestResponse queryUser(String key, Integer status, Integer startPage, Integer pageSize,HttpServletRequest request) {
        SessionBean bean = Config.getSessionBean(request);
        //判定是否是客服
        if (!Arrays.asList(Config.kfids).contains(bean.getUid())){
            if (Common.isNull(key)){
                PageHelper.startPage(startPage, pageSize);
                List<Map<String, Object>> users = this.mapper.selectUserListMap(key, status, bean.getUid(),0,0);
                return RestUtil.createResponseData(new PageInfo<>(users));
            }else {
                String ids = this.mapper.selectUserIdChildLst(bean.getUid());
                ids = ids.replaceAll(",,",",");
                if (ids.startsWith(",")){
                    ids = ids.substring(1,ids.length());
                }
                if (ids.endsWith(",")){
                    ids = ids.substring(0,ids.length()-1);
                }
                PageHelper.startPage(startPage, pageSize);
                List<Map<String, Object>> users = this.mapper.selectUserListMapChildLst(key,1,ids,0);
                //List<Map<String, Object>> users = this.mapper.selectUserListMapAllPhone(key);
                return RestUtil.createResponseData(new PageInfo<>(users));
            }
        }else{
            PageHelper.startPage(startPage, pageSize);
            List<Map<String, Object>> users = this.mapper.selectUserListMap(key, status, null,0,0);
            return RestUtil.createResponseData(new PageInfo<>(users));
        }
    }

    public static String userStatusMsg(Integer status){
        if (Common.isNull(status)){
            return "状态未传入";
        }
        String msg = "";
        switch (status){
            case 0://未激活
                msg = "账号未激活";
                break;
            case 1://已激活
                msg = "账号已激活";
                break;
            case 2://冻结
                msg = "账号已被冻结";
                break;
            case 3://封号
                msg = "账号已被封停";
                break;
            case 4://黑名单
                msg = "账号已被拉入黑名单";
                break;
        }
        return msg;
    }

    public JsonRestResponse userLogin(SUser user,HttpServletRequest request) {
        //登录只限主账号登录
        SUser u = this.mapper.selectArrangementUser(user.getUseraccount());
        if (Common.isNull(u)){
            throw new BusinessException("common.user.accountErro");
        }
        if (!Common.isEqual(u.getPassword(), user.getPassword())) {
            throw new BusinessException("common.user.passwordErro");
        }
        if (u.getStatus()!=1){
            return RestUtil.createResponseErro(userStatusMsg(u.getStatus()));
        }
        if (Common.isEqual(u.getIsdelete(),1)){
            throw new BusinessException("common.user.isdelete");
        }
        u.setLastlogintime(new Date());
        this.mapper.updateByPrimaryKeySelective(u);
        //注入日志
        SUserLoginLog loginLog = new SUserLoginLog();
        loginLog.setUseraccount(u.getUseraccount());
        loginLog.setUserpassword(u.getPassword());
        loginLog.setRealName(u.getRealName());
        loginLog.setRequestip(request.getRemoteAddr());
        loginLog.setRequestthread(Thread.currentThread().getName());
        loginLog.setCreatedate(new Date());
        userLoginLogMapper.insertSelective(loginLog);
        String token = u.getToken();
        if (Common.isNull(token) || Common.isNull(RedisUtil.get(token))) {
            try {
                token = AESUtils.encrypt(String.valueOf(Common.numberOnlyRoomId()), String.valueOf(u.getId()));
                //将该token绑定到用户库对应的字段中
                u.setToken(token);
                this.mapper.updateByPrimaryKeySelective(u);
                //1.将用户对应的信息注入userInfo
                SessionBean userInfo = new SessionBean();
                userInfo.setUid(u.getId());
                userInfo.setUserAccount(u.getUseraccount());
                userInfo.setUserNickName(u.getUsernickname());
                //2.将token、用户信息放入Redis，并设置对应的过期时间（7天）
                RedisUtil.set(token, JSON.toJSONString(userInfo), Long.valueOf(7 * 24 * 60 * 60));

            } catch (Exception e) {
                e.printStackTrace();
                log.error("token块儿出现异常");
            }
        } else {
            //存在时表示当前库中的token是有效的，在这里不做任何操作，如需做操作请根据业务来做
        }
        return RestUtil.createResponse(userInfoFilter(u));
//        boolean isLogin = false;
//        JsonRestResponse result = null;
//        SUser ul = null;
        //先验证是否是手机号登录的
//        List<SUser> userAll = this.mapper.selectUserPhoneAll(user.getUseraccount());
//        if (userAll.size() == 0) {
//            //再验证昵称
//            SUser userNick = this.mapper.selectUserNickName(user.getUseraccount());
//            if (Common.isNull(userNick)) {
//                //都不满足认为账号是错误的
//                throw new BusinessException("common.user.accountErro");
//            }
//                //满足昵称  验证密码这些
//                if (!Common.isEqual(userNick.getPassword(), user.getPassword())) {
//                    throw new BusinessException("common.user.passwordErro");
//                }
//                //attach为0表示当前是直接登录的 为1表示需要选择登录账号
//                userNick.setLastlogintime(new Date());
//                this.mapper.updateByPrimaryKeySelective(userNick);
//                result = RestUtil.createResponse(userInfoFilter(userNick), 0);
//                ul = userNick;
//                isLogin = true;
//
//        } else {
            //验证密码
//            int is = userAll.size();
//            for (int i = 0; i < is;i++) {
//                if (!Common.isEqual(userAll.get(i).getPassword(), user.getPassword())) {
//                    userAll.remove(0);
//                } else {
//                    userInfoFilter(userAll.get(0));
//                }
//            }
//            if (userAll.size() == 0) {
//                throw new BusinessException("common.user.passwordErro");
//            }
//            if (userAll.size() == 1) {
//                //验证密码
//                if (!Common.isEqual(userAll.get(0).getPassword(), user.getPassword())) {
//                    throw new BusinessException("common.user.passwordErro");
//                }
//                //attach为0表示当前是直接登录的 为1表示需要选择登录账号
//                userAll.get(0).setLastlogintime(new Date());
//                this.mapper.updateByPrimaryKeySelective(userAll.get(0));
//                result = RestUtil.createResponse(userInfoFilter(userAll.get(0)), 0);
//                ul = userAll.get(0);
//                isLogin = true;
//            } else {
//                for (int i = 0; i < userAll.size(); i++) {
//                    userInfoFilter(userAll.get(i));
//                }
//                result = RestUtil.createResponse(userAll, 1);
//            }
        }
//        if (isLogin) {
//            String token = ul.getToken();
//            if (Common.isNull(token) || Common.isNull(RedisUtil.get(token))) {
//                try {
//                    token = AESUtils.encrypt(String.valueOf(Common.numberOnlyRoomId()), String.valueOf(ul.getId()));
//                    //将该token绑定到用户库对应的字段中
//                    ul.setToken(token);
//                    this.mapper.updateByPrimaryKeySelective(ul);
//                    //绑定之后 执行以下逻辑↓↓↓↓↓↓↓↓↓↓
//                    //1.将用户对应的信息注入userInfo
//                    SessionBean userInfo = new SessionBean();
//                    userInfo.setUid(ul.getId());
//                    userInfo.setUserAccount(ul.getUseraccount());
//                    userInfo.setUserNickName(ul.getUsernickname());
//                    //2.将token、用户信息放入Redis，并设置对应的过期时间（7天）
//                    RedisUtil.set(token, JSON.toJSONString(userInfo), Long.valueOf(7 * 24 * 60 * 60));
//                    result = RestUtil.createResponse(ul, 0);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    log.error("token块儿出现异常");
//                }
//            } else {
//                //存在时表示当前库中的token是有效的，在这里不做任何操作，如需做操作请根据业务来做
//            }
//        }
//        return result;

   // }

    /**
     * 作者：
     * 时间： 2018/9/25 13:00
     * 描述： 用户数据过滤
     **/
    public static SUser userInfoFilter(SUser user) {
        if (Common.isNull(user))return null;
        user.setPassword(null);
        user.setTwopassword(null);
        user.setRefereeId(null);
        return user;
    }

    public JsonRestResponse queryUserDetail(Integer userId) {
        SUser user = this.mapper.selectByPrimaryKey(userId);
        if (Common.isNull(user)) {
            throw new BusinessException("common.userInfo.notExistent");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("userRefereeInfo",userInfoFilter(this.mapper.selectByPrimaryKey(user.getRefereeId())));
        data.put("userInfo", userInfoFilter(user));
        data.put("accountInfo", accountMapper.selectAccountUserId(userId));
        return RestUtil.createResponse(data);
    }

    public JsonRestResponse updatePassword(SUser user, String code) {
        SUser su = this.mapper.selectByPrimaryKey(user.getId());
        if (Common.isNull(su)) {
            throw new BusinessException("common.parm.erro");
        }
        Object rc = RedisUtil.get(su.getUseraccount() + Config.UPDATEPASSWORD);
        if (Common.isNull(rc)) {
            throw new BusinessException("common.sendCode.not");
        }
        if (!Common.isEqual(rc.toString(), code)) {
            throw new BusinessException("common.sendCode.Code.Erro");
        }
        MSmsCode smsCode = smsCodeMapper.selectCodeNewest(su.getUseraccount(), Config.UPDATEPASSWORD);
        if (Common.isNull(smsCode)) {
            throw new BusinessException("common.sendCode.not");
        }
        if (!Common.isEqual(smsCode.getCode(), code)) {
            throw new BusinessException("common.sendCode.Code.Erro");
        }
        this.mapper.updateByPrimaryKeySelective(user);
        return RestUtil.createResponse();
    }

    public JsonRestResponse forgetPassword(SUser user, String code) {
        SUser su = this.mapper.selectUserPhone(user.getUseraccount());
        if (Common.isNull(su)) {
            throw new BusinessException("common.parm.erro");
        }
        Object rc = RedisUtil.get(su.getUseraccount() + Config.FORGETPASSWORD);
        if (Common.isNull(rc)) {
            throw new BusinessException("common.sendCode.not");
        }
        if (!Common.isEqual(rc.toString(), code)) {
            throw new BusinessException("common.sendCode.Code.Erro");
        }
        MSmsCode smsCode = smsCodeMapper.selectCodeNewest(su.getUseraccount(), Config.FORGETPASSWORD);
        if (Common.isNull(smsCode)) {
            throw new BusinessException("common.sendCode.not");
        }
        if (!Common.isEqual(smsCode.getCode(), code)) {
            throw new BusinessException("common.sendCode.Code.Erro");
        }
        su.setPassword(user.getPassword());
        this.mapper.updateByPrimaryKeySelective(su);
        return RestUtil.createResponse();
    }

    public synchronized JsonRestResponse saveSonAccount(HttpServletRequest request, String userNickName, String userPassword, String code,
                                                        String alipayAccount, String wxpayAccount, String bankAccount,String bankName,
                                                        String realName) {
        if (Common.isNull(alipayAccount)&&Common.isNull(wxpayAccount)){
            throw new BusinessException("common.account.true.not");
        }
        SessionBean bean = Config.getSessionBean(request);
        if (Common.isNull(bean)){
            throw new BusinessException("common.tokenInvalid");
        }
        Integer userId = bean.getUid();
        SUser nickUs = this.mapper.selectUserNickName(userNickName);
        if (!Common.isNull(nickUs)) {
            throw new BusinessException("common.userInfo.nickName.exist");
        }
        SUser user = this.mapper.selectByPrimaryKey(userId);
        if (Common.isNull(user)) {
            throw new BusinessException("common.parm.erro");
        }
        if (user.getArrangement()!=0){
            throw new BusinessException("common.arrangement.erro");
        }
        if (!userNickName.startsWith(user.getRealName())){
            throw new BusinessException("common.user.nickName.erro");
        }
        SAccount a = accountMapper.selectAccountUserId(userId);
        if (!Common.isEqual(a.getBankaccount(),bankAccount)){
            throw new BusinessException("common.account.z.bankaccount.erro");
        }
        if (!Common.isEqual(a.getBankName(),bankName)){
            throw new BusinessException("common.account.z.bankname.erro");
        }
        if (!Common.isNull(a.getWxpayaccount())){
            if (!Common.isEqual(a.getWxpayaccount(),wxpayAccount)){
                throw new BusinessException("common.account.z.wxaccount.erro");
            }
        }
        if (!Common.isNull(a.getAlipayaccount())){
            if (!Common.isEqual(a.getAlipayaccount(),alipayAccount)){
                throw new BusinessException("common.account.z.aliaccount.erro");
            }
        }
//        Object rc = RedisUtil.get(user.getUseraccount() + Config.SAVESUNACCOUNT);
//        if (Common.isNull(rc)) {
//            throw new BusinessException("common.sendCode.not");
//        }
//        if (!Common.isEqual(rc.toString(), code)) {
//            throw new BusinessException("common.sendCode.Code.Erro");
//        }
//        MSmsCode smsCode = smsCodeMapper.selectCodeNewest(user.getUseraccount(), Config.SAVESUNACCOUNT);
//        if (Common.isNull(smsCode)) {
//            throw new BusinessException("common.sendCode.not");
//        }
//        if (!Common.isEqual(smsCode.getCode(), code)) {
//            throw new BusinessException("common.sendCode.Code.Erro");
//        }
        SDict dict = dictMapper.selectDict(Config.DICT_SUNACCOUNT_NUM);
        if (Common.isNull(dict)) {
            throw new BusinessException("common.dict.not");
        }
        List<SUser> lus = this.mapper.selectUserPhoneAll(user.getUseraccount());
        Integer aggrentNum = dict.getRealvalue().intValue()+user.getArrangement();
        if (lus.size() > aggrentNum) {
            throw new BusinessException("common.dictNum.erro", "最多只能添加" + aggrentNum + "个子账号");
        }
        SUser newUser = new SUser();
        newUser.setUseraccount(user.getUseraccount());
        newUser.setUsernickname(userNickName);
        newUser.setRealName(realName);
        newUser.setPassword(Common.personEncrypt(userPassword));
        newUser.setArrangement(1);
        newUser.setToken(AESUtils.encrypt(String.valueOf(Common.numberOnlyRoomId()), String.valueOf(userNickName)));
        newUser.setStatus(1);
        newUser.setIsdelete(0);
        newUser.setMold(1);
        newUser.setSocketUuid(UUID.randomUUID().toString());
        newUser.setCreatedate(new Date());
        this.mapper.insert(newUser);
        newUser = this.mapper.selectOne(newUser);
        //新增账户
        SAccount account = new SAccount();
        account.setIntegral(0);
        account.setTotalprice(0);
        account.setCanprice(0);
        account.setUserid(newUser.getId());
        account.setAlipayaccount(alipayAccount);
        account.setWxpayaccount(wxpayAccount);
        account.setBankaccount(bankAccount);
        account.setBankName(bankName);
        account.setRefereeNum(0);
        accountMapper.insert(account);
        return RestUtil.createResponse();
    }

    public JsonRestResponse updateUserInfo(HttpServletRequest request, String userNickName, String twopassword,
                                           String alipayAccount, String wxpayAccount, String bankAccount,
                                           String realName,String bankName,Integer mold,String code) {

        SessionBean bean = Config.getSessionBean(request);
        if (Common.isNull(bean)){
            throw new BusinessException("common.tokenInvalid");
        }
        Integer userId = bean.getUid();
        SUser user = this.mapper.selectByPrimaryKey(userId);
        if (Common.isNull(user)){
            throw new BusinessException("common.parm.erro");
        }
        if (Common.isNull(mold)){
            Object rc = RedisUtil.get(user.getUseraccount() + Config.UPDATEUSERINFO);
            if (Common.isNull(rc)) {
                throw new BusinessException("common.sendCode.not");
            }
            if (!Common.isEqual(rc.toString(), code)) {
                throw new BusinessException("common.sendCode.Code.Erro");
            }
            MSmsCode smsCode = smsCodeMapper.selectCodeNewest(user.getUseraccount(), Config.UPDATEUSERINFO);
            if (Common.isNull(smsCode)) {
                throw new BusinessException("common.sendCode.not");
            }
            if (!Common.isEqual(smsCode.getCode(), code)) {
                throw new BusinessException("common.sendCode.Code.Erro");
            }
        }else {
            if (Common.isEqual(user.getArrangement(),1)){
                throw new BusinessException("common.user.arrangement.erro");
            }
        }
//        if (!Common.isNull(userNickName)){
//            SUser su = this.mapper.selectUserNickName(userNickName);
//            if (!Common.isNull(su)){
//                if (su.getId()!=userId)throw new BusinessException("common.userInfo.nickName.exist");
//            }
//        }
        if (!Common.isNull(userNickName)||!Common.isNull(twopassword)||
                !Common.isNull(realName)||!Common.isNull(mold)){
            user.setUsernickname(userNickName);
            user.setTwopassword(Common.personEncrypt(twopassword));
            user.setRealName(realName);
            user.setMold(mold);
            this.mapper.updateByPrimaryKeySelective(user);
        }
        if (!Common.isNull(alipayAccount)||!Common.isNull(wxpayAccount)||!Common.isNull(bankAccount)||!Common.isNull(bankName)){
            SAccount account = accountMapper.selectAccountUserId(userId);
            if (!duplicationOfData(account)){
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

    public JsonRestResponse querySonAccount(HttpServletRequest request) {
        List<SUser> lists = this.mapper.selectUserPhoneAll(Config.getSessionBean(request).getUserAccount());
        for (int i = 0; i < lists.size(); i++) {
            userInfoFilter(lists.get(i));
        }
        return RestUtil.createResponse(lists);
    }

    public JsonRestResponse sonLogin(String sonToken,HttpServletRequest request) {
        SessionBean bean = Config.getSessionBean(request);
        if (Common.isNull(bean)){
            throw new BusinessException("common.tokenInvalid");
        }
        SUser user = this.mapper.selectUserInfoToken(sonToken,bean.getUserAccount());
        if (Common.isNull(user)){
            throw new BusinessException("common.sontoken.not");
        }
        String token = user.getToken();
        if (Common.isNull(token) || Common.isNull(RedisUtil.get(token))) {
            try {
                token = AESUtils.encrypt(String.valueOf(Common.numberOnlyRoomId()), String.valueOf(user.getId()));
                //将该token绑定到用户库对应的字段中
                user.setToken(token);
                this.mapper.updateByPrimaryKeySelective(user);
                //绑定之后 执行以下逻辑↓↓↓↓↓↓↓↓↓↓
                //1.将用户对应的信息注入userInfo
                SessionBean userInfo = new SessionBean();
                userInfo.setUid(user.getId());
                userInfo.setUserAccount(user.getUseraccount());
                userInfo.setUserNickName(user.getUsernickname());
                //2.将token、用户信息放入Redis，并设置对应的过期时间（7天）
                RedisUtil.set(token, JSON.toJSONString(userInfo), Long.valueOf(7 * 24 * 60 * 60));
            } catch (Exception e) {
                e.printStackTrace();
                log.error("token块儿出现异常");
            }
        }
        return RestUtil.createResponse(userInfoFilter(user));
    }

    /**
     * 作者：
     * 时间： 2018/10/4 17:09
     * 描述： 用户个人资料完善判定
     **/
    public boolean userInfoIsPerfect(Integer userId,HttpServletRequest request){
        String userInfoIsPerfectMsg = "资料完善度不足";
        boolean boo = true;
        SUser user = this.mapper.selectByPrimaryKey(userId);
        if (Common.isNull(user.getRealName())){
            boo = false;
            userInfoIsPerfectMsg += "，真实姓名未填写";
        }
        SAccount account = accountMapper.selectAccountUserId(userId);
        if (Common.isNull(account.getBankaccount())){
            boo = false;
            userInfoIsPerfectMsg += "，银行卡必须填写";
        }
        if (Common.isNull(account.getAlipayaccount()) && Common.isNull(account.getWxpayaccount())
                && Common.isNull(account.getBankaccount())){
            boo = false;
            userInfoIsPerfectMsg +="，支付宝、微信、银行卡至少填写一种";
        }else{
            if (!Common.isNull(account.getBankaccount())){
                if (Common.isNull(account.getBankName())){
                    boo = false;
                    userInfoIsPerfectMsg +="，银行卡必须指定银行名称";
                }
            }
        }
        request.getSession().setAttribute("userInfoIsPerfectMsg",userInfoIsPerfectMsg);
        return boo;
    }

    /**
     * 作者：
     * 时间： 2018/10/27 19:31
     * 描述： 资料重复度验证
     **/
    public boolean duplicationOfData(SAccount account){
        List<SAccount> accounts = accountMapper.select(account);
        if (accounts.size()>0){
            if (Common.isNull(account.getId())){
                return false;
            }else {
                for (int i = 0; i < accounts.size(); i++) {
                    if (Common.isEqual(account.getId(),accounts.get(i).getId())){
                        return true;
                    }
                }
                return false;
            }
        }
        return true;
    }



    public JsonRestResponse userActivation(Integer userId, HttpServletRequest request) {
        SessionBean bean = Config.getSessionBean(request);
        if (Common.isNull(bean)){
            throw new BusinessException("common.tokenInvalid");
        }
        SUser user = this.mapper.selectByPrimaryKey(userId);
        if (Common.isNull(user)){
            throw new BusinessException("common.userInfo.notExistent");
        }
        if (!Arrays.asList(Config.kfids).contains(bean.getUid())){
            if (!Common.isEqual(user.getRefereeId(),bean.getUid())){
                throw new BusinessException("common.user.activation.erro");
            }
        }
        if (!Common.isEqual(user.getStatus(),0)){
            return RestUtil.createResponseErro(userStatusMsg(user.getStatus()));
        }
        user.setStatus(1);
        this.mapper.updateByPrimaryKeySelective(user);
        return RestUtil.createResponse();
    }

    public synchronized JsonRestResponse giftIntegralToRefereeUser(Integer refeeUserId,Integer integral, HttpServletRequest request) {
        SessionBean bean = Config.getSessionBean(request);
        if (Common.isNull(bean)){
            throw new BusinessException("common.tokenInvalid");
        }
        SUser user = this.mapper.selectByPrimaryKey(refeeUserId);
        if (Common.isNull(user)){
            throw new BusinessException("common.userInfo.notExistent");
        }
        if (Common.isEqual(user.getArrangement(),1)){
            //子账号
            if (!Common.isEqual(user.getUseraccount(),bean.getUserAccount())){
                throw new BusinessException("common.user.gift.integral.erro");
            }
        }
//        if (!Common.isEqual(user.getRefereeId(),bean.getUid())){
//            throw new BusinessException("common.user.gift.integral.erro");
//        }
        if (Arrays.asList(Config.kfids).contains(bean.getUid())){
           if (integral<10){
               return RestUtil.createResponseErro("最低10起转");
           }
        }else {
            if (integral<50){
                return RestUtil.createResponseErro("最低50起转");
            }
        }
        //赠送方
        SAccount account = accountMapper.selectAccountUserId(bean.getUid());
        if (account.getIntegral()<integral){
            throw new BusinessException("common.intgral.not");
        }
        account.setIntegral(account.getIntegral()-integral);
        accountMapper.updateByPrimaryKeySelective(account);
        accountRecordService.saveAccountRecord(Config.giftIntegral_ZS,integral,account.getIntegral(),
                0,1,4,refeeUserId.toString(),bean.getUid(),account.getId());
        //接收方
        SAccount ac = accountMapper.selectAccountUserId(refeeUserId);
        ac.setIntegral(ac.getIntegral()+integral);
        accountMapper.updateByPrimaryKeySelective(ac);
        accountRecordService.saveAccountRecord(Config.giftIntegral_JS,integral,ac.getIntegral(),
                1,1,4,bean.getUid().toString(),refeeUserId,ac.getId());
        return RestUtil.createResponse();
    }

    public JsonRestResponse userZr() {
        List<Map<String,Object>> d1 = this.mapper.userZr();
        for (int i = 0; i < d1.size(); i++) {
            Map<String,Object> dd = d1.get(i);
            //新增用户
            String uuid = UUID.randomUUID().toString();
            SUser user = new SUser();
            user.setUseraccount(dd.get("loginname").toString());
            user.setPassword(Common.personEncrypt2(dd.get("password").toString()));
            user.setArrangement(0);
            user.setStatus(0);
            user.setIsdelete(0);
            user.setMold(1);
            user.setSocketUuid(uuid);
            user.setUsernickname("用户_" + uuid.replaceAll("-", ""));
            user.setToken(Common.getToken(user.getUseraccount()));
            user.setCreatedate((Date) dd.get("registrationtime"));
            user.setLastlogintime((Date) dd.get("activationtime"));
            user.setRealName((String) dd.get("username"));
            user.setRefereeId((Integer) dd.get("userid"));
            user.setId((Integer) dd.get("id"));
            this.insert(user);
            SUser su = this.selectOne(user);
            //新增账户
            SAccount account = new SAccount();
            account.setIntegral((Integer) dd.get("integral"));
            account.setTotalprice((Integer) dd.get("balance"));
            account.setCanprice((Integer) dd.get("balance"));
            account.setUserid(su.getId());
            account.setAlipayaccount((String) dd.get("alipay"));
            account.setWxpayaccount((String) dd.get("wechat"));
            account.setBankaccount((String) dd.get("bank_card"));
            account.setBankName((String) dd.get("bank_add"));
            account.setRefereeNum(0);
            accountMapper.insert(account);
        }
        return RestUtil.createResponse();
    }

    public JsonRestResponse giftIntegralRecord() {
        List<Map<String,Object>> d1 = this.mapper.giftIntegralRecord();
        for (int i = 0; i < d1.size(); i++) {
            try {
                Map<String,Object> dd = d1.get(i);
                SAccountRecord record = new SAccountRecord();
                String typename = (String) dd.get("typename");
                record.setRecordbody(typename);
                Integer integral = (Integer) dd.get("shuliang");
                if (integral<0){
                    record.setRecordprice(integral*-1);
                    record.setRecordstatus(0);
                }else{
                    record.setRecordprice(integral);
                    record.setRecordstatus(1);
                }
                record.setRecordtype(1);
                if (Common.isEqual(typename,"积分转入")||Common.isEqual(typename,"积分转出")){
                    record.setRecordmold(4);
                }else if(Common.isEqual(typename,"排单和抢单扣取手续费")){
                    record.setRecordmold(0);
                }else {
                    record.setRecordmold(5);
                }
                record.setRecordtoobject(dd.get("target_userid").toString());
                record.setRecordtouserid((Integer) dd.get("cons_userid"));
                SAccount account = accountMapper.selectAccountUserId(record.getRecordtouserid());
                if (!Common.isNull(account)){
                    record.setRecordtoaccountid(account.getId());
                }
                record.setCreatedate((Date) dd.get("createtime"));
                accountRecordMapper.insertSelective(record);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return RestUtil.createResponse();
    }

    public JsonRestResponse giftCashapply() {
        List<Map<String,Object>> d1 = this.mapper.giftCashapply();
        for (int i = 0; i < d1.size(); i++) {
            try {
                Map<String,Object> dd = d1.get(i);
                MCashapplyOrder co = new MCashapplyOrder();
                Integer price = (Integer) dd.get("money");
                co.setPrice(price);
                Integer status = (Integer) dd.get("iscomplete");
                co.setStatus(status);
                if (status==0){
                    co.setRemainprice(price);
                    co.setMateprice(0);
                }else if (status==1){
                    co.setRemainprice(0);
                    co.setMateprice(price);
                }
                co.setUserid((Integer) dd.get("userid"));
                co.setCreatedate((Date) dd.get("t_time"));
                co.setId((Integer) dd.get("id"));
                cashapplyOrderMapper.insertSelective(co);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return RestUtil.createResponse();
    }

    public JsonRestResponse giftPlatoonOrder() {
        List<MCashapplyOrder> d1 = this.mapper.giftPlatoonOrder();
        for (int i = 0; i < d1.size(); i++) {
            try {
                MCashapplyOrder dd = d1.get(i);
//                SAccount account = accountMapper.selectAccountUserId(dd.getUserid());
//                account.setTotalprice(account.getTotalprice()+dd.getPrice());
//                account.setCanprice(account.getCanprice()+dd.getPrice());
//                accountMapper.updateByPrimaryKeySelective(account);
                cashapplyOrderMapper.delete(dd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.mapper.clearAccount();
        List<SUser> users = this.mapper.selectAll();
        for (int i = 0; i < users.size(); i++) {
            MCashapplyOrder c = this.mapper.giftCashapplyOrderOne(users.get(i).getId());
            SAccount account = accountMapper.selectAccountUserId(users.get(i).getId());
            if (Common.isNull(c)){
                Integer money = 0;
                Double rp = 0.0;
                List<MMate> mates = mateMapper.selectMateOut(users.get(i).getId());
                for (int j = 0; j < mates.size(); j++) {
                    money+=mates.get(j).getPrice();
                    if (mates.get(j).getType()==0){
                        rp += mates.get(j).getPrice()*0.15;
                    }else {
                        rp += mates.get(j).getPrice()*0.1;
                    }
                }
                Integer allPrice = money + rp.intValue();
                account.setTotalprice(account.getTotalprice()+allPrice);
                account.setCanprice(account.getCanprice()+allPrice);
                accountMapper.updateByPrimaryKeySelective(account);
            }else {
                Map<String,Object> p = this.mapper.giftPlatoonOrderOne(users.get(i).getId());
                Map<String,Object> r = this.mapper.robOrder(users.get(i).getId());
                if (!Common.isNull(p)) {
                    Date date = (Date) p.get("makePriceDate");
                    if (date.getTime() > c.getCreatedate().getTime()) {
                        Double rp = (Integer) p.get("price") * 0.15;
                        Integer allPrice = (Integer) p.get("price") + rp.intValue();
                        account.setTotalprice(account.getTotalprice() + allPrice);
                        account.setCanprice(account.getCanprice() + allPrice);
                        accountMapper.updateByPrimaryKeySelective(account);
                    }
                }
                if (!Common.isNull(r)){
                    Date date = (Date) r.get("makePriceDate");
                    if (date.getTime()>c.getCreatedate().getTime()){
                        Double rp = (Integer)r.get("price")*0.1;
                        Integer allPrice = (Integer)r.get("price") + rp.intValue();
                        account.setTotalprice(account.getTotalprice()+allPrice);
                        account.setCanprice(account.getCanprice()+allPrice);
                        accountMapper.updateByPrimaryKeySelective(account);
                    }
                }
            }
        }
//        List<MMate> al = this.mapper.selectAllMMate();
//        for (int i = 0; i < al.size(); i++) {
//            try {
//                MMate mate = al.get(i);
//                Double rp = mate.getPrice()*0.15;
//                Integer allPrice = mate.getPrice() + rp.intValue();
//                SAccount account = accountMapper.selectAccountUserId(mate.getPlantoonuserid());
//                account.setTotalprice(account.getTotalprice()+allPrice);
//                account.setCanprice(account.getCanprice()+allPrice);
//                accountMapper.updateByPrimaryKeySelective(account);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return RestUtil.createResponse();
    }

    public JsonRestResponse giftPlatoonOrderTwo() {
        //List<SUser> users = this.mapper.selectAllsUserSA();
        System.out.println("开始");
        List<Map> users = Test.data();
        for (int i = 0; i < users.size(); i++) {
            SAccount account = accountMapper.selectAccountUserId((Integer) users.get(i).get("id"));
            account.setTotalprice(0);
            account.setCanprice(0);
            accountMapper.updateByPrimaryKeySelective(account);
        }
        for (int i = 0; i < users.size(); i++) {
            try {
//                List<MPlatoonOrder> ps = this.mapper.platOrderOut((Integer) users.get(i).get("id"));
//                Integer money = 0;
//                for (int j = 0; j < ps.size(); j++) {
//                    money+=ps.get(j).getPrice();
//                }
//                List<MRobOrder> ro = this.mapper.robOrderOut((Integer) users.get(i).get("id"));
//                for (int j = 0; j < ro.size(); j++) {
//                    money+=ps.get(j).getPrice();
//                }
                Integer money = 0;
                Double rp = 0.0;
                List<MMate> mates = mateMapper.selectMateOut((Integer) users.get(i).get("id"));
                for (int j = 0; j < mates.size(); j++) {
                    money+=mates.get(j).getPrice();
                    if (mates.get(j).getType()==0){
                        rp += mates.get(j).getPrice()*0.15;
                    }else {
                        rp += mates.get(j).getPrice()*0.1;
                    }
                }
                SAccount account = accountMapper.selectAccountUserId((Integer) users.get(i).get("id"));
                Integer allPrice = money + rp.intValue();
                account.setTotalprice(account.getTotalprice()+allPrice);
                account.setCanprice(account.getCanprice()+allPrice);
                accountMapper.updateByPrimaryKeySelective(account);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        List<SUser> users = this.mapper.selectAll();
//        for (int i = 0; i < users.size(); i++) {
//            try {
//                MCashapplyOrder c = this.mapper.giftCashapplyOrderOne(users.get(i).getId());
//                Map<String,Object> p = this.mapper.giftPlatoonOrderOne(users.get(i).getId());
//                if (!Common.isNull(c)&&!Common.isNull(p)){
//                    Date date = (Date) p.get("makePriceDate");
//                    if (date.getTime()>c.getCreatedate().getTime()){
//                        SAccount account = accountMapper.selectAccountUserId((Integer) p.get("userId"));
//                        Double rp = (Integer)p.get("price")*0.15;
//                        Integer allPrice = (Integer)p.get("price") + rp.intValue();
//                        account.setTotalprice(account.getTotalprice()+allPrice);
//                        account.setCanprice(account.getCanprice()+allPrice);
//                        accountMapper.updateByPrimaryKeySelective(account);
//                    }
//                }
//                Map<String,Object> r = this.mapper.robOrder(users.get(i).getId());
//                if (!Common.isNull(c)&&!Common.isNull(r)){
//                    Date date = (Date) r.get("makePriceDate");
//                    if (date.getTime()>c.getCreatedate().getTime()){
//                        SAccount account = accountMapper.selectAccountUserId((Integer) r.get("userId"));
//                        Double rp = (Integer)r.get("price")*0.15;
//                        Integer allPrice = (Integer)r.get("price") + rp.intValue();
//                        account.setTotalprice(account.getTotalprice()+allPrice);
//                        account.setCanprice(account.getCanprice()+allPrice);
//                        accountMapper.updateByPrimaryKeySelective(account);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return RestUtil.createResponse();
    }

    public JsonRestResponse queryVersion(Integer versionNum, HttpServletRequest request) {
        SDict dict = dictMapper.selectDict(Config.DICT_VERSION_NUM);
        if (Common.isNull(dict)){
            throw new BusinessException("common.dict.not");
        }
        if (dict.getRealvalue().intValue()>versionNum){
            return RestUtil.createResponse(dict,true);
        }else {
            return RestUtil.createResponse(dict,false);
        }
    }
}
