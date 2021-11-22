package com.ecdata.cmp.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.user.dto.SysNotifyManageVO;
import com.ecdata.cmp.user.entity.SysNotifyManage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface SysNotifyManageMapper extends BaseMapper<SysNotifyManage> {

    IPage<SysNotifyManageVO> getNotifyManage(Page page, @Param("map") Map<String, Object> map);

}
