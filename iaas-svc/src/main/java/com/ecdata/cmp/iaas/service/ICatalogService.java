package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.catalog.IaasCatalog;
import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVO;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVO;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVirtualMachineTreeVO;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVO;
import com.ecdata.cmp.iaas.entity.dto.response.catalog.CatalogMachineComponentResponse;

import java.util.List;
import java.util.Map;

/**
 * 描述:服务目录
 *
 * @author xxj
 * @create 2019-11-24 10:49
 */
public interface ICatalogService {

    /**
     * 分页查询服务目录
     *
     * @param page
     * @param params
     * @return
     */
    IPage<IaasCatalogVO> queryIaasCatalog(Page<IaasCatalogVO> page, Map<String, Object> params);

    /**
     * 虚拟机 组件树状显示
     *
     * @param catalogId
     * @return
     */
    CatalogMachineComponentResponse queryMachineTree(Long catalogId);

    /**
     * 添加服务目录
     *
     * @param catalog
     * @return
     */
    void savaIaasCatalog(IaasCatalog catalog);

    /**
     * 选择虚拟机和模板参数
     *
     * @param catalogMachineComponent
     * @return
     */
    BaseResponse saveCatalogMachineComponent(CatalogMachineComponentResponse catalogMachineComponent);

    /**
     * 编辑虚拟机
     *
     * @param catalogMachineComponent
     * @return
     */
    BaseResponse editCatalogMachineComponent(CatalogMachineComponentResponse catalogMachineComponent);

    /**
     * 删除模板
     *
     * @param catalogId
     */
    void deleteCatalog(Long catalogId);

    /**
     * 更新服务目录
     *
     * @param catalog
     */
    void updateCatalog(IaasCatalog catalog);

    /**
     * 判断服务目录是否被申请
     * @param id
     * @return
     */
    List<IaasProcessApplyVO> checkCatalogIFUse(Long id);
}
