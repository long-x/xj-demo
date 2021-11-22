package com.ecdata.cmp.activiti.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xuxinsheng
 * @since 2020-02-21
 */
@Data
public class MyApplicationRequest implements Serializable {
    private static final long serialVersionUID = 1283649074840811467L;
    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页", example = "1")
    private Integer pageNo;
    /**
     * 每页的数量
     */
    @ApiModelProperty(value = "每页的数量", example = "20")
    private Integer pageSize;
    /**
     * 开始时间(yyyy-MM-dd HH:mm:ss)
     */
    @ApiModelProperty(value = "开始时间(yyyy-MM-dd HH:mm:ss)")
    private String startTime;
    /**
     * 结束时间(yyyy-MM-dd HH:mm:ss)
     */
    @ApiModelProperty(value = "结束时间(yyyy-MM-dd HH:mm:ss)")
    private String endTime;
    /**
     * 业务组id
     */
    @ApiModelProperty(value = "业务组id")
    private Long businessGroupId;
    /**
     * 业务组名称
     */
    @ApiModelProperty(value = "业务组名称")
    private String businessGroupName;
    /**
     * 流程实例id
     */
    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;
    /**
     * 自定义流程名
     */
    @ApiModelProperty(value = "自定义流程名")
    private String processName;
    /**
     * 自定义流程操作
     */
    @ApiModelProperty(value = "自定义流程操作")
    private String processOperation;
    /**
     * 自定义流程对象
     */
    @ApiModelProperty(value = "自定义流程对象")
    private String processObject;
    /**
     * 工单id
     */
    @ApiModelProperty(value = "工单id")
    private Long workorderId;
    /**
     * 业务详情(模糊查询使用)
     */
    @ApiModelProperty(value = "业务详情(模糊查询使用)")
    private String businessDetail;


}
