package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.IaasTemplate;
import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVO;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVO;
import com.ecdata.cmp.iaas.entity.dto.response.template.TemplateVirtualMachineTreeResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xxj
 */
@Mapper
@Repository
public interface IaasTemplateMapper extends BaseMapper<IaasTemplate> {
    IPage<IaasTemplateVO> queryIaasTemplate(Page<IaasTemplateVO> page, @Param("map") Map<String, Object> params);

    void updateTemplate(IaasTemplate iaasTemplate);

    IaasTemplateVO queryTemplateInfo(IaasTemplateVO templateVO);

    List<TemplateVirtualMachineTreeResponse> queryMachineTree(@Param("templateId") Long templateId);

    List<IaasTemplateVO> queryTemplate();

    List<IaasCatalogVO> checkTemplateIFUse(Long id);

    List<IaasTemplateVO> qryVMTreeInfo();

}
