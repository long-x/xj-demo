package com.ecdata.cmp.huawei.service;

import com.ecdata.cmp.huawei.dto.vo.ProjectsVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/10 19:45
 * @modified By：
 */
public interface ProjectsService {

    List<ProjectsVO> getProjectList(RequestVO requestVO)throws Exception;

}
