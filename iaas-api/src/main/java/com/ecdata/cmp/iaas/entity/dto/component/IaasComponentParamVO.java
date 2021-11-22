package com.ecdata.cmp.iaas.entity.dto.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhaoYX
 * @since 2019/11/13 13:26,
 */

@Data
@ToString
@Accessors(chain = true)
@ApiModel(value = "组件参数", description = "组件参数")
public class IaasComponentParamVO implements Serializable {
    private static final long serialVersionUID = 6037220549060833021L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private Long tenantId;

    /**
     * 组件id
     */
    @ApiModelProperty("组件id")
    private Long componentId;

    /**
     * 参数名
     */
    @ApiModelProperty("参数名")
    private String paramName;

    /**
     * 显示名
     */
    @ApiModelProperty("显示名")
    private String displayName;

    /**
     * 默认值
     */
    @ApiModelProperty("默认值")
    private String defaultValue;

    /**
     * 参数类型(int;double;string;boolean;password;text)
     */
    @ApiModelProperty("参数类型(int;double;string;boolean;password;text)")
    private String paramType;

    /**
     * 0：非必须；1：必须
     */
    @ApiModelProperty("0：非必须；1：必须")
    private Boolean required;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String unit;

    /**
    参数值列表
     */
    @ApiModelProperty("参数值列表(如：[{'paramValue': '1','displayValue': '1', 'unit': '个', 'sort': 1, 'remark': 'remark'}])'")
    private String valueList;

    @ApiModelProperty("下拉框选择")
    private JSONArray valueSelect;

    @ApiModelProperty("配合前端树状")
    private Integer modifiable = 1;

    public static void main(String[] args) {
        String ss="[{\"paramValue\": \"1\",\"displayValue\": \"3\", \"unit\": \"个\", \"sort\": 1, \"remark\": \"remark\"},{\"paramValue\": \"2\",\"displayValue\": \"2\", \"unit\": \"双\", \"sort\": 1, \"remark\": \"remark\"},{\"paramValue\": \"3\",\"displayValue\": \"3\", \"unit\": \"双\", \"sort\": 1, \"remark\": \"remark\"},{\"paramValue\": \"4\",\"displayValue\": \"4\", \"unit\": \"双\", \"sort\": 1, \"remark\": \"remark\"},{\"paramValue\": \"5\",\"displayValue\": \"5\", \"unit\": \"双\", \"sort\": 1, \"remark\": \"remark\"}]";

        JSONArray array = JSON.parseArray(ss);

        System.out.println(array);
    }

//    /**
//     * 种类
//     */
//    @ApiModelProperty("种类(1:键唯一;2:键可有多个;3:组合键唯一;4:组合键可由多个;)")
//    private Boolean kind;
//
//    /**
//     * 父id
//     */
//    @ApiModelProperty("父id")
//    private Long parentId;
//
//    /**
//     * 可视级别
//     */
//    @ApiModelProperty("可视级别(1:全可视;2:唯建模不可视;3:建模和用户申请不可视;4:唯审批可视;)")
//    private Boolean level;
//
//    /**
//     * 参数名正则表达式
//     */
//    @ApiModelProperty("参数名正则表达式")
//    private String regex;
//
//    /**
//     * 同名互斥(0:不互斥;1:互斥;)
//     */
//    @ApiModelProperty("同名互斥(0:不互斥;1:互斥;)")
//    private Boolean mutex;

    /**
     * 备注
     */
    @ApiModelProperty("排序")
    private String sort;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("是否已删除(0表示未删除，1表示已正常)")
    private Boolean isDeleted;

    @ApiModelProperty("前端对应id接就行，不作处理")
    private String key;


//    @ApiModelProperty("组件参数值")
//    private List<IaasComponentParamValueVO> valueList;

}
