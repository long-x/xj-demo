package com.ecdata.cmp.user.controller;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.AES;
import com.ecdata.cmp.common.crypto.License;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.SysLicenseVO;
import com.ecdata.cmp.user.dto.response.SysLicenseResponse;
import com.ecdata.cmp.user.entity.SysLicense;
import com.ecdata.cmp.user.entity.SysLogoStyle;
import com.ecdata.cmp.user.service.ISysLicenseService;
import com.ecdata.cmp.user.service.ISysLogoStyleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @title: SysLicense controller
 * @Author: shig
 * @description: 许可证控制层
 * @Date: 2019/11/21 10:30 下午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/sysLicense")
@Api(tags = "🥕许可证相关的API")
public class SysLicenseController {

    /**
     * 许可证(SysLicense) service
     */
    @Autowired
    private ISysLicenseService sysLicenseService;

    /**
     * 系统logo样式(SysLogoStyle) service
     */
    @Autowired
    ISysLogoStyleService sysLogoStyleService;


    /**
     * 新增:/v1/sysLicense/add
     *
     * @param sysLicenseVO
     * @return BaseResponse
     * @throws Exception
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增许可证", notes = "新增许可证")
    public ResponseEntity<BaseResponse> add(@RequestBody SysLicenseVO sysLicenseVO) throws Exception {
        SysLicense sysLicense = new SysLicense();
        //保存sysLicense
        BeanUtils.copyProperties(sysLicenseVO, sysLicense);
        sysLicense.setId(SnowFlakeIdGenerator.getInstance().nextId());
        //createUser+time
        sysLicense.setCreateUser(Sign.getUserId());
        sysLicense.setCreateTime(DateUtil.getNow());
        sysLicense.setUpdateTime(DateUtil.getNow());

        //响应基础对象
        BaseResponse baseResponse = null;
        //新增：成功、失败
        if (sysLicenseService.save(sysLicense)) {
            baseResponse = new BaseResponse();
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse = new BaseResponse();
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    /**
     * 修改方法
     *
     * @param sysLicenseVO
     * @return
     * @throws Exception
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改许可证", notes = "修改许可证")
    public ResponseEntity<BaseResponse> update(@RequestBody SysLicenseVO sysLicenseVO) throws Exception {
        //许可证对象
        SysLicense sysLicense = new SysLicense();
        BeanUtils.copyProperties(sysLicenseVO, sysLicense);
        //响应失败，缺少主键
        if (sysLicense.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.MISS_PRIMARY_KEY));
        }
        //update user+time
        sysLicense.setUpdateUser(Sign.getUserId());
        sysLicense.setUpdateTime(DateUtil.getNow());

        //响应
        BaseResponse baseResponse = null;
        if (sysLicenseService.updateById(sysLicense)) {
            baseResponse = new BaseResponse();
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse = new BaseResponse();
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    /**
     * 新增:/v1/sysLicense/contextDecrypt
     *
     * @return BaseResponse
     * @throws Exception
     */
    @GetMapping("/getLastTimeSysLicense")
    @ApiOperation(value = "获取最新许可证", notes = "获取最新许可证信息")
    public ResponseEntity<SysLicenseResponse> getLastTimeSysLicense() throws Exception {
        SysLicenseResponse sysLicenseResponse = new SysLicenseResponse();
        SysLicenseVO sysLicenseVO = new SysLicenseVO();
        //此案例，防止数据清空条件下报空指针
//        String context = "d68a7719723ae1999b441982435d0a83ba63900a336cf34314f55b13c7e2d842ea886520f4c6fcb4ddf2856ab610fdbde935f9c75b54de2d17b8ea63a533529f";
        //查询最近一次密钥
        SysLicense sysLicense = sysLicenseService.getLastTimeSysLicense();
        //从：logo表取,sys_logo_style.login_name
        SysLogoStyle logoStyle = sysLogoStyleService.getLastTimeSysLogoStyle();
        if (sysLicense != null) {
            //解码
            License license = AES.decryptLicense(sysLicense.getContent() == null ? "" : sysLicense.getContent());
            sysLicenseVO.setVersionNum(license.getVersion());
            sysLicenseVO.setVersion("1".equals(license.getProd()) ? "测试版" : "企业版");
            sysLicenseVO.setExpireTime(license.getExpire());
            sysLicenseVO.setAuthorized(logoStyle == null ? "诚投集团" : (logoStyle.getLoginName() == null ? "诚投集团" : logoStyle.getLoginName()));
            sysLicenseVO.setLicensePic(logoStyle.getLicensePic() == null ? "获取图片失败" : logoStyle.getLicensePic());
            BeanUtils.copyProperties(sysLicense, sysLicenseVO);
            sysLicenseResponse.setData(sysLicenseVO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(sysLicenseResponse);
    }

}