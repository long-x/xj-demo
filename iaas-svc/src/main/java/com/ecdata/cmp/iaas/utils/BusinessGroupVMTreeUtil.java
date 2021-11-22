package com.ecdata.cmp.iaas.utils;

import com.ecdata.cmp.iaas.entity.dto.workbench.HostDistributionPieDataNewVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作台业务组资源概况树状
 */
public class BusinessGroupVMTreeUtil {
    public List<HostDistributionPieDataNewVO> queryData;
    public List<HostDistributionPieDataNewVO> list = new ArrayList<>();

    public List<HostDistributionPieDataNewVO> getTree(List<HostDistributionPieDataNewVO> menu) {
        this.queryData = menu;
        for (HostDistributionPieDataNewVO vo : menu) {
            if (vo.getParentId() == null) {
                vo.setChildren(getChild(vo.getId()));
                list.add(vo);
            }
        }
        return list;
    }

    public List<HostDistributionPieDataNewVO> getChild(Long id) {
        List<HostDistributionPieDataNewVO> lists = new ArrayList<>();
        for (HostDistributionPieDataNewVO vo : queryData) {
            if (id.equals(vo.getParentId())) {
                vo.setChildren(getChild(vo.getId()));
                lists.add(vo);
            }
        }
        return lists;
    }
}
