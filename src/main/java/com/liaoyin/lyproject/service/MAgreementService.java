package com.liaoyin.lyproject.service;

import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.base.service.BaseService;
import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.dao.MAgreementMapper;
import com.liaoyin.lyproject.entity.MAgreement;
import com.liaoyin.lyproject.exception.BusinessException;
import com.liaoyin.lyproject.util.RestUtil;
import org.springframework.stereotype.Service;

/**
 * 作者：
 * 时间：2018/9/25 14:45
 * 描述：
 */
@Service
public class MAgreementService extends BaseService<MAgreementMapper,MAgreement> {

    public JsonRestResponse saveAgreement(MAgreement agreement) {
        MAgreement ag = this.mapper.selectAgreement(agreement.getType());
        if (!Common.isNull(ag)){
            throw new BusinessException("common.agreement.existence");
        }
        this.mapper.insert(ag);
        return RestUtil.createResponse(this.mapper.selectAgreement(agreement.getType()));
    }

    public JsonRestResponse updateAgreement(MAgreement agreement) {
        this.mapper.updateByPrimaryKeySelective(agreement);
        return RestUtil.createResponse(agreement);
    }

    public JsonRestResponse queryAgreement(Integer type) {
        MAgreement agreement = this.mapper.selectAgreement(type);
        if (Common.isNull(agreement)){
            throw new BusinessException("common.agreement.not");
        }
        return RestUtil.createResponse(agreement);
    }
}
