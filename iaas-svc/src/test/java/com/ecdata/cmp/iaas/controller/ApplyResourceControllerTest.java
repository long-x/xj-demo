package com.ecdata.cmp.iaas.controller;

import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyResourceVO;
import com.ecdata.cmp.iaas.service.IApplyResourceService;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Description:
 *
 * @author hhj
 * @create created in 15:05 2020/6/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplyResourceControllerTest {

    @Autowired
    private IApplyResourceService applyResourceService;

    @Test
    public void recycleResourcesAuto() {
        List<IaasApplyResourceVO> iaasApplyResourceVOList = applyResourceService.recycleResourcesAuto();
        // 无申请资源到期
        if (CollectionUtils.isEmpty(iaasApplyResourceVOList)) {
            System.err.println("到期申请资源为空");
        }

        // 审批完成租期到期的资源重新copy一份存入数据库, 再发起一条默认审批流程，审批中添加取消纳管按钮
        for (IaasApplyResourceVO vo : iaasApplyResourceVOList) {
            vo.setState(1);
            vo.setBusinessActivitiId(84601990567477250L);
            vo.setBusinessActivitiName("城投审核流程");
            applyResourceService.copyResourceAndStartRecoveryApply(vo);
        }

        System.err.println("自动回收资源成功");

    }
}
