package com.ecdata.cmp.iaas.service;

import com.ecdata.cmp.huawei.dto.vo.RequestVO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/14 11:42
 * @modified By：
 */

public interface VmhistoryPerformanceService {

    /**
     * 获取性能历史数据
     * @param requestVO
     * @return
     * @throws IOException
     */
    List<Map<String, Object>> getVmHistoryData(RequestVO requestVO) throws IOException;



}
