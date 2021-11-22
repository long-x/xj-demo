package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.IaasAlert;
import com.ecdata.cmp.iaas.entity.dto.IaasAlertVO;
import com.ecdata.cmp.user.dto.SysNotificationReceiverVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IaasAlertMapper extends BaseMapper<IaasAlert> {

    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);

    boolean saveAlert(IaasAlert ia);

    boolean saveAlertBatch(List<IaasAlert> list);

    IPage<IaasAlertVO> alertPage(Page page, Wrapper<IaasAlert> wrapper);

    SysNotificationReceiverVO fetchReceiveIdByAlert(Long csn);

}
