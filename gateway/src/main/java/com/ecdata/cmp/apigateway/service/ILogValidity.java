package com.ecdata.cmp.apigateway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.apigateway.entity.LogValidity;
import com.ecdata.cmp.apigateway.entity.response.TokenVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/13 14:33
 * @modified By：
 */
public interface ILogValidity extends IService<LogValidity> {

    int insert(LogValidity logValidity);

    /**
     * 查询token过期时间
     */
    TokenVO getTokenInfo(String accessToken);

    /**
     * 查询token是否有效
     */
    boolean checkTokenValid(String accessToken);


    /**
     * 刷新token
     */
    TokenVO refreshToken(String refreshToken);


    /**
     * 查询是否存在记录
     * @param userId
     * @return
     */
    Long isExist(long userId);


    Long getTime(LogValidity logValidity);


    /**
     * 插入记录
     * @param logValidity
     * @return
     */
    int saveLogValidity(LogValidity logValidity);


    int updateLogValidity(LogValidity logValidity);

}
