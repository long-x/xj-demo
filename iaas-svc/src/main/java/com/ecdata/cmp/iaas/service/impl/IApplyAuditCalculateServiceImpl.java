package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyCalculate;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyCalculateVO;
import com.ecdata.cmp.iaas.entity.dto.response.apply.AuditResponse;
import com.ecdata.cmp.iaas.mapper.apply.IaasApplyCalculateMapper;
import com.ecdata.cmp.iaas.service.IApplyAuditCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ty
 * @date 2020/3/10 16:21
 */
@Slf4j
@Service
public class IApplyAuditCalculateServiceImpl implements IApplyAuditCalculateService {
    @Autowired
    private IaasApplyCalculateMapper iaasApplyCalculateMapper;

    @Override
    public AuditResponse saveIaasApplyCalculate(IaasApplyCalculateVO iaasApplyCalculateVO) throws Exception {
        IaasApplyCalculate queryCalculate = iaasApplyCalculateMapper.selectById(iaasApplyCalculateVO.getId());
        AuditResponse auditResponse = new AuditResponse();
        if (queryCalculate == null) {
            IaasApplyCalculate iaasApplyCalculate = new IaasApplyCalculate();
            BeanUtils.copyProperties(iaasApplyCalculateVO, iaasApplyCalculate);

            long id = SnowFlakeIdGenerator.getInstance().nextId();
            auditResponse.setId(id);
            iaasApplyCalculate.setId(id);
            iaasApplyCalculate.setCreateTime(DateUtil.getNow());
            iaasApplyCalculate.setCreateUser(Sign.getUserId());
            iaasApplyCalculate.setIsDeleted(0);
            iaasApplyCalculateMapper.insert(iaasApplyCalculate);
        } else {
            auditResponse.setId(queryCalculate.getId());
            BeanUtils.copyProperties(iaasApplyCalculateVO, queryCalculate);
            iaasApplyCalculateMapper.updateById(queryCalculate);
        }

        return auditResponse;
    }

    @Override
    public BaseResponse deleteIaasApplyCalculate(Long id) throws Exception {
        iaasApplyCalculateMapper.deleteCalculate(id);
        return new BaseResponse();
    }
}
