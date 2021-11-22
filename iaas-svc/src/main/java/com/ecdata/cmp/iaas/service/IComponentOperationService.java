package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.component.IaasComponent;
import com.ecdata.cmp.iaas.entity.component.IaasComponentHistory;
import com.ecdata.cmp.iaas.entity.component.IaasComponentOperation;
import com.ecdata.cmp.iaas.entity.component.IaasComponentScript;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentOperationVO;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentScriptVO;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentVO;

import java.util.List;
import java.util.Map;

public interface IComponentOperationService extends IService<IaasComponentOperation> {

    void modifyUpdateRecord(Long id, Long userId);

    List<IaasComponentOperationVO> qryComponentOperationInfo(Long compId);

    boolean judge(IaasComponentVO componentVo, IaasComponentVO testVO,
                  List<IaasComponentOperationVO> update) throws Exception;

    boolean judge(IaasComponentVO componentVo, IaasComponentVO testVO) throws Exception;

//compvo中
BaseResponse saveComponentOpHistory( IaasComponentVO componentVo, IaasComponentVO testVO,
                                 IaasComponentHistory history,
                                 IComponentOpHistoryService compHisOpService,
                                     IComponentScriptHistoryService compHisScriptService,
                                     BaseResponse baseResponse) throws Exception ;


    void judgeAndSaveHistory(List<IaasComponentOperationVO> opList,
                             IaasComponent component,IaasComponentHistory history);


    void updateOperation(
                         List<IaasComponentOperationVO> opUpList,
                         List<IaasComponentOperation> removeList,
                         IaasComponent component, BaseResponse baseResponse,
                         IaasComponentVO testVO,
                         List<Map<String, Object>> keys);



    boolean add(IaasComponentScriptVO vo,IaasComponentVO componentVo,IaasComponent component);

    boolean defaultAddition(IaasComponent component);

//IaasComponentScriptVO scVo,
}
