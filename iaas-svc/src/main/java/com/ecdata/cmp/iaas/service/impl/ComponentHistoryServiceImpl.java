package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.iaas.entity.component.IaasComponent;
import com.ecdata.cmp.iaas.entity.component.IaasComponentHistory;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentHistoryVO;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentVO;
import com.ecdata.cmp.iaas.mapper.component.ComponentHistoryMapper;
import com.ecdata.cmp.iaas.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/11/13 10:02,
 */
@Slf4j
@Service
public class ComponentHistoryServiceImpl extends ServiceImpl<ComponentHistoryMapper, IaasComponentHistory>
        implements IComponentHistoryService {

    @Autowired
    IComponentScriptService componentScriptService;

    @Autowired
    IComponentParamService componentParamService;

    @Autowired
    IComponentOperationService componentOperationService;

    @Autowired
    IComponentService componentService;


    @Override
    public void modifyUpdateRecord(Long id, Long userId) {
        baseMapper.modifyUpdateRecord(id,userId);
    }

    @Override
    public List<IaasComponentHistoryVO> qryVersion(Long id) {
        List<IaasComponentHistoryVO> list = baseMapper.qryVersion(id);
        return list;
    }

    @Override
    public IaasComponentHistoryVO qryCompHisInfo(Long compHisId) {
        return baseMapper.qryCompHisInfo(compHisId);
    }

    @Override
    public IaasComponentHistoryVO qryUnionHistory(Long compHisId) {
        return baseMapper.qryUnionHistory(compHisId);
    }

    /**
     *
     * @param compHisVo 历史中查询出的
     * @param icvo //历史的翻新副本
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rollBack(IaasComponentHistoryVO compHisVo,IaasComponentVO icvo){
        BaseResponse baseResponse = new BaseResponse();
        //当前的组件入历史
        IaasComponentVO testVO = new IaasComponentVO();
        if(icvo.getId()!=null)
            testVO= componentService.qryUnionComponent(icvo.getId());//qryOneComponent
        Long testId=testVO.getId();

        QueryWrapper<IaasComponent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(IaasComponent::getId, testId);
        int count=componentService.count(queryWrapper);
        log.info("rollBack - count "+count);
        if(count>0){
            IaasComponent component =componentService.getById(testId);
            //塞入历史，添加关联记录
            IaasComponentHistory compHis=componentService.compHisInsert(component);
            log.info("updateComponent - compHis "+compHis);
//            if(!CollectionUtils.isEmpty(icvo.getCompScripts())) {
                componentScriptService.judgeAndSaveHistory(testVO, component, compHis);
                componentOperationService.judgeAndSaveHistory(testVO.getCompOpVOs(),component,compHis);
                componentScriptService.updateScript(component,baseResponse,icvo,testVO,icvo.getCompScripts());
//            }
//            if(!CollectionUtils.isEmpty(testVO.getCompOpVOs()))

//            if(!CollectionUtils.isEmpty(icvo.getCompParams())) {
                componentParamService.judgeAndSaveHistory(testVO.getCompParams(), component, compHis);
                componentParamService.updateParam(component,baseResponse,icvo,testVO,icvo.getCompParams());
//            }
            //组件更新
            int version = component.getVersion();
            BeanUtils.copyProperties(icvo, component);//new IaasComponent();
            component.setVersion(version+1);
            //修改人和时间
            component.setUpdateUser(Sign.getUserId());
            component.setUpdateTime(DateUtil.getNow());
            componentService.updateById(component);
        }
    }


}
