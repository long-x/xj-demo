package com.ecdata.cmp.huawei.service;

import com.ecdata.cmp.huawei.dto.region.ActualCapacity;
import com.ecdata.cmp.huawei.dto.region.RegionInfo;
import com.ecdata.cmp.huawei.dto.token.OMToken;
import com.ecdata.cmp.huawei.dto.token.TokenInfoDTO;
import com.ecdata.cmp.huawei.dto.vm.SimpleIndicator;
import com.ecdata.cmp.huawei.dto.availablezone.AvailableZoneStatistics;
import com.ecdata.cmp.huawei.dto.vo.CiInstanceVO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ty
 * @description
 * @date 2019/11/20 15:40
 */
public interface ManageOneService {
    /**
     * 获取token(ocp4.1)
     *
     * @param tokenInfoDTO token信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    String getTokenInfo(TokenInfoDTO tokenInfoDTO,String ipAddress) throws IOException;

    /**
     * 获取ManageOne运维面token
     *
     * @param userName，value token信息
     * @return OMToken
     * @throws IOException io异常
     */
    OMToken getOMToken(String userName, String value) throws IOException;

    /**
     * 获取ManageOne运营面token
     *
     * @param userName，value,domainName,projectId token信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    String getOCToken(String userName, String value, String domainName, String projectId) throws IOException;

    /**
     * 获取ManageOne运维面Web token
     *
     * @param userName，value token信息
     * @return OMToken
     * @throws IOException io异常
     */
    OMToken getOMTokenWeb(String userName, String value) throws IOException;

    /**
     * 获取指定id的虚拟机resId
     *
     * @param token，nativeId,projectId信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    List<CiInstanceVO> getInstanceDataByType(String token, String nativeId, String projectId, String type) throws IOException;

    /**
     * 获取所有监控对象类型
     *
     * @param token，nativeId,projectId信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    String getMonitorObjs(String token) throws IOException;

    /**
     * 获取所有监控对象类型
     *
     * @param token，nativeId,projectId信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    List<String> getTargetsByObjType(String token, String objTypeId) throws IOException;

    /**
     * 获取监控指标
     *
     * @param token，indicators信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    Map<Object, SimpleIndicator> getMonitorQuotaById(String token, List<Double> indicators) throws IOException;

    /**
     * 获取指定的区域的指定资源类型的当前容量信息
     *
     * @param token 信息
     * @return AvailableZoneStatistics
     * @throws IOException io异常
     */
    AvailableZoneStatistics getRegionCapacity(String token, String regionId, String resourceType) throws IOException;

    /**
     * 获取区域列表
     *
     * @param token 信息
     * @return AvailableZoneStatistics
     * @throws IOException io异常
     */
    RegionInfo getRegion(String token) throws IOException;

    /**
     * 获取VDC列表
     *
     * @param token 信息
     * @return String
     * @throws IOException io异常
     */
    String getVdcs(String token) throws IOException;

    /**
     * 获取制定VDC的项目列表
     *
     * @param token 信息
     * @return String
     * @throws IOException io异常
     */
    String getProjectByVdcId(String token,String vdcId) throws IOException;

    /**
     * 查询区域当前单个指标容量
     *
     * @param token 信息
     * @return String
     * @throws IOException io异常
     */
    ActualCapacity getRegoinCapacity(String token, String regionId, String type,String dataKey) throws IOException;

    /**
     * 查询区域当前所有指标容量
     *
     * @param token 信息
     * @return String
     * @throws IOException io异常
     */
    Map<String,ActualCapacity> getRegoinAllCapacity(String token, String regionId) throws IOException;
}
