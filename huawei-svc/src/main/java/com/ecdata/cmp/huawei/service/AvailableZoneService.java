package com.ecdata.cmp.huawei.service;

import com.ecdata.cmp.huawei.dto.availablezone.*;
import com.ecdata.cmp.huawei.dto.token.TokenDTO;
import com.ecdata.cmp.huawei.dto.vo.BmsInfoVO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AvailableZoneService {

    /**
     * 根据云资源池id查询可用分区
     *
     * @param token token信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    List<AvailableZone> getAvailableZoneByInfraId(String token, String infraId) throws IOException;

    /**
     * 根据可用分区id查询可用分区数据
     *
     * @param token token信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    List<SysAzone> getAvailableZoneById(String token, String nativeId) throws IOException;

    /**
     * 查询可用区的当前容量
     *
     * @param token token信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    Map<String,WholeCapacity> getAvailableZoneCapacity(String token, List<SysAzone> SysAzoneList) throws IOException;

    /**
     * 查询可用区存储池的容量
     *
     * @param token token信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    List<WholeDimensionCapacity> getAvailableZoneStoragePoolCapacity(String token, List<SysAzone> SysAzoneList,String dimensionType) throws IOException;

    /**
     * 获取可用区的虚拟机、物理机状态数目统计
     *
     * @param token 信息
     * @return AvailableZoneStatistics
     * @throws IOException io异常
     */
    AvailableZoneStatistics getAvailableZoneDetail(String token, String azoneId) throws IOException;

    /**
     * 查询可用区的资源
     *
     * @param token token信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    AvailableZoneResource getAvailableZoneResource(String token,  String azoneId) throws IOException;

    /**
     * 根据项目id查询所有可用区(包括裸金属)
     *
     * @param token token信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    List<ProjectAviailableZone> getAllAvailableZoneByProjectId(String token,  String projectId) throws IOException;

    /**
     * 根据项目id查询所有可用区(去除裸金属)
     *
     * @param token token信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    List<AvailableZone> getAvailableZoneByProjectId(String token,  String projectId) throws IOException;


    /**
     * 查出所有分区 页面爬的接口
     * @param type
     * @return
     * @throws IOException
     */
    List<SysAzone> getAzAllList(String type)throws IOException;


    /**
     * 根据分区查询磁盘名称
     * @param azId
     * @return
     * @throws IOException
     */
    List<DimensionInfo> getDiskName(String azId)throws IOException;


    /**
     * 根据vdckey 获取裸金属列表
     * @param vdcKey
     * @return
     * @throws IOException
     */
    List<BmsInfoVO> getBmsList(String vdcKey)throws IOException;


    /**
     * 根据项目id获取裸金属列表
     * @param projectId
     * @return
     * @throws IOException
     */
    List<BmsInfoVO> getBmsListByProjectId(String projectId) throws IOException;


}
