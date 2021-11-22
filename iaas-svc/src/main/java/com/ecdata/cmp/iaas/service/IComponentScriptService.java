package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.component.IaasComponent;
import com.ecdata.cmp.iaas.entity.component.IaasComponentHistory;
import com.ecdata.cmp.iaas.entity.component.IaasComponentScript;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentScriptVO;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentVO;

import java.util.List;

public interface IComponentScriptService extends IService<IaasComponentScript> {

    void modifyUpdateRecord(Long id, Long userId);

    List<IaasComponentScriptVO> qryComponentScriptInfo(Long compId);

    BaseResponse saveComponentScriptHistory(IaasComponentVO componentVo, IaasComponentVO testVO,
                                            IaasComponentHistory history,
                                            IComponentScriptHistoryService compHisScriptService,
                                            IComponentOpHistoryService compHisOpService,
                                            BaseResponse baseResponse)throws Exception;

    //List<IaasComponentScript>
    boolean add(IaasComponentVO componentVo,IaasComponent component);

    void saveScriptHis(IaasComponentScriptVO sc, IaasComponentHistory history,
                       IComponentScriptHistoryService compHisScriptService);

     void updateScript(IaasComponent component,BaseResponse baseResponse,
                               IaasComponentVO componentVo,
                               IaasComponentVO testVO,
                               List<IaasComponentScriptVO> upList
    );

    void judgeAndSaveHistory(IaasComponentVO testVO,//List<IaasComponentScriptVO> scList,
                             IaasComponent component,IaasComponentHistory history);


    boolean judge(IaasComponentVO componentVo, IaasComponentVO testVO,
                  List<IaasComponentScriptVO> update) throws Exception;

    boolean judge(IaasComponentVO componentVo, IaasComponentVO testVO) throws Exception;




}
