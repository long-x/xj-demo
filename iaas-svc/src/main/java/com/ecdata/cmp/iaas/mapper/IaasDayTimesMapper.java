package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.IaasDayTimes;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IaasDayTimesMapper extends BaseMapper<IaasDayTimes> {
    void deleteAll();

    int insertForeach(List<String> list);
}