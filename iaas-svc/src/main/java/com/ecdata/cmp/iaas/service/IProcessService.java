package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVO;
import com.ecdata.cmp.iaas.entity.process.IaasProcessApply;

import java.util.List;
import java.util.Map;

/**
 * @author xxj
 */
public interface IProcessService {

    /**
     * 服务申请保存
     *
     * @param processApplyVO
     * @return
     */
    BaseResponse saveProcess(IaasProcessApplyVO processApplyVO);

    /**
     * 已申请服务查询
     *
     * @param page
     * @param params
     * @return
     */
    IPage<IaasProcessApplyVO> queryProcessApply(Page<IaasProcessApplyVO> page, Map<String, Object> params);

    /**
     * 查询当前用户id
     *
     * @param params
     * @return
     */
    List<IaasProcessApplyVO> queryCurrentUserProcessApply(Map<String, Object> params);

    /**
     * 申请服务详情
     *
     * @param processApplyId
     * @return
     */
    IaasProcessApplyVO queryProcessApplyDetails(Long processApplyId);

    /**
     * 编辑服务申请
     *
     * @param processApplyVO
     * @return
     */
    BaseResponse editProcess(IaasProcessApplyVO processApplyVO);

    /**
     * 撤销发布
     *
     * @param processApplyVO
     */
    BaseResponse updateProcess(IaasProcessApplyVO processApplyVO);

    /**
     * 删除服务申请信息
     *
     * @param processApplyId
     */
    void deleteProcess(Long processApplyId,String processInstanceId,String deleteReason);
}
