package com.ecdata.cmp.huawei.service;

import com.ecdata.cmp.huawei.dto.vo.PhysicalHostVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/11 14:25
 * @modified By：
 */
public interface PhysicalHostService {
    /**
     * 宿主机利用率
     * @param requestVO
     * @return
     * @throws Exception
     */
   List<PhysicalHostVO> getPhysicalHostList(RequestVO requestVO) throws Exception;

}
