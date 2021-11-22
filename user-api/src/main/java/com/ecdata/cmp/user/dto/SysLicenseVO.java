package com.ecdata.cmp.user.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @title: SysLicenseVO
 * @Author: shig
 * @description: 许可证拓展类
 * @Date: 2019/11/21 10:35 下午
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "许可证拓展类 对象", description = "许可证拓展类 对象")
public class SysLicenseVO implements Serializable {

    private static final long serialVersionUID = 3003985476863928299L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 是否已删除(0表示未删除,1表示已删除)
     */
    @ApiModelProperty(value = " 是否已删除(0表示未删除,1表示已删除)")
    private Boolean isDeleted;

    /**
     * 许可证内容
     */
    @ApiModelProperty(value = "许可证内容")
    private String content;


    //拓展字段属性
    /**
     * 许可证内容
     */
    @ApiModelProperty(value = "授权给")
    private String authorized;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "过期时间")
    private Date expireTime;

    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelProperty(value = "版本号")
    private String versionNum;

    @ApiModelProperty(value = "许可证密钥图片")
    private String licensePic;

}