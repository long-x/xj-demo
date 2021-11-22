package com.ecdata.cmp.iaas.entity.dto;

import com.ecdata.cmp.iaas.entity.dto.apply.ApplyAreaUpdateVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyResourceVO;
import com.ecdata.cmp.iaas.entity.dto.file.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/2/27 14:29
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "申批时参数", description = "申批时参数")
public class ApplyInfoVO {

    @ApiModelProperty("申请资源")
    private IaasApplyResourceVO applyResourceVO;

    @ApiModelProperty("提交oa")
    private String submitOa;

    @ApiModelProperty("类型：1.纳管；2.取消纳管")
    private String type;

    @ApiModelProperty("base64Pdf")
    private String base64Pdf;

    @ApiModelProperty("pdfUrl")
    private String pdfUrl;

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty("流程实例步骤")
    private Integer processWorkflowStep;

    @ApiModelProperty("taskId")
    private String taskId;

    @ApiModelProperty(value = "结果")
    private String outcome;

    @ApiModelProperty(value = "意见")
    private String comment;

    @ApiModelProperty(value = "用户id(调动完成任务时候，如果不传这个参数，自动从token中获取)")
    private Long userId;

    @ApiModelProperty(value = "用户显示名(与用户id称配套)")
    private String userDisplayName;

    @ApiModelProperty(value = "更新区域")
    private List<ApplyAreaUpdateVO> areaVOList;

    @ApiModelProperty("审批文件显示用")
    private List<FileVo> applyFileVos;

    public Long applyId() {
        if (this == null || this.applyResourceVO == null) {
            return null;
        }

        return this.applyResourceVO.getId();
    }

    public Long projectId() {
        if (this == null || this.applyResourceVO == null) {
            return null;
        }

        return this.applyResourceVO.getProjectId();
    }

    public String isSubmitGroup() {
        if (this == null || this.applyResourceVO == null) {
            return null;
        }

        return this.applyResourceVO.getIsSubmitGroup();
    }

    public String groupProcessInstanceId() {
        if (this == null || this.applyResourceVO == null) {
            return null;
        }

        return this.applyResourceVO.getGroupProcessInstanceId();
    }

    public String operationType() {
        if (this == null || this.applyResourceVO == null) {
            return null;
        }

        return this.applyResourceVO.getOperationType();
    }

    public Long businessGroupId() {
        if (this == null || this.applyResourceVO == null) {
            return null;
        }

        return this.applyResourceVO.getBusinessGroupId();
    }
}
