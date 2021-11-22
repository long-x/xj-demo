package com.ecdata.cmp.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.user.dto.BanTokenVO;
import com.ecdata.cmp.user.dto.BanUserVo;
import com.ecdata.cmp.user.service.IBanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/6 16:03
 * @modified By：
 */
@Service
@Slf4j
public class BanServiceImpl implements IBanService {

    @Value("${ban.url}")
    private String url;

    @Value("${ban.clientSecret}")
    private String clientSecret;


    public static final String mediaType = "application/json";

    public static final String clientId = "YGPT";

    public static final String grantType = "authorization_code";

    @Override
    public BanTokenVO getToken(String code) {
        String tokenUrl = url+"/idp/oauth2/getToken?client_id="+clientId+"&grant_type="+grantType+"&code="+code+"&client_secret="+clientSecret;
        BanTokenVO vo = new BanTokenVO();
        try {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put("X-Auth-Token","json");
            String result = BaseOkHttpClientUtil.post(tokenUrl, "", headerMap, mediaType);
            log.info("getToken{}  ",result);
            JSONObject object = JSON.parseObject(result);
            if (object.getString("errcode")==null){
                vo.setAccessToken(object.getString("access_token"));
                vo.setRefreshToken(object.getString("refresh_token"));
                vo.setUid(object.getString("uid"));
                vo.setExpiresIn(object.getString("expires_in"));
            }else {
                vo.setErrcode(object.getString("errcode"));
                vo.setMsg(object.getString("msg"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return vo;
    }


    @Override
    public BanUserVo getUserInfo(String accessToken) {
        String infoUrl = url+"/idp/oauth2/getUserInfo?access_token="+accessToken+"&client_id="+clientId;
        Map<String, String> headerMap = new HashMap<>();
        BanUserVo banUserVo = new BanUserVo();
        try {
            String result = BaseOkHttpClientUtil.get(infoUrl, headerMap);
            log.info("getUserInfo{}  ",result);
            JSONObject object = JSON.parseObject(result);
            if(object.getString("errcode")==null){
                JSONArray jsonArray = object.getJSONArray("spRoleList");//获取数组
                //获取用户名
                for (int i = 0; i < jsonArray.size(); i++) {
                    banUserVo.setSpRoleList(jsonArray.getString(i));
                }
            }else {
                banUserVo.setErrcode(object.getString("errcode"));
                banUserVo.setMsg(object.getString("msg"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return banUserVo;
    }

    @Override
    public BanTokenVO refreshToken(String refreshToken) {

        String tokenUrl = url+"/idp/oauth2/refreshToken?client_id="+clientId+"&grant_type=refresh_token&client_secret="+clientSecret+"&refresh_token="+refreshToken;
        BanTokenVO vo = new BanTokenVO();
        try {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put("X-Auth-Token","json");
            String result = BaseOkHttpClientUtil.post(tokenUrl, "", headerMap, mediaType);
            log.info("getToken{}  ",result);
            JSONObject object = JSON.parseObject(result);
            if (object.getString("errcode")==null){
                vo.setAccessToken(object.getString("access_token"));
                vo.setRefreshToken(object.getString("refresh_token"));
                vo.setUid(object.getString("uid"));
                vo.setExpiresIn(object.getString("expires_in"));
            }else {
                vo.setErrcode(object.getString("errcode"));
                vo.setMsg(object.getString("msg"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return vo;

    }

    @Override
    public BanTokenVO getTokenInfo(String accessToken) {
        String tokenUrl = url+"/idp/oauth2/getTokenInfo?access_token="+accessToken;
        BanTokenVO vo = new BanTokenVO();
        try {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put("X-Auth-Token","json");
            String result = BaseOkHttpClientUtil.get(tokenUrl, headerMap);
            log.info("getToken{}  ",result);
            JSONObject object = JSON.parseObject(result);
            if (object.getString("errcode")==null){
                vo.setAccessToken(object.getString("access_token"));
                vo.setRefreshToken(object.getString("refresh_token"));
                vo.setUid(object.getString("uid"));
                vo.setExpiresIn(object.getString("expires_in"));
            }else {
                vo.setErrcode(object.getString("errcode"));
                vo.setMsg(object.getString("msg"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return vo;
    }
}
