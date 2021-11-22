package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.apply.IaasApplySoftwareServer;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplySoftwareServerVO;
import com.ecdata.cmp.iaas.entity.dto.response.apply.AuditResponse;
import com.ecdata.cmp.iaas.mapper.apply.IaasApplySoftwareServerMapper;
import com.ecdata.cmp.iaas.service.IaasApplySoftwareServerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xuxiaojian
 * @date 2020/3/11 10:49
 */
@Service
public class IaasApplySoftwareServerServiceImpl implements IaasApplySoftwareServerService {
    @Autowired
    private IaasApplySoftwareServerMapper softwareServerMapper;

    @Override
    public AuditResponse saveIaasApplySoftwareServer(IaasApplySoftwareServerVO vo) {
        IaasApplySoftwareServer querySoftwareServer = softwareServerMapper.selectById(vo.getId());
        AuditResponse auditResponse = new AuditResponse();

        if (querySoftwareServer == null) {
            IaasApplySoftwareServer applyStorage = new IaasApplySoftwareServer();
            BeanUtils.copyProperties(vo, applyStorage);

            long id = SnowFlakeIdGenerator.getInstance().nextId();
            auditResponse.setId(id);

            applyStorage.setId(id);
            applyStorage.setCreateTime(DateUtil.getNow());
            applyStorage.setCreateUser(Sign.getUserId());
            applyStorage.setIsDeleted(0);
            softwareServerMapper.insert(applyStorage);
        } else {
            auditResponse.setId(querySoftwareServer.getId());
            BeanUtils.copyProperties(vo, querySoftwareServer);
            softwareServerMapper.updateById(querySoftwareServer);
        }

        return auditResponse;
    }

    @Override
    public BaseResponse deleteIaasApplySoftwareServer(Long id) {
        softwareServerMapper.deleteById(id);
        return BaseResponse.builder().build();
    }
}
