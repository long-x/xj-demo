package com.ecdata.cmp.user;

import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.user.dto.DistributionBlockDTO;
import com.ecdata.cmp.user.dto.DistributionDTO;
import com.ecdata.cmp.user.entity.User;
import com.ecdata.cmp.user.mapper.UserMapper;
import com.ecdata.cmp.user.service.ISysBusinessGroupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2019-09-25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserApplicationTest {

    @Autowired
    UserMapper userMapper;

    @Autowired
    ISysBusinessGroupService sysBusinessGroupService;

    @Test
    public void test() {
        Sign.setCurrentTenantId(10000L);
//        Map<String, Object> map = new HashMap<>();
////        map.put("userId", 2777994821132290L);
////        map.put("type", 2);
////        List<Long> poolIds = sysBusinessGroupService.getPoolIds(map);
////        for (Long poolId : poolIds) {
////            System.out.println(poolId);
////        }
        DistributionDTO distributionDTO = sysBusinessGroupService.getUserDistribution();
        System.out.println(distributionDTO.getTotalNum());
        for (DistributionBlockDTO blockDTO : distributionDTO.getBlockList()) {
            System.out.println(blockDTO.getNum() + blockDTO.getBlockName() + blockDTO.getRate());
        }
    }
}
