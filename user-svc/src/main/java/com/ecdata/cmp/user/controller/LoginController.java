package com.ecdata.cmp.user.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecdata.cmp.common.api.StringResponse;
import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.common.config.TenantProperties;
import com.ecdata.cmp.common.constant.Constants;
import com.ecdata.cmp.common.crypto.Hash;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.user.dto.BanTokenVO;
import com.ecdata.cmp.user.dto.BanUserVo;
import com.ecdata.cmp.user.entity.User;
import com.ecdata.cmp.user.service.IBanService;
import com.ecdata.cmp.user.service.IUserService;
import com.ecdata.cmp.user.utils.RSAUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-03-28
 * 通过 Spring Cloud 原生注解 @RefreshScope 实现配置自动更新
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/login")
@Api(tags = "登入相关的API")
public class LoginController {

    @Value("${private.key}")
    private String privateKeys;
    /**
     * 租户参数
     */
    @Autowired
    private TenantProperties tenantProperties;
    /**
     * userService
     */
    @Autowired
    private IUserService userService;


    @Autowired
    private IBanService iBanService;


    @PostMapping
    @ApiOperation(value = "用户登入", notes = "根据用户名密码登入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "code", value = "竹云code", paramType = "query", dataType = "string")
    })
    public ResponseEntity<StringResponse> login(HttpServletRequest request, HttpServletResponse response) {
        String token;
        String username;
        String password;
        String tenantId;
        try {
            String body = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
            String usernameParameter = "username";
            String passwordParameter = "password";
            String tenantIdParameter = "tenantId";
            String codeParameter = "code";
            if (StringUtils.hasText(body)) {
                JSONObject jsonBody = JSON.parseObject(body);
                username = jsonBody.getString(usernameParameter);
                password = jsonBody.getString(passwordParameter);
                tenantId = jsonBody.getString(tenantIdParameter);
            } else {
                // 表单提交时候获取数据
                username = request.getParameter(usernameParameter);
                password = request.getParameter(passwordParameter);
                tenantId = request.getParameter(tenantIdParameter);
            }

            username = username == null ? "" : username.trim();
            password = password == null ? "" : password;
            tenantId = tenantId == null ? String.valueOf(tenantProperties.getDefaultId()) : tenantId.trim();
            //密码解密
            PrivateKey privateKey = RSAUtil.getPrivateKey(privateKeys);
            password = RSAUtil.decryptString(privateKey, password);

            Sign.setCurrentTenantId(Long.valueOf(tenantId));

            User user;

            if ("sysadmin".equals(username)) {
                user = userService.getSysAdmin();
                if (user == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StringResponse(ResultEnum.USER_NOT_FOUND));
                }
            } else {
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(User::getName, username);
                List<User> userList = userService.list(queryWrapper);
                if (userList != null && userList.size() > 0) {
                    if (userList.size() > 1) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StringResponse(ResultEnum.BAD_ACCOUNT));
                    }
                    user = userList.get(0);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StringResponse(ResultEnum.USER_NOT_FOUND));
                }
            }

            if (user.getStatus() == Constants.TWO) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StringResponse(ResultEnum.LOCKED_ACCOUNT));
            }

            if (!Hash.encode(password).equals(user.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StringResponse(ResultEnum.BAD_CREDENTIALS));
            }
            String loginName = user.getName() == null ? user.getDisplayName() : user.getName();
            String displayName = user.getDisplayName() == null ? user.getName() : user.getDisplayName();
            token = Sign.generateUserToken(user.getId(), user.getTenantId(), loginName, displayName, user.getEmail());
            response.addHeader(AuthConstant.AUTHORIZATION_HEADER, token);
            user.setLastLoginTime(DateUtil.getNow()).updateById();

        } catch (Exception ex) {
            log.debug("登入异常");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StringResponse(ResultEnum.LOGIN_FAIL));
        } finally {
            Sign.removeCurrentTenantId();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new StringResponse(ResultEnum.DEFAULT_SUCCESS, token));
    }

}
