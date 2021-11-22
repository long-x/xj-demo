package com.ecdata.cmp.common.crypto;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.error.ServiceException;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author honglei
 * @since 2019-08-19
 */
public class Sign {
    /**
     * signingToken
     */
    public static final String SIGNING_TOKEN = "cci-signing-token-55606c72-62b1-4e0b-abc7-96767a20401e";
    /**
     * email
     */
    public static final String CLAIM_EMAIL = "email";
    /**
     * email
     */
    public static final String CLAIM_TENANT_ID = "tenantId";
    /**
     * userId
     */
    public static final String CLAIM_USER_ID = "userId";
    /**
     * userDisplayName
     */
    public static final String CLAIM_USER_DISPLAY_NAME = "userDisplayName";
    /**
     * userLoginName
     */
    public static final String CLAIM_USER_LOGIN_NAME = "userLoginName";
    /**
     * role:support
     */
    public static final String CLAIM_SUPPORT = "support";
    /**
     * duration the duration：Expiration time
     */
    public static final Long DURATION = 12L;
    /**
     * 缓存检验
     */
    private static Map<String, JWTVerifier> VERIFIER_MAP = new HashMap<>();
    /**
     * 缓存算法
     */
    private static Map<String, Algorithm> ALGORITHM_MAP = new HashMap<>();

    /**
     * 当前租户id key
     */
    private static final String KEY_CURRENT_TENANT_ID = "KEY_CURRENT_TENANT_ID";

    /**
     * 当前用户id key
     */
    private static final String KEY_CURRENT_USER_ID = "KEY_CURRENT_USER_ID";
    /**
     * 上下文map
     */
    private static final Map<String, Object> MAP_CONTEXT = new ConcurrentHashMap<>();

    /**
     * 如果构造方法是protected的，则建议在构造方法里面抛出异常，以防止子类被实例化
     */
    protected Sign() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    /**
     * @param signingToken signingToken(the secret to use in the verify or signing instance.)
     * @return algorithm
     */
    private static Algorithm getAlgorithm(String signingToken) {
        Algorithm algorithm = ALGORITHM_MAP.get(signingToken);
        if (algorithm == null) {
            synchronized (ALGORITHM_MAP) {
                algorithm = ALGORITHM_MAP.get(signingToken);
                if (algorithm == null) {
                    algorithm = Algorithm.HMAC512(signingToken);
                    ALGORITHM_MAP.put(signingToken, algorithm);
                }
            }
        }
        return algorithm;
    }

