package com.ecdata.cmp.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.huawei.dto.availablezone.WholeDimensionCapacity;
import com.ecdata.cmp.huawei.dto.response.*;
import com.ecdata.cmp.huawei.dto.vo.VdcsVO;
import com.ecdata.cmp.user.dto.ResWorkorderVO;
import com.ecdata.cmp.user.entity.ResWorkorder;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IResWorkorderService extends IService<ResWorkorder> {

    List<VdcsVO> getVdcList();

    Map<String,Object> selectConditions() throws IOException;

    ResWorkorder createWorkOrder(ResWorkorderVO resWorkorderVo);

    //vdc下项目
    @Transactional(rollbackFor = Exception.class)
    Map<String, Object> queryProject(String vdcId) throws IOException;

    //根据项目id获取各种关联下拉框
    @Transactional(rollbackFor = Exception.class)
    Map<String,Object> queryVdcAndProject(String vdcId,String proId) throws IOException;

    //根据区域Id获取磁盘信息
    @Transactional(rollbackFor = Exception.class)
    List<WholeDimensionCapacity> queryDisks(String azId) throws IOException;

    ResWorkorder qryWorkOrderByPoolId(Long poolId);

//    //规格参数
//    VmFlavorsResponse getVdcFlavors(String vdcId, String projectKey) throws IOException;
//
//    //安全组
//    SecurityGroupsListResponse getSecurityGroup(String vdcId, String projectKey) throws IOException;
//
//    //镜像
//    ImagesListResponse getMirrors (String vdcId, String projectKey) throws IOException;

//    @Transactional(rollbackFor = Exception.class)
//    AvailableZoneResponse getAvailableZones(String proId) throws IOException;
//
//    @Transactional(rollbackFor = Exception.class)
//    AvailableZoneResourceResponse getAvailableDisks(String azId) throws IOException;
}
