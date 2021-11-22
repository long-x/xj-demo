package com.ecdata.cmp.huawei.service;

import com.ecdata.cmp.huawei.dto.vo.ImagesVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/3 16:12
 * @modified By：
 */
public interface ImagesService {


    /**
     * 查询镜像接口 /images
     * @param  requestVO
     * @return
     */
    List<ImagesVO> getImages(RequestVO requestVO) throws IOException;


}
