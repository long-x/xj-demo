package com.ecdata.cmp.iaas.service;

import com.ecdata.cmp.activiti.dto.vo.ActHistoricTaskInstanceVO;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/3/10 11:05
 */
public interface IAuditService {
    //流程跟踪
    List<ActHistoricTaskInstanceVO> queryProcessTracking(String processInstanceId);
}
