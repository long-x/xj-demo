package com.ecdata.cmp.user.service;

import com.ecdata.cmp.user.dto.BanTokenVO;
import com.ecdata.cmp.user.dto.BanUserVo;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/6 15:35
 * @modified By：
 */
public interface IBanService {


    /**
     * 根据code获取竹云token
     * @param code
     * @return
     */
    BanTokenVO getToken(String code);




    /**
     * 从竹云接口获取用户信息
     * @param accessToken
     * @return
     */
    BanUserVo getUserInfo(String accessToken);


    /**
     * 刷新token
     * @param refreshToken
     * @return
     */
    BanTokenVO refreshToken(String refreshToken);


    /**
     * 获取token时效
     * @param accessToken
     * @return
     */
    BanTokenVO getTokenInfo(String accessToken);


}
