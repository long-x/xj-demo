package com.ecdata.cmp.iaas.mapper.catalog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.catalog.IaasCatalog;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVO;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVirtualMachineTreeVO;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface IaasCatalogMapper extends BaseMapper<IaasCatalog> {

    IPage<IaasCatalogVO> queryIaasCatalog(Page<IaasCatalogVO> page, @Param("map") Map<String, Object> params);

    void updateIaasCatalog(IaasCatalog catalog);

    List<IaasCatalogVirtualMachineTreeVO> queryMachineTree(@Param("catalogId") Long catalogId);

    IaasCatalogVO queryIaasCatalogById(@Param("catalogId") Long catalogId);

    /**
     * 判断申请服务是否
     * @param id
     * @return
     */
    List<IaasProcessApplyVO> checkCatalogIFUse(Long id);
}
