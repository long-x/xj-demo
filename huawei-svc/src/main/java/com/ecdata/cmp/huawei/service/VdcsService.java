package com.ecdata.cmp.huawei.service;

import com.ecdata.cmp.huawei.dto.vo.VdcsVO;

import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/9 15:59
 * @modified By：
 */
public interface VdcsService {

    List<VdcsVO> getVdcsList(Map param) throws Exception;
}
