package com.ecdata.cmp.iaas.utils;

import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupResourceStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.ResourcePoolStatisticsVO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作台业务组资源概况树状
 */
public class BusinessGroupTreeUtil {
    public List<BusinessGroupStatisticsVO> queryData;
    public List<BusinessGroupResourceStatisticsVO> list = new ArrayList<>();

    public List<BusinessGroupResourceStatisticsVO> getTree(List<BusinessGroupStatisticsVO> menu) {
        this.queryData = menu;
        for (BusinessGroupStatisticsVO vo : menu) {
            if (vo.getParentId() == null) {
                BusinessGroupResourceStatisticsVO statisticsVO = assemblerBusinessGroupResourceStatisticsTreeVO(vo);
                statisticsVO.setChildren(getChild(vo.getId()));
                list.add(statisticsVO);
            }
        }
        return list;
    }

    public List<BusinessGroupResourceStatisticsVO> getChild(Long id) {
        List<BusinessGroupResourceStatisticsVO> lists = new ArrayList<>();
        for (BusinessGroupStatisticsVO vo : queryData) {
            if (id.equals(vo.getParentId())) {
                BusinessGroupResourceStatisticsVO statisticsVO = assemblerBusinessGroupResourceStatisticsTreeVO(vo);
                statisticsVO.setChildren(getChild(vo.getId()));
                lists.add(statisticsVO);
            }
        }
        return lists;
    }

    //组装业务组资源概况
    private BusinessGroupResourceStatisticsVO assemblerBusinessGroupResourceStatisticsTreeVO(BusinessGroupStatisticsVO vo) {
        BusinessGroupResourceStatisticsVO statisticsVO = new BusinessGroupResourceStatisticsVO();
        statisticsVO.setKey(vo.getId());//业务组id
        statisticsVO.setName(vo.getBusinessGroupName());//业务组名称
        statisticsVO.setIsApp(vo.getIsApp());

        List<ResourcePoolStatisticsVO> poolStatisticsVOS = vo.getPoolStatisticsVOS();

        if (CollectionUtils.isNotEmpty(poolStatisticsVOS)) {
            statisticsVO.setResourceNum(poolStatisticsVOS.size());
            statisticsVO.setCpu(vo.cpuTotal());
            statisticsVO.setMemory(vo.memoryTotal());
            statisticsVO.setVirtual(vo.vmTotal());
            statisticsVO.setVirtualUse(vo.vmUsed());
        }
        return statisticsVO;
    }

}
