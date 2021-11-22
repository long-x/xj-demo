package com.ecdata.cmp.huawei.service;

import com.ecdata.cmp.huawei.dto.vo.HostVolumesVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/11 11:03
 * @modified By：
 */
public interface HostVolumesService {

    List<HostVolumesVO> getHostVolumesList(RequestVO requestVO)throws Exception;

}
