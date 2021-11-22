package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.apply.IaasApplySecurityServer;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplySecurityServerVO;
import com.ecdata.cmp.iaas.entity.dto.response.apply.AuditResponse;
import com.ecdata.cmp.iaas.mapper.apply.IaasApplySecurityServerMapper;
import com.ecdata.cmp.iaas.service.IApplySecurityServerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author ty
 * @date 2020/3/11 10:46
 */
@Service
public class IApplySecurityServerServiceImpl implements IApplySecurityServerService {

    @Autowired
    private IaasApplySecurityServerMapper iaasApplySecurityServerMapper;

    @Override
    public AuditResponse saveSecurityServer(IaasApplySecurityServerVO iaasApplySecurityServerVO) throws Exception {
        IaasApplySecurityServer querySecurityServer = iaasApplySecurityServerMapper.selectById(iaasApplySecurityServerVO.getId());
        AuditResponse auditResponse = new AuditResponse();
        if (querySecurityServer == null) {
            IaasApplySecurityServer iaasApplySecurityServer = new IaasApplySecurityServer();
            BeanUtils.copyProperties(iaasApplySecurityServerVO, iaasApplySecurityServer);

            long id = SnowFlakeIdGenerator.getInstance().nextId();

            auditResponse.setId(id);
            iaasApplySecurityServer.setId(id);
            iaasApplySecurityServer.setCreateUser(Sign.getUserId());
            iaasApplySecurityServer.setCreateTime(new Date());
            iaasApplySecurityServer.setIsDeleted(0);
            iaasApplySecurityServerMapper.insert(iaasApplySecurityServer);
        } else {
            auditResponse.setId(querySecurityServer.getId());
            BeanUtils.copyProperties(iaasApplySecurityServerVO, querySecurityServer);
            iaasApplySecurityServerMapper.updateById(querySecurityServer);
        }
        return auditResponse;
    }

    @Override
    public BaseResponse deleteSecurityServer(Long id) throws Exception {
        iaasApplySecurityServerMapper.deleteApplySecurityServer(id);
        return new BaseResponse();
    }
}
