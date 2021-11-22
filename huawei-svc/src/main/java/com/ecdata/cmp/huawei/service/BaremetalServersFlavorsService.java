package com.ecdata.cmp.huawei.service;
import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
//import com.ecdata.cmp.huawei.dto.vo.BareMetalVO;
import com.ecdata.cmp.huawei.dto.vo.BaremetalServersFlavorsVO;
import com.ecdata.cmp.huawei.dto.vo.CloudServersFlavorsVO;

import java.io.IOException;
import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/1 15:38
 * @modified By：
 */
public interface BaremetalServersFlavorsService {

    /**
     * 根据vdcID获取裸金属规格信息
     * @param projectId
     * @return
     */
    List<BaremetalServersFlavorsVO> getBaremetalServersFlavors(String projectId) throws IOException;


    /**
     * 根据vdcID获取弹性云规格信息
     * @param projectId
     * @return
     */
    List<CloudServersFlavorsVO> getCloudServersFlavors(String projectId) throws IOException;


    /**
     * 裸金属拼接数据
     */
    List<BareMetalVO> getBareMetalAll(String projectId)throws IOException;


}
