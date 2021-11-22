package com.ecdata.cmp.huawei.service;

import com.ecdata.cmp.huawei.dto.vo.VpcVO;

import java.io.IOException;
import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/26 21:52
 * @modified By：
 */
public interface VpcService {

    List<VpcVO> getVpcList(String id) throws IOException;


}
