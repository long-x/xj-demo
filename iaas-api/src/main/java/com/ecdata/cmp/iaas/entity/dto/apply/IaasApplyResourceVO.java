package com.ecdata.cmp.iaas.entity.dto.apply;

import com.ecdata.cmp.iaas.entity.dto.file.FileVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/3/2 14:45
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "资源申请", description = "资源申请")
public class IaasApplyResourceVO {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("业务组id（前端对应项目名称）")
    private Long businessGroupId;

    @ApiModelProperty("业务组Name")
    private String businessGroupName;

    @ApiModelProperty("业务组关联的流程id")
    private Long businessActivitiId;

    @ApiModelProperty("业务组关联的流程名称")
    private String businessActivitiName;

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty("集团流程实例id")
    private String groupProcessInstanceId;

    @ApiModelProperty("流程状态(0:初始化;1:开始流程;2:流程处理中;3:流程结束;4:放弃流程;)")
    private Integer state;

    @ApiModelProperty("vdcName")
    private String vdcName;

    @ApiModelProperty("vdcid")
    private Long vdcId;

    @ApiModelProperty("项目大类id")
    private Long projectId;

    @ApiModelProperty("是否项目")
    private String isProject;

    @ApiModelProperty("项目大类名称")
    private String projectName;

    @ApiModelProperty("是否现网环境")
    private String isCurrentNetworkEvn;

    @ApiModelProperty("是否报送集团")
    private String isSubmitGroup;

    @ApiModelProperty("紧急度:(0.一般，1.紧急)")
    private String urgency;

    @ApiModelProperty("其他要求")
    private String remark;

    @ApiModelProperty("预留字段1")
    private String reservedFieldOne;

    @ApiModelProperty("预留字段2")
    private String reservedFieldTwo;

    @ApiModelProperty("预留字段3")
    private String reservedFieldThree;

    @ApiModelProperty("base64Pdf")
    private String base64Pdf;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除) )comment 资源申请表 engine = innodb;create index index_resource_apply_is_deleted on resource_apply (is_deleted)")
    private boolean isDeleted;

    @ApiModelProperty("租期")
    private String lease;

    @ApiModelProperty("操作类型：0：申请，1，回收；默认0")
    private String operationType;

    @ApiModelProperty("执行时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String executeTime;

    @ApiModelProperty("是否备份")
    private String isBack;

    @ApiModelProperty("备份要求")
    private String backupRequest;

    @ApiModelProperty("是否方案支持:0不支持，1支持")
    private String isProgramSupport;

    @ApiModelProperty("pdfurl")
    private String pdfUrl;

    @ApiModelProperty("上云备案号")
    private String  cloudNumber;

    @ApiModelProperty("资源信息")
    private List<IaasApplyConfigInfoVO> iaasApplyConfigInfoVOList;

    @ApiModelProperty("计算资源")
    private List<IaasApplyCalculateVO> iaasApplyCalculateVOS;

    @ApiModelProperty("网络策略信息")
    private List<IaasApplyNetworkPolicyVO> iaasApplyNetworkPolicyVOS;

    @ApiModelProperty("安全服务信息")
    private List<IaasApplySecurityServerVO> iaasApplySecurityServerVOS;

    @ApiModelProperty("存储信息表")
    private List<IaasApplyStorageVO> iaasApplyStorageVOS;

    @ApiModelProperty("软件服务")
    private List<IaasApplySoftwareServerVO> iaasApplySoftwareServerVOS;

    @ApiModelProperty("其他信息")
    private List<IaasApplyOtherVO> iaasApplyOtherVOS;

    @ApiModelProperty("文件显示用")
    private List<FileVo> fileVos;

    @ApiModelProperty("审批文件显示用")
    private List<FileVo> applyFileVos;

    @ApiModelProperty("当前流程节点")
    private String processNode;

    @ApiModelProperty("当前流程")
    private String processState;

    @ApiModelProperty("taskId")
    private String taskId;

    @ApiModelProperty("用户ID")
    private Long userId;
}
