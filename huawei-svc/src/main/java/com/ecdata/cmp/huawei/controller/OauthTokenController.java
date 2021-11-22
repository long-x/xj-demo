package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.common.api.BooleanResponse;
import com.ecdata.cmp.common.api.StringResponse;
import com.ecdata.cmp.huawei.dto.response.TokenInfoResponse;
import com.ecdata.cmp.huawei.dto.token.OMToken;
import com.ecdata.cmp.huawei.dto.token.TokenInfoVO;
import com.ecdata.cmp.huawei.service.ManageOneService;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author ty
 * @description
 * @date 2019/11/20 14:35
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/ManageOne")
@Api(tags = "华为第三方登录认证")
public class OauthTokenController {
    /**
     * ManageOneService
     */
    @Autowired
    private ManageOneService manageOneService;

    @GetMapping("/manageOne1")
    @ApiOperation(value = "三方登录验证", notes = "三方登录验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "ipAdress", value = "ip地址", paramType = "query", dataType = "String")
    })
    public ResponseEntity<BooleanResponse> getTokenInfoByUser(@RequestParam(required = true) String userName,
                                                              @RequestParam(required = true) String password,
                                                              @RequestParam(required = false) String ipAdress) throws Exception {
        try {
            OMToken omToken = manageOneService.getOMToken(userName, password);
            return ResponseEntity.status(HttpStatus.OK).body(new BooleanResponse(StringUtils.hasText(omToken.getAccessSession())));
        } catch (Exception e) {
            log.info("get omToken error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).body(new BooleanResponse(false));
        }


    }

    @GetMapping("/getToken")
    @ApiOperation(value = "ManageOne获取Token", notes = "ManageOne获取Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "domainName", value = "租户名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目名", paramType = "query", dataType = "String")
    })
    public ResponseEntity<TokenInfoResponse> getTokenByUser(@RequestParam(required = true) String userName,
                                                            @RequestParam(required = true) String password,
                                                            @RequestParam(required = true) String domainName,
                                                            @RequestParam(required = false) String projectId) throws Exception {
        try {
            //获取运维面OM Token
            OMToken omToken = manageOneService.getOMToken("ecdataadmin", "Huawei@2020");
            //获取运营面MO Token
            String ocToken = manageOneService.getOCToken("ecdata_admin", "ecdata123!@#$", "mo_bss_admin", projectId);
            //获取运维面OM Web Token
            OMToken omTokenWeb = manageOneService.getOMTokenWeb("ecdataadmin", "Huawei@2020");
            TokenInfoVO tokenInfoVO = TokenInfoVO.builder().
                    ocToken(ocToken).
                    omToken(omToken).
                    omTokenWeb(omTokenWeb).
                    build();
            TokenInfoResponse tokenInfoResponse = new TokenInfoResponse();
            tokenInfoResponse.setData(tokenInfoVO);
            return ResponseEntity.status(HttpStatus.OK).body(tokenInfoResponse);
        } catch (Exception e) {
            log.info("get Token error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }




    @PutMapping("/getTokenNew")
    @ApiOperation(value = "ManageOne获取Token NEW", notes = "ManageOne获取Token NEW")
    public ResponseEntity<TokenInfoResponse> getTokenAll(@RequestBody IaasProviderVO providerVO ) throws Exception {
        try {
            //获取运维面OM Token
            OMToken omToken = manageOneService.getOMToken(providerVO.getOcInterfaceUsername(), providerVO.getOcInterfacePassword());
            //获取运营面MO Token
            String ocToken = manageOneService.getOCToken(providerVO.getUsername(), providerVO.getPassword(), providerVO.getDomainName(), "");
            //获取运维面OM Web Token
            OMToken omTokenWeb = manageOneService.getOMTokenWeb(providerVO.getOcInterfaceUsername(), providerVO.getOcInterfacePassword());
            TokenInfoVO tokenInfoVO = TokenInfoVO.builder().
                    ocToken(ocToken).
                    omToken(omToken).
                    omTokenWeb(omTokenWeb).
                    build();
            TokenInfoResponse tokenInfoResponse = new TokenInfoResponse();
            tokenInfoResponse.setData(tokenInfoVO);
            return ResponseEntity.status(HttpStatus.OK).body(tokenInfoResponse);
        } catch (Exception e) {
            log.info("get TokenNew error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }



    @GetMapping("/getTokenByVdcUser")
    @ApiOperation(value = "VDC获取Token", notes = "ManageOne获取Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "domainName", value = "租户名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目名", paramType = "query", dataType = "String")
    })
    public ResponseEntity<StringResponse> getTokenByVdcUser(@RequestParam(required = true) String userName,
                                                            @RequestParam(required = true) String password,
                                                            @RequestParam(required = true) String domainName,
                                                            @RequestParam(required = true) String projectId) throws Exception {

        try {
            String vdcToken = manageOneService.getOCToken(userName, password, domainName, projectId);
            return ResponseEntity.status(HttpStatus.OK).body(new StringResponse(vdcToken));
        } catch (Exception e) {
            log.info("get ocToken error");
            e.printStackTrace();
            StringResponse stringResponse = new StringResponse();
            stringResponse.setCode(401);
            stringResponse.setMessage("输入信息有误!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(stringResponse);
        }
    }

    @GetMapping("/CheckVdcUser")
    @ApiOperation(value = "验证VDC用户是否存在", notes = "验证VDC用户是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "domainName", value = "租户名", paramType = "query", dataType = "String")
    })
    public ResponseEntity<StringResponse> CheckVdcUser(@RequestParam(required = true) String userName,
                                                       @RequestParam(required = true) String password,
                                                        @RequestParam(required = true) String domainName) throws Exception {
        try {
            String vdcToken = manageOneService.getOCToken(userName, password, domainName, "");
            return ResponseEntity.status(HttpStatus.OK).body(new StringResponse(vdcToken));
        } catch (Exception e) {
            log.info("Check VdcUser error");
            e.printStackTrace();
            StringResponse stringResponse = new StringResponse();
            stringResponse.setCode(400);
            stringResponse.setMessage("该vdc用户不存在!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(stringResponse);
        }
    }
}
