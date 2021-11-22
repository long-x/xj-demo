package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupStatisticsVO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MenuTreeUtil {
    public static Map<String, Object> mapArray = new LinkedHashMap<String, Object>();
    public List<BusinessGroupStatisticsVO> menuCommon;
    public List<Object> list = new ArrayList<Object>();

    public List<Object> menuList(List<BusinessGroupStatisticsVO> menu) {
        this.menuCommon = menu;
        for (BusinessGroupStatisticsVO x : menu) {
            Map<String, Object> mapArr = new LinkedHashMap<String, Object>();
            if (x.getParentId() == null) {
                mapArr.put("id", x.getId());
                mapArr.put("name", x.getBusinessGroupName());
                mapArr.put("pid", x.getParentId());
                mapArr.put("childList", menuChild(x.getId()));
                list.add(mapArr);
            }
        }
        return list;
    }

    public List<?> menuChild(Long id) {
        List<Object> lists = new ArrayList<Object>();
        for (BusinessGroupStatisticsVO a : menuCommon) {
            Map<String, Object> childArray = new LinkedHashMap<String, Object>();
            if (a.getParentId() == id) {
                childArray.put("id", a.getId());
                childArray.put("name", a.getBusinessGroupName());
                childArray.put("pid", a.getParentId());
                childArray.put("childList", menuChild(a.getId()));
                lists.add(childArray);
            }
        }
        return lists;
    }
}
