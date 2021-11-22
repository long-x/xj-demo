package com.ecdata.cmp.iaas.mapper.sangfor;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.sangfor.SangforSecurityRisk;
import com.ecdata.cmp.user.dto.SysNotificationReceiverVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface SangforMapper extends BaseMapper<SangforSecurityRisk> {
    boolean insertBatchRisk(List<SangforSecurityRisk> sangforSecurityRisk);

    List<SysNotificationReceiverVO> fetchReceiveIdByRisk(@Param("riskId") Long riskId);

    @Select("select min(last_time) from iaas_sangfor_security_risk")
    Date getMinLastTime();

    @Select("select min(record_date) from iaas_sangfor_security_risk")
    Long getMinRecordDate();

}
