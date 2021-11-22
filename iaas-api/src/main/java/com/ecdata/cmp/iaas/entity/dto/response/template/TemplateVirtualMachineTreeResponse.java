package com.ecdata.cmp.iaas.entity.dto.response.template;

import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVirtualMachineDiskVO;
import lombok.Data;

import java.util.List;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-21 15:59
 */
@Data
public class TemplateVirtualMachineTreeResponse {
    private Long id;
    private Long parentId;
    private Long key;
    private String title;
    //模板id
    private Long templateId;
    //模板状态
    private Integer state;
    private Integer vcpu;
    private Integer memory;
    private String os;
    private String osType;
    private String username;
    private String password;
    private Integer sort;
    private List<IaasTemplateVirtualMachineDiskVO> machineDiskVOList;
    private List<TemplateVirtualMachineComponentTreeResponse> children;
}
