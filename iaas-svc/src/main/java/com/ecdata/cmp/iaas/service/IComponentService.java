package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.component.IaasComponent;
import com.ecdata.cmp.iaas.entity.component.IaasComponentHistory;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface IComponentService extends IService<IaasComponent> {

    void modifyUpdateRecord(Long id, Long userId);

    List<IaasComponentVO> qryComponentInfo(Long componentId);

//    IPage<IaasComponentVO> qryComponentInfo(Page page);

    IaasComponentVO qryComponentById(Long id);

    BaseResponse updateComponent(IaasComponentVO componentVo,BaseResponse baseResponse) throws Exception;

//    List<IaasComponentVO> qryComponents();

    IPage<IaasComponentVO> queryPage(Page page,Wrapper<IaasComponent> queryWrapper);

//    IaasComponentVO qryOneComponent( Long id);

    IaasComponentVO qryUnionComponent( Long id);

    boolean add(IaasComponentVO componentVo);

    IaasComponentHistory compHisInsert(IaasComponent component);

}
