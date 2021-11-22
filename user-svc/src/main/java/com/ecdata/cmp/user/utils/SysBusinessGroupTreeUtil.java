package com.ecdata.cmp.user.utils;

import com.ecdata.cmp.user.dto.SysBusinessGroupVO;

import java.util.ArrayList;
import java.util.List;

public class SysBusinessGroupTreeUtil {
    public List<SysBusinessGroupVO> queryData;
    public List<SysBusinessGroupVO> list = new ArrayList<>();

    public List<SysBusinessGroupVO> getTreeList(List<SysBusinessGroupVO> menu) {
        this.queryData = menu;
        for (SysBusinessGroupVO vo : menu) {
            if (vo.getParentId() == null) {
                vo.setChildren(getTreeChild(vo.getId()));
                list.add(vo);
            }
        }
        return list;
    }

    public List<SysBusinessGroupVO> getTreeChild(Long id) {
        List<SysBusinessGroupVO> lists = new ArrayList<>();
        for (SysBusinessGroupVO vo : queryData) {
            if (id.equals(vo.getParentId())) {
                vo.setChildren(getTreeChild(vo.getId()));
                lists.add(vo);
            }
        }
        return lists;
    }


}
