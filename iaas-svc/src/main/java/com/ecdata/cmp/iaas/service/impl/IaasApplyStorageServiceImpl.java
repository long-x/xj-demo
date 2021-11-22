package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyStorage;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyStorageVO;
import com.ecdata.cmp.iaas.entity.dto.response.apply.AuditResponse;
import com.ecdata.cmp.iaas.mapper.apply.IaasApplyStorageMapper;
import com.ecdata.cmp.iaas.service.IaasApplyStorageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xuxiaojian
 * @date 2020/3/11 10:40
 */
@Service
public class IaasApplyStorageServiceImpl implements IaasApplyStorageService {
    @Autowired
    private IaasApplyStorageMapper applyStorageMapper;

    @Override
    public AuditResponse saveIaasApplyStorage(IaasApplyStorageVO vo) {
        IaasApplyStorage queryStorage = applyStorageMapper.selectById(vo.getId());
        AuditResponse auditResponse = new AuditResponse();

        if (queryStorage == null) {
            IaasApplyStorage applyStorage = new IaasApplyStorage();
            BeanUtils.copyProperties(vo, applyStorage);

            long id = SnowFlakeIdGenerator.getInstance().nextId();

            auditResponse.setId(id);
            applyStorage.setId(id);
            applyStorage.setCreateTime(DateUtil.getNow());
            applyStorage.setCreateUser(Sign.getUserId());
            applyStorage.setIsDeleted(0);
            applyStorageMapper.insert(applyStorage);
        } else {
            auditResponse.setId(queryStorage.getId());
            BeanUtils.copyProperties(vo, queryStorage);
            applyStorageMapper.updateById(queryStorage);
        }

        return auditResponse;
    }

    @Override
    public BaseResponse deleteIaasApplyStorage(Long id) {
        applyStorageMapper.deleteById(id);
        return BaseResponse.builder().build();
    }
}
