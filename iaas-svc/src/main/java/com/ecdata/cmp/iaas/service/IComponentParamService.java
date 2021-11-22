package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.component.IaasComponent;
import com.ecdata.cmp.iaas.entity.component.IaasComponentHistory;
import com.ecdata.cmp.iaas.entity.component.IaasComponentParam;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentParamVO;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentVO;

import java.util.List;

public interface IComponentParamService extends IService<IaasComponentParam> {

    void modifyUpdateRecord(Long id, Long userId);

//    List<IaasComponentParamVO> qryComponentParamInfo(Long compId);

    BaseResponse saveComponentParamHistory(IaasComponentVO componentVo, IaasComponentVO testVO,
                                   IaasComponentHistory history,
                                   IComponentParamHistoryService compHisParamService,
                                   BaseResponse baseResponse)throws Exception;
//List<IaasComponentParam>

     boolean add(IaasComponentVO componentVo, IaasComponent component);

    boolean judge(IaasComponentVO componentVo, IaasComponentVO testVO,List<IaasComponentParamVO> update) throws Exception;

    void updateParam(IaasComponent component, BaseResponse baseResponse,
                     IaasComponentVO componentVo,
                     IaasComponentVO testVO,
                     List<IaasComponentParamVO> upList);


    void judgeAndSaveHistory(List<IaasComponentParamVO> pmList,
                             IaasComponent component,IaasComponentHistory history);

}
