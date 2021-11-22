package com.ecdata.cmp.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/6 15:49
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "竹云用户信息", description = "竹云用户信息")
public class BanUserVo implements Serializable {

    private static final long serialVersionUID = -2544370947536971916L;

    @ApiModelProperty(value = "country")
    private String country;

    @ApiModelProperty(value = "sorgId")
    private String sorgId;

    @ApiModelProperty(value = "updateDate")
    private String updateDate;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "gender")
    private String gender;

    @ApiModelProperty(value = "loginType")
    private String loginType;

    @ApiModelProperty(value = "nation")
    private String nation;

    @ApiModelProperty(value = "displayName")
    private String displayName;

    @ApiModelProperty(value = "positionNumber")
    private String positionNumber;

    @ApiModelProperty(value = "title")
    private String title;

    @ApiModelProperty(value = "loginInterceptFlagTwo")
    private String loginInterceptFlagTwo;

    @ApiModelProperty(value = "loginInterceptFlagFour")
    private String loginInterceptFlagFour;

    @ApiModelProperty(value = "employeeNumber")
    private String employeeNumber;

    @ApiModelProperty(value = "changePwdAt")
    private String changePwdAt;

    @ApiModelProperty(value = "spRoleList")
    private String  spRoleList;

    @ApiModelProperty(value = "uid")
    private long uid;

    @ApiModelProperty(value = "pwdPolicy")
    private String pwdPolicy;

    @ApiModelProperty(value = "loginInterceptFlagFive")
    private String loginInterceptFlagFive;

    @ApiModelProperty(value = "loginInterceptFlagThree")
    private String loginInterceptFlagThree;

    @ApiModelProperty(value = "identityNumber")
    private String identityNumber;

    @ApiModelProperty(value = "identityType")
    private String identityType;

    @ApiModelProperty(value = "loginName")
    private String loginName;

    @ApiModelProperty(value = "secAccValid")
    private String secAccValid;

    @ApiModelProperty(value = "pinyinShortName")
    private String pinyinShortName;

    @ApiModelProperty(value = "orgNumber")
    private String orgNumber;

    @ApiModelProperty(value = "wechatNo")
    private String wechatNo;

    @ApiModelProperty(value = "orgNamePath")
    private String orgNamePath;

    @ApiModelProperty(value = "passwordModifyRequired")
    private String passwordModifyRequired;

    @ApiModelProperty(value = "birthDay")
    private String birthDay;

    @ApiModelProperty(value = "givenName")
    private String givenName;

    @ApiModelProperty(value = "mobile")
    private String mobile;

    @ApiModelProperty(value = "loginInterceptFlagOne")
    private String loginInterceptFlagOne;

    @ApiModelProperty(value = "certSn")
    private String certSn;

    @ApiModelProperty(value = "employeeType")
    private String employeeType;

    @ApiModelProperty(value = "orgCodePath")
    private String orgCodePath;

    @ApiModelProperty(value = "otpKey")
    private String otpKey;

    @ApiModelProperty(value = "positionStatus")
    private String positionStatus;

    @ApiModelProperty(value = "departmentNumber")
    private String departmentNumber;

    @ApiModelProperty(value = "certDn")
    private String certDn;

    @ApiModelProperty(value = "spNameList")
    private String [] spNameList;

    @ApiModelProperty(value = "isPassRemind")
    private String isPassRemind;

    @ApiModelProperty(value = "errcode")
    private String errcode;

    @ApiModelProperty(value = "msg")
    private String msg;












}
