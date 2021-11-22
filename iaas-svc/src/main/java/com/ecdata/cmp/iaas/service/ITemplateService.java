package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.IaasTemplate;
import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVO;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVO;
import com.ecdata.cmp.iaas.entity.dto.response.template.TemplateVirtualMachineTreeResponse;

import java.util.List;
import java.util.Map;

public interface ITemplateService {

    /**
     * 分页查询模板
     *
     * @param page
     * @param params
     * @return
     */
    IPage<IaasTemplateVO> queryIaasTemplate(Page<IaasTemplateVO> page, Map<String, Object> params);

    /**
     * 更新模板状态
     *
     * @param template
     */
    void updateTemplate(IaasTemplate template);

    /**
     * 删除模板
     *
     * @param templateId
     */
    void deleteTemplate(Long templateId);

    /**
     * 保存模板
     *
     * @param template
     */
    void saveIaasTemplate(IaasTemplate template);

    /**
     * 树状格式保存 虚拟机、组件、组件参数、虚拟机磁盘信息
     *
     * @param vos
     */
    BaseResponse saveTemplateMachineComponent(List<TemplateVirtualMachineTreeResponse> vos);

    /**
     * 编辑模板，虚拟机，组件信息
     *
     * @param vos
     */
    BaseResponse editTemplate(List<TemplateVirtualMachineTreeResponse> vos);

    /**
     * 查询模板全部信息(虚拟机、虚拟机磁盘、组件、组件参数)
     *
     * @param templateVO
     * @return
     */
    IaasTemplateVO queryTemplateInfo(IaasTemplateVO templateVO);

    /**
     * 查询所有已发布模板信息，不分页
     *
     * @return
     */
    List<IaasTemplateVO> queryTemplate();

    /**
     * 查询虚拟机组件树状
     *
     * @return
     */
    List<TemplateVirtualMachineTreeResponse> queryMachineTree(Long templateId);

    /**
     * 判断模板是否被服务目录使用
     *
     * @param id
     * @return
     */
    List<IaasCatalogVO> checkTemplateIFUse(Long id);
}
