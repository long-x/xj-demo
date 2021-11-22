package com.ecdata.cmp.iaas.entity.dto.response.catalog;

import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVO;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVirtualMachineTreeVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 描述:服务目录信息 虚拟机组件树状信息
 *
 * @author xxj
 * @create 2019-11-21 15:59
 */
@Data
public class CatalogMachineComponentResponse {
    @ApiModelProperty(value = "服务目录信息")
    private IaasCatalogVO iaasCatalogVO;

    private List<IaasCatalogVirtualMachineTreeVO> catalogVirtualMachinVOList;
}
