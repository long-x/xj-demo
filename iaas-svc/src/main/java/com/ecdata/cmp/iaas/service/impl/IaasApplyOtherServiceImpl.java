package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyOther;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyOtherVO;
import com.ecdata.cmp.iaas.entity.dto.response.apply.AuditResponse;
import com.ecdata.cmp.iaas.mapper.apply.IaasApplyOtherMapper;
import com.ecdata.cmp.iaas.service.IaasApplyOtherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xuxiaojian
 * @date 2020/3/26 15:02
 */
@Service
public class IaasApplyOtherServiceImpl implements IaasApplyOtherService {
    @Autowired
    private IaasApplyOtherMapper iaasApplyOtherMapper;

    @Override
    public AuditResponse saveIaasApplyOther(IaasApplyOtherVO iaasApplyOtherVO) throws Exception {
        IaasApplyOther queryCalculate = iaasApplyOtherMapper.selectById(iaasApplyOtherVO.getId());
        AuditResponse auditResponse = new AuditResponse();
        if (queryCalculate == null) {
            IaasApplyOther IaasApplyOther = new IaasApplyOther();
            BeanUtils.copyProperties(iaasApplyOtherVO, IaasApplyOther);

            long id = SnowFlakeIdGenerator.getInstance().nextId();
            auditResponse.setId(id);
            IaasApplyOther.setId(id);
            IaasApplyOther.setCreateTime(DateUtil.getNow());
            IaasApplyOther.setCreateUser(Sign.getUserId());
            IaasApplyOther.setIsDeleted(0);
            iaasApplyOtherMapper.insert(IaasApplyOther);
        } else {
            auditResponse.setId(queryCalculate.getId());
            BeanUtils.copyProperties(iaasApplyOtherVO, queryCalculate);
            iaasApplyOtherMapper.updateById(queryCalculate);
        }
        return auditResponse;
    }

    @Override
    public BaseResponse deleteIaasApplyOther(Long id) throws Exception {
        iaasApplyOtherMapper.deleteApplyOther(id);
        return new BaseResponse();
    }
}
