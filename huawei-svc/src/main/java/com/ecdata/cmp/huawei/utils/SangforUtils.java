package com.ecdata.cmp.huawei.utils;

import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
@Component
public class SangforUtils {

    @Value("${sangfor.url}")
    private String url;

    @Value("${sangfor.username}")
    private String username;

    @Value("${sangfor.password}")
    private String password;

    @Value("${sangfor.party}")
    private String party;

    @Value("${sangfor.platformName}")
    private String platformName;

    public String getUrl() {
        return this.url;
    }

    public String getToken() {
        final String url = this.url + "/sangforinter/v1/auth/party/login";

        List<String> list = new ArrayList<>();

        JSONObject requestBody = buildLoginJson();
        Optional.ofNullable(requestBody).ifPresent(e -> {
            log.info("getToken, url={}, params={}", url, requestBody);
            try {
                String response = BaseOkHttpClientUtil.post(url, e, new HashMap<>());
                log.info("getToken response = {}", response);
                Optional.ofNullable(response).ifPresent(t -> {
                    JSONObject r = JSONObject.parseObject(t);
                    Optional.ofNullable(r.getJSONObject("data")).ifPresent(m -> {
                        String token = m.getString("token");
                        log.info("getToken, token={}", token);
                        list.add(token);
                    });
                });

            } catch (IOException ex) {
                log.error("get token error={}", e);
            }
        });

        String token = CollectionUtils.isNotEmpty(list) ? list.get(0) : StringUtils.EMPTY;
        log.info("getToken, result={}", token);
        return token;
    }

    private JSONObject buildLoginJson() {
        try {
            int rand = new Random().nextInt();

            // SHA-1加密后，转HEX编码
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digests = md.digest((rand + password + party + username).getBytes());
            String auth = HexUtils.toHexString(digests);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rand", rand);
            jsonObject.put("userName", username);
            jsonObject.put("clientProduct", "");
            jsonObject.put("clientId", 0);
            jsonObject.put("clientVersion", "");
            jsonObject.put("desc", "");
            jsonObject.put("auth", auth);
            jsonObject.put("password", password);
            jsonObject.put("platformName", platformName);

            log.info("sangfor login params={}", jsonObject);

            return jsonObject;
        } catch (NoSuchAlgorithmException e) {
            log.error("buildLoginJson error " + e);
        }

        log.info("buildLoginJson gets nothing");
        return null;
    }


}