    /**
     * @param userId       userID
     * @param email        email
     * @param signingToken token
     * @return token
     */
    public static String generateEmailConfirmationToken(String userId, String email, String signingToken) {
        Algorithm algorithm = getAlgorithm(signingToken);
        String token = JWT.create()
                .withClaim(CLAIM_EMAIL, email)
                .withClaim(CLAIM_USER_ID, userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(DURATION)))
                .sign(algorithm);
        return token;
    }

    /**
     * @param userId          用户ID
     * @param tenantId        租户Id
     * @param userDisplayName 用户显示名
     * @return token
     */
    public static String generateUserToken(Long userId, Long tenantId, String loginName, String userDisplayName, String email) {
        Algorithm algorithm = getAlgorithm(SIGNING_TOKEN);
        String token = JWT.create()
                .withClaim(CLAIM_TENANT_ID, tenantId)
                .withClaim(CLAIM_USER_ID, userId)
                .withClaim(CLAIM_USER_LOGIN_NAME, loginName)
                .withClaim(CLAIM_USER_DISPLAY_NAME, userDisplayName)
                .withClaim(CLAIM_EMAIL, email)
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(DURATION)))
                .sign(algorithm);
        return token;
    }

    /**
     * @param tokenString token
     * @return DecodedJWT
     */
    public static DecodedJWT verifyUserToken(String tokenString) {
        return verifyToken(tokenString, SIGNING_TOKEN);
    }

    /**
     * @param tokenString  token
     * @param signingToken token
     * @return DecodedJWT
     */
    public static DecodedJWT verifyEmailConfirmationToken(String tokenString, String signingToken) {
        return verifyToken(tokenString, signingToken);
    }

    /**
     * @param tokenString  token
     * @param signingToken token
     * @return DecodedJWT
     */
    public static DecodedJWT verifySessionToken(String tokenString, String signingToken) {
        return verifyToken(tokenString, signingToken);
    }

    /**
     * @param tokenString  token
     * @param signingToken secret
     * @return DecodedJWT
     */
    static DecodedJWT verifyToken(String tokenString, String signingToken) {
        JWTVerifier verifier = VERIFIER_MAP.get(signingToken);
        if (verifier == null) {
            synchronized (VERIFIER_MAP) {
                verifier = VERIFIER_MAP.get(signingToken);
                if (verifier == null) {
                    Algorithm algorithm = Algorithm.HMAC512(signingToken);
                    verifier = JWT.require(algorithm).build();
                    VERIFIER_MAP.put(signingToken, verifier);
                }
            }
        }

        DecodedJWT jwt = verifier.verify(tokenString);
        return jwt;
    }

    /**
     * a Json Web Token
     * <p>
     *
     * @param userId       user id
     * @param signingToken signingToken(secret)
     * @param support      角色
     * @param duration     过期时间
     * @return token
     */
    public static String generateSessionToken(String userId, String signingToken, boolean support, long duration) {
        if (StringUtils.isEmpty(signingToken)) {
            throw new ServiceException("No signing token present");
        }
        Algorithm algorithm = getAlgorithm(signingToken);
        String token = JWT.create()
                .withClaim(CLAIM_USER_ID, userId)
                .withClaim(CLAIM_SUPPORT, support)
                .withExpiresAt(new Date(System.currentTimeMillis() + duration))
                .sign(algorithm);
        return token;
    }

    public static Long getUserId() {
        Object obj = MAP_CONTEXT.get(KEY_CURRENT_USER_ID);
        if (obj == null) {
            DecodedJWT jwt = Sign.verifyUserToken(AuthContext.getAuthz());
            return jwt.getClaim(Sign.CLAIM_USER_ID).asLong();
        } else {
            return (Long) obj;
        }
    }

    public static String getEmail() {
        DecodedJWT jwt = Sign.verifyUserToken(AuthContext.getAuthz());
        return jwt.getClaim(Sign.CLAIM_EMAIL).asString();
    }

    public static void setCurrentUserId(Long userId) {
        MAP_CONTEXT.put(KEY_CURRENT_USER_ID, userId);
    }

    public static void removeCurrentUserId() {
        if (MAP_CONTEXT.get(KEY_CURRENT_USER_ID) != null) {
            MAP_CONTEXT.remove(KEY_CURRENT_USER_ID);
        }
    }

    public static Long getTenantId() {
        Object obj = MAP_CONTEXT.get(KEY_CURRENT_TENANT_ID);
        if (obj == null) {
            DecodedJWT jwt = Sign.verifyUserToken(AuthContext.getAuthz());
            return jwt.getClaim(Sign.CLAIM_TENANT_ID).asLong();
        } else {
            return (Long) obj;
        }
    }

    public static void setCurrentTenantId(Long tenantId) {
        MAP_CONTEXT.put(KEY_CURRENT_TENANT_ID, tenantId);
    }

    public static void removeCurrentTenantId() {
        if (MAP_CONTEXT.get(KEY_CURRENT_TENANT_ID) != null) {
            MAP_CONTEXT.remove(KEY_CURRENT_TENANT_ID);
        }
    }

    public static String getUserDisplayName() {
        DecodedJWT jwt = Sign.verifyUserToken(AuthContext.getAuthz());
        return jwt.getClaim(Sign.CLAIM_USER_DISPLAY_NAME).asString();
    }
}
