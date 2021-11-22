package com.ecdata.cmp.huawei.service;

import com.ecdata.cmp.huawei.dto.vo.RegionsVO;

import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/9 11:09
 * @modified By：
 */
public interface RegionsService {


    List<RegionsVO> gitRegionsList(Map param) throws Exception;

}
