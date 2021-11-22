package com.ecdata.cmp.user.controller;

import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.BanTokenVO;
import com.ecdata.cmp.user.dto.BanUserVo;
import com.ecdata.cmp.user.dto.response.BanTokenResponse;
import com.ecdata.cmp.user.dto.response.BanUserResponse;
import com.ecdata.cmp.user.service.IBanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/6 16:16
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/oauth")
@Api(tags = "竹云-获取用户信息")
public class BanServiceController {

    @Autowired
    private IBanService iBanService;






    @GetMapping("/get_token")
    @ApiOperation(value = "获取竹云token", notes = "获取竹云token")
    @ApiImplicitParam(name = "code", value = "登录拿到code", paramType = "query", dataType = "string")
    public ResponseEntity<BanTokenResponse> getInfo(@RequestParam(required = false) String code) {
        BanTokenResponse banTokenResponse = new BanTokenResponse();
        BanTokenVO tokenVO = iBanService.getToken(code);
        banTokenResponse.setData(tokenVO);
        if (tokenVO.getErrcode() != null) {
            banTokenResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            banTokenResponse.setMessage("获取token失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(banTokenResponse);
        }
        banTokenResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        return ResponseEntity.status(HttpStatus.OK).body(banTokenResponse);
    }




    @GetMapping("/get_user_info")
    @ApiOperation(value = "获取竹云用户信息", notes = "获取竹云用户信息")
    @ApiImplicitParam(name = "token", value = "token", paramType = "query", dataType = "string")
    public ResponseEntity<BanUserResponse> getUserInfo(@RequestParam(required = false) String token) {

        BanUserResponse banUserResponse = new BanUserResponse();
        BanUserVo banUserVo = iBanService.getUserInfo(token);
        banUserResponse.setData(banUserVo);
        if (banUserVo.getErrcode() != null) {
            banUserResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            banUserResponse.setMessage("获取用户信息失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(banUserResponse);
        }
        banUserResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        return ResponseEntity.status(HttpStatus.OK).body(banUserResponse);

    }


    @GetMapping("/get_token_info")
    @ApiOperation(value = "查询token时效", notes = "查询token时效")
    @ApiImplicitParam(name = "accessToken", value = "token", paramType = "query", dataType = "string")
    public ResponseEntity<BanTokenResponse> getTokenInfo(@RequestParam(required = false) String accessToken) {
        BanTokenResponse banTokenResponse = new BanTokenResponse();
        BanTokenVO tokenVO = iBanService.getTokenInfo(accessToken);
        banTokenResponse.setData(tokenVO);
        if (tokenVO.getErrcode() != null) {
            banTokenResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            banTokenResponse.setMessage("获取token失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(banTokenResponse);
        }
        banTokenResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        return ResponseEntity.status(HttpStatus.OK).body(banTokenResponse);
    }




    @GetMapping("/refresh_token")
    @ApiOperation(value = "刷新token", notes = "刷新token")
    @ApiImplicitParam(name = "refreshToken", value = "refreshToken", paramType = "query", dataType = "string")
    public ResponseEntity<BanTokenResponse> refreshToken(@RequestParam(required = false) String refreshToken) {
        BanTokenResponse banTokenResponse = new BanTokenResponse();
        BanTokenVO tokenVO = iBanService.refreshToken(refreshToken);
        banTokenResponse.setData(tokenVO);
        if (tokenVO.getErrcode() != null) {
            banTokenResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            banTokenResponse.setMessage("获取token失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(banTokenResponse);
        }
        banTokenResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        return ResponseEntity.status(HttpStatus.OK).body(banTokenResponse);
    }


}
