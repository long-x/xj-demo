package com.ecdata.cmp.iaas.entity.dto.request;

import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyConfigInfoVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyNetworkAskVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyResourceVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyServiceSecurityResourcesVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/3/3 15:29
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "iaas流程申请表", description = "iaas流程申请表")
public class IaasApplyRequest {

    @ApiModelProperty("资源申请")
    private IaasApplyResourceVO iaasApplyResourceVO;

    @ApiModelProperty("配置信息")
    private IaasApplyConfigInfoVO iaasApplyConfigInfoVO;

    @ApiModelProperty("服务安全资源")
    private IaasApplyServiceSecurityResourcesVO iaasApplyServiceSecurityResourcesVO;

    @ApiModelProperty("网络要求")
    private List<IaasApplyNetworkAskVO> iaasApplyNetworkAskVO;

    @ApiModelProperty("配置id：变更回收用")
    private Long configId;

    @ApiModelProperty("类型：变更回收用")
    private String type;

    /**
     * 获取申请虚拟机数量
     *
     * @return
     */
    public int vmNum() {
        if (this.iaasApplyConfigInfoVO == null) {
            return 0;
        }
        return this.iaasApplyConfigInfoVO.getVmNum();
    }

    /**
     * 获取申请类型
     *
     * @return
     */
    public Integer state() {
        if (this.iaasApplyResourceVO == null) {
            return null;
        }
        return this.iaasApplyResourceVO.getState();
    }

    public Long applyId() {
        if (this.iaasApplyResourceVO == null) {
            return null;
        }
        return this.iaasApplyResourceVO.getId();
    }

    public Long businessGroupId() {
        if (this.iaasApplyResourceVO == null) {
            return null;
        }
        return this.iaasApplyResourceVO.getBusinessGroupId();
    }
}
