package com.ecdata.cmp.activiti.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xuxinsheng
 * @since 2020-01-10
*/
@Data
public class DeployProcessDTO implements Serializable {

    private static final long serialVersionUID = -43255190877528101L;
    /** 部署名称 */
    @ApiModelProperty(value = "部署名称")
    private String deploymentName;

    /** 1:bpmn和png; 2:zip */
    @ApiModelProperty(value = "1:bpmn和png; 2:zip")
    private Integer type;

    /** bpmn文件全名 */
    @ApiModelProperty(value = "(当type为1时填写)bpmn文件全名, 如workflow.bpmn")
    private String bpmnName;

    /** png图片全名 */
    @ApiModelProperty(value = "(当type为1时填写)png图片全名, 如workflow.png")
    private String pngName;

    /** zip压缩文件全名 */
    @ApiModelProperty(value = "(当type为2时填写)zip压缩文件全名, 如workflow.zip")
    private String zipName;

}
