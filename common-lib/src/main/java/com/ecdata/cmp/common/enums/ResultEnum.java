package com.ecdata.cmp.common.enums;

import lombok.Getter;


/**
 * 1位码：默认成功/失败；2位码：通用错误类型；3位码：http状态码一致；4位码即以上：业务错误
 *
 * @author xuxinsheng
 * @since 2019-09-11
 */
@Getter
public enum ResultEnum implements Code {

    /**
     * 成功
     */
    DEFAULT_SUCCESS(0, "成功"),

    /**
     * 失败
     */
    DEFAULT_FAIL(1, "失败"),
    /**
     * 参数缺少
     */
    PARAM_MISS(10, "缺少参数"),
    /**
     * 参数类型错误
     */
    PARAM_TYPE_ERROR(11, "参数类型错误"),
    /**
     * 参数校验错误
     */
    PARAM_VALID_ERROR(12, "参数校验错误"),
    /**
     * 参数绑定错误
     */
    PARAM_BIND_ERROR(13, "参数绑定错误"),
    /**
     * 信息不可读
     */
    MSG_NOT_READABLE(14, "信息不可读"),
    /**
     * 缺少主键
     */
    MISS_PRIMARY_KEY(15, "缺少主键"),
    /**
     * 对象已存在
     */
    EXIST_OBJECT(16, "对象已存在"),

    /**
     * 参数有误
     */
    BAD_PARAM(17, "参数不可用"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),
    /**
     * 禁止
     */
    FORBIDDEN(403, "禁止"),
    /**
     * 未找到
     */
    NOT_FOUND(404, "未找到"),
    /**
     * 方法不支持
     */
    METHOD_NOT_SUPPORTED(405, "方法不支持"),
    /**
     * 不支持媒体类型
     */
    UNSUPPORTED_MEDIA_TYPE(415, "不支持媒体类型"),
    /**
     * 内部服务错误
     */
    INTERNAL_SERVER_ERROR(500, "内部服务错误"),
    /**
     * 登录失败
     */
    LOGIN_FAIL(1000, "登录失败"),
    /**
     * 用户不存在
     */
    USER_NOT_FOUND(1001, "用户不存在"),
    /**
     * 密码错误
     */
    BAD_CREDENTIALS(1002, "密码错误"),
    /**
     * 缺少用户名密码
     */
    MISS_NAME_PASSWORD(1004, "缺少用户名密码"),
    /**
     * 用户已存在
     */
    EXIST_USER(1005, "用户已存在"),
    /**
     * 账户异常
     */
    BAD_ACCOUNT(1006, "账户异常"),
    /**
     * 账户异常
     */
    LOCKED_ACCOUNT(1007, "账户锁定"),
    /**
     * 账户异常
     */
    CONFIRM_PASSWORD(1008, "两次密码不一致"),
    /**
     * 账户设置修改密码
     */
    SAME_PASSWORD(1009, "新旧密码相同"),
    /**
     * 密码中必须包含字母、数字，至少8个字符，最多30个字符。
     */
    PWD_WRONG_FORMAT(1010,"密码长度8-16，必须包含大写字母、小写字母、数字、特殊字符:!@#$%^&*()"),
    /**
     * 令牌错误
     */
    TOKEN_ERROR(2000, "令牌错误"),
    /**
     * /**
     * 令牌到期
     */
    TOKEN_EXPIRES(2001, "令牌到期"),
    /**
     * 令牌认证失败
     */
    TOKEN_VERIFY_FAIL(2002, "令牌认证失败"),
    /**
     * 空令牌
     */
    TOKEN_EMPTY(2003, "空令牌"),
    /**
     * 令牌权限不足
     */
    TOKEN_FORBIDDEN(2003, "令牌权限不足"),
    /**
     * 角色权限有误
     */
    ROLE_ERROR(3000, "角色权限有误"),
    /**
     * 缺少菜单路径
     */
    MISS_PERMISSION_PATH(4000, "缺少菜单路径"),
    /**
     * 菜单路径已存在
     */
    EXIST_PERMISSION_PATH(4001, "菜单路径已存在"),
    /**
     * 容器部署名已存在
     */
    EXIST_CONTAINER_DEPLOYMENT_NAME(5000, "容器部署名已存在"),
    /**
     * 容器服务名已存在
     */
    EXIST_CONTAINER_SERVICE_NAME(5001, "容器服务名已存在"),
    /**
     * 容器路由名已存在
     */
    EXIST_CONTAINER_ROUTE_NAME(5002, "容器路由名已存在"),
    /**
     * 缺少挂载地址
     */
    MISS_MOUNT_PATH(5003, "缺少挂载地址"),
    /**
     * 挂载地址重复
     */
    DUPLICATE_MOUNT_PATH(5004, "挂载地址重复"),

    /**
     * api请求失败
     */
    FAIL_HTTP_UTIL_REQUEST(6001, "api请求失败"),

    /**
     * harbor项目已存在
     */
    EXIST_HARBOR_PROJECT(7001, "harbor项目已存在");

    /**
     * 码
     */
    private Integer code;

    /**
     * 信息
     */
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
