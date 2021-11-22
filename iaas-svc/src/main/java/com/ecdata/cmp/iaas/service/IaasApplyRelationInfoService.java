package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyRelationInfo;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyRelationInfoParams;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasNextApplyUserParam;
import com.ecdata.cmp.iaas.entity.dto.apply.ProgramSupportParam;

import java.util.List;
import java.util.Map;

/**
 * @author xuxiaojian
 * @date 2020/4/16 11:09
 */
public interface IaasApplyRelationInfoService extends IService<IaasApplyRelationInfo> {

    /**
     * 绑定关系及下一级会签人处理
     *
     * @param params
     */
    void saveIaasApplyRelationInfo(IaasApplyRelationInfoParams params);

    /**
     * 处理关系
     *
     * @param id
     */
    void handleRelationInfo(Long id, Long applyId, String processInstanceId);

    void programSupport(ProgramSupportParam param);

    void nextApplyUser(IaasNextApplyUserParam param);

    String queryApplyUser(Long applyId);

    List<Map<String,String>> queryApplyUserId(Long applyId);

    boolean deleteApplyUser(Long applyId,Long userId);

}
