package com.ecdata.cmp.huawei.service;

import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.dto.vo.VmHistoryDataVO;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/11 18:18
 * @modified By：
 */
public interface VmHistoryDataService {

    VmHistoryDataVO getVmHistoryData(RequestVO requestVO)throws Exception;

}
