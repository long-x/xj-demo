package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.activiti.client.ActivitiClient;
import com.ecdata.cmp.activiti.dto.response.ActHistoricTaskInstanceListResponse;
import com.ecdata.cmp.activiti.dto.vo.ActHistoricTaskInstanceVO;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.iaas.service.IAuditService;
import com.ecdata.cmp.user.client.UserClient;
import com.ecdata.cmp.user.dto.response.UserResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/3/10 11:06
 */
@Service
public class IAuditServiceImpl implements IAuditService {
    @Autowired
    private ActivitiClient activitiClient;

    @Autowired
    private UserClient userClient;

    @Override
    public List<ActHistoricTaskInstanceVO>  queryProcessTracking(String processInstanceId) {
        try {
            ActHistoricTaskInstanceListResponse response = activitiClient.queryProcessTask(processInstanceId);

            if (response.getCode() != 0 || CollectionUtils.isEmpty(response.getData())) {
                return new ArrayList<>();
            }

            List<ActHistoricTaskInstanceVO> data = response.getData();

            for (ActHistoricTaskInstanceVO vo : data) {
                if (StringUtils.isBlank(vo.getAssignee())) {
                    continue;
                }
                UserResponse userResponse = userClient.getById(AuthContext.getAuthz(), Long.valueOf(vo.getAssignee()));

                if (userResponse.getCode() != 0 || userResponse.getData() == null || StringUtils.isBlank(userResponse.getData().getDisplayName())) {
                    continue;
                }
                vo.setAssignee(userResponse.getData().getDisplayName());
            }
            return data;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
