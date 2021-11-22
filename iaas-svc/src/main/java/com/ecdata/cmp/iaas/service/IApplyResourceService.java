package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyConfigInfo;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyConfigInfoListVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyConfigInfoVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyResourceVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyServiceSecurityResourcesVO;
import com.ecdata.cmp.iaas.entity.dto.request.IaasApplyRequest;
import com.ecdata.cmp.iaas.entity.dto.response.apply.ApplyResponse;
import com.ecdata.cmp.user.dto.response.UserListResponse;

import java.util.List;
import java.util.Map;

/**
 * @author xuxiaojian
 * @date 2020/3/2 14:44
 */
public interface IApplyResourceService {
    //项目与申请资源保存
    ApplyResponse saveResourceApply(IaasApplyRequest vo);

    //删除项目与申请资源
    BaseResponse deleteApplyAll(Long applyId);

    //删除申请资源，虚拟机或裸金属
    BaseResponse deleteApplyResource(Long configId);

    //更新项目与申请资源
    BaseResponse updateResourceApply(IaasApplyRequest vo);

    //发起申请
    BaseResponse startResourceApply(IaasApplyResourceVO vo);

    //申请单提交
    BaseResponse submitApply(IaasApplyResourceVO applyResourceVO);

    //查看详情
    List<IaasApplyResourceVO> queryApplyResource(Long applyId);

    IPage<IaasApplyResourceVO> queryApplyResource(Page<IaasApplyResourceVO> page, Map<String, Object> params);

    IaasApplyConfigInfoVO queryApplyConfigInfo(Long configId);

    int queryApplyServerNamePrefix(String serverNamePrefix);

    //更改申请资源配置信息
    BaseResponse updateApplyConfigInfo(IaasApplyRequest applyRequest);

    //审核时详情展示
    IaasApplyResourceVO queryApplyDetails(Long applyId);

    //发起流程接口
    BaseResponse callActivitis(boolean isGroup,
                               String operationType,
                               long applyId,
                               BaseResponse baseResponse,
                               IaasApplyResourceVO applyResourceVO);

    /**
     * 重新存入一份，然后发起流程
     *
     * @param applyResourceVO
     */
    String copyResourceAndStartRecoveryApply(IaasApplyResourceVO applyResourceVO);

    IaasApplyConfigInfoListVO queryApplyResourceByProjectId(Long projectId);

    /**
     * 变更资源
     *
     * @param applyRequest
     * @return
     */
    ApplyResponse saveChangeResourceApply(IaasApplyRequest applyRequest);

    /**
     * 复制申请资源
     *
     * @param applyId
     * @return
     */
    IaasApplyResourceVO copyResourceApply(Long applyId);

    IaasApplyResourceVO queryApplyRelationDetails(Long applyId);

    /**
     * 解除申请资源和项目关系
     *
     * @param applyId
     */
    void removeResourceAndApplyInfo(Long applyId);

    /**
     * 处理资源变更回收
     *
     * @param applyRequest
     */
    ApplyResponse handleConfigChange(IaasApplyRequest applyRequest);

    String queryApplyResourceByProcessInstanceId(String processInstanceId);

    List<IaasApplyResourceVO> recycleResourcesAuto();

    IaasApplyConfigInfoVO queryApplyServiceSecurityResources(int applyType, String sysBusinessGroupId);

    void addCandidate(Long id, Long userId);

    UserListResponse getITDirectors();
}
