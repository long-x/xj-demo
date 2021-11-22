package com.ecdata.cmp.apigateway.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.apigateway.entity.LogValidity;
import com.ecdata.cmp.apigateway.entity.response.TokenVO;
import com.ecdata.cmp.apigateway.mapper.LogValidityMapper;
import com.ecdata.cmp.apigateway.service.ILogValidity;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/13 14:36
 * @modified By：
 */
@Slf4j
@Service
public class LogValidityImpl extends ServiceImpl<LogValidityMapper, LogValidity> implements ILogValidity {

    @Value("${ban.url}")
    private String url;

    @Value("${ban.clientSecret}")
    private String clientSecret;

    public static final String mediaType = "application/json";

    public static final String clientId = "YGPT";


    @Override
    public int insert(LogValidity logValidity) {
        return baseMapper.insert(logValidity);
    }

    /**
     * 查询token有效时间
     *
     * @param accessToken
     * @return
     */
    @Override
    public TokenVO getTokenInfo(String accessToken) {

        String tokenUrl = url + "/idp/oauth2/getTokenInfo?access_token=" + accessToken;
        TokenVO vo = new TokenVO();
        try {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put("X-Auth-Token", "json");
            String result = BaseOkHttpClientUtil.get(tokenUrl, headerMap);
            log.info("getToken{}  ", result);
            JSONObject object = JSON.parseObject(result);
            if (object.getString("errcode") == null) {
                vo.setAccessToken(object.getString("access_token"));
                vo.setRefreshToken(object.getString("refresh_token"));
                vo.setUid(object.getString("uid"));
                vo.setExpiresIn(object.getString("expires_in"));
            } else {
                vo.setErrcode(object.getString("errcode"));
                vo.setMsg(object.getString("msg"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return vo;
    }

    /**
     * 检查token是否有效
     *
     * @param accessToken
     * @return
     */
    @Override
    public boolean checkTokenValid(String accessToken) {
        String tokenUrl = url + "/idp/oauth2/checkTokenValid?access_token=" + accessToken;
        try {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put("X-Auth-Token", "json");
            String result = BaseOkHttpClientUtil.get(tokenUrl, headerMap);
            log.info("checkTokenValid{}  ", result);
            JSONObject object = JSON.parseObject(result);
            if (object.getString("errcode") == null) {
                return Boolean.getBoolean(object.getString("result"));
            }
            return false;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 刷新token
     *
     * @param refreshToken
     * @return
     */
    @Override
    public TokenVO refreshToken(String refreshToken) {

        String tokenUrl = url + "/idp/oauth2/refreshToken?client_id=" + clientId + "&grant_type=refresh_token&client_secret=" + clientSecret + "&refresh_token=" + refreshToken;
        TokenVO vo = new TokenVO();
        try {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put("X-Auth-Token", "json");
            String result = BaseOkHttpClientUtil.post(tokenUrl, "", headerMap, mediaType);
            log.info("getToken{}  ", result);
            JSONObject object = JSON.parseObject(result);
            if (object.getString("errcode") == null) {
                vo.setAccessToken(object.getString("access_token"));
                vo.setRefreshToken(object.getString("refresh_token"));
                vo.setUid(object.getString("uid"));
                vo.setExpiresIn(object.getString("expires_in"));
            } else {
                vo.setErrcode(object.getString("errcode"));
                vo.setMsg(object.getString("msg"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return vo;
    }

    @Override
    public Long isExist(long userId) {

        return baseMapper.isExist(userId);

    }

    @Override
    public Long getTime(LogValidity logValidity) {
        return baseMapper.getTime(logValidity);
    }

    @Override
    public int saveLogValidity(LogValidity logValidity) {
        return baseMapper.saveLogValidity(logValidity);
    }

    @Override
    public int updateLogValidity(LogValidity logValidity) {
        return baseMapper.updateLogValidity(logValidity);
    }


}
