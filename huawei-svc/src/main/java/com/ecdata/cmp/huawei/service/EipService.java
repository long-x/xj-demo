package com.ecdata.cmp.huawei.service;

import com.ecdata.cmp.huawei.dto.vo.EipVO;

import java.io.IOException;
import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/10/19 16:27
 * @modified By：
 */
public interface EipService {
    List<EipVO> findEip(String id) throws IOException;
}
