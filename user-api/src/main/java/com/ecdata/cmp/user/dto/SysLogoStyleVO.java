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
 * @title: SysLogoStyleVO
 * @Author: shig
 * @description: 系统logo样式 拓展对象
 * @Date: 2019/11/22 2:01 下午
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "系统logo样式 对象", description = "系统logo样式 对象")
public class SysLogoStyleVO implements Serializable {
    /**
     * 主键
     */
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 游览器tab页面上名称
     */
    @ApiModelProperty(value = "游览器tab页面上名称")
    private String titleTab;

    /**
     * 系统名称
     */
    @ApiModelProperty(value = "系统名称")
    private String sysName;

    /**
     * 主题颜色
     */
    @ApiModelProperty(value = "主题颜色")
    private String objColor;

    /**
     * 辅助颜色
     */
    @ApiModelProperty(value = "辅助颜色")
    private String auxiliaryColor;

    /**
     * 登陆显示的系统名称
     */
    @ApiModelProperty(value = "登陆显示的系统名称")
    private String loginName;

    /**
     * 登陆帮助文字信息
     */
    @ApiModelProperty(value = "登陆帮助文字信息")
    private String loginInfo;

    /**
     * 登陆帮助信息链接
     */
    @ApiModelProperty(value = "登陆帮助信息链接")
    private String loginLink;

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
     * 系统logo图标
     */
    @ApiModelProperty(value = "系统logo图标")
    private String sysLogo;

    /**
     * favicon图标
     */
    @ApiModelProperty(value = "favicon图标")
    private String faviconLogo;

    /**
     * 登陆页logo
     */
    @ApiModelProperty(value = "登陆页logo")
    private String loginLogo;

    /**
     * 系统logo图标
     */
    @ApiModelProperty(value = "系统logo图标 图片名称")
    private String sysLogoPicName;

    /**
     * favicon图标
     */
    @ApiModelProperty(value = "favicon图标 图片名称")
    private String faviconLogoPicName;

    /**
     * 登陆页logo
     */
    @ApiModelProperty(value = "登陆页logo 图片名称")
    private String loginLogoPicName;

    /**
     * 版权所有
     */
    @ApiModelProperty(value = "版权所有")
    private String copyRight;

    /**
     * 版权所有
     */
    @ApiModelProperty(value = "许可证密钥图片")
    private String licensePic;

    /**
     * 登陆页logo 图片名称
     */
    @ApiModelProperty(value = "许可证密钥图片名称")
    private String licensePicName;

}