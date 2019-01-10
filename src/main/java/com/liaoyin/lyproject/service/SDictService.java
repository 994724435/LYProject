package com.liaoyin.lyproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.base.service.BaseService;
import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.dao.MCashapplyOrderMapper;
import com.liaoyin.lyproject.dao.SAccountMapper;
import com.liaoyin.lyproject.dao.SDictMapper;
import com.liaoyin.lyproject.dao.SUserMapper;
import com.liaoyin.lyproject.entity.SDict;
import com.liaoyin.lyproject.util.RestUtil;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 作者：
 * 时间：2018/9/29 10:00
 * 描述：
 */
@Service
public class SDictService extends BaseService<SDictMapper,SDict> {

    @Resource
    private SDictMapper dictMapper;
    @Resource
    private MCashapplyOrderMapper cashapplyOrderMapper;
    @Resource
    private SAccountMapper accountMapper;
    @Resource
    private SUserMapper userMapper;


    public JsonRestResponse saveDict(String code, String dictType, String displayvalue, Double realvalue, Integer parentid) {
        SDict dict = new SDict();
        dict.setCode(code);
        dict.setDicttype(dictType);
        dict.setDisplayvalue(displayvalue);
        dict.setRealvalue(realvalue);
        dict.setParentid(parentid);
        dictMapper.insertSelective(dict);
        return RestUtil.createResponse(this.mapper.selectOne(dict));
    }

    public JsonRestResponse selectDict(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        List<SDict> datas = this.mapper.selectDictDictionary();
        PageInfo<SDict> p = new PageInfo<>(datas);
        return RestUtil.createResponseData(p);
    }

    public JsonRestResponse updateDict(Integer id, String code, String dictType, String displayvalue, Double realvalue, Integer parentid) {
        SDict dict = new SDict();
        dict.setId(id);
        dict.setCode(code);
        dict.setDicttype(dictType);
        dict.setDisplayvalue(displayvalue);
        dict.setRealvalue(realvalue);
        dict.setParentid(parentid);
        dictMapper.updateByPrimaryKeySelective(dict);
        if (Common.isEqual(code,Config.DICT_CASHAPPLY_CLOSURE)){
            if (Common.isEqual(realvalue.intValue(),1)){
                String userIds = userMapper.selectWarningUserIds();
                if (!Common.isNull(userIds)){
                    cashapplyOrderMapper.updateCashapplyMoldIsSwitchToUserIds(0,userIds);
                    accountMapper.updateAccountStatusAndUserIds(0,userIds);
                }
            }
        }
        return RestUtil.createResponse(dict);
    }
}
