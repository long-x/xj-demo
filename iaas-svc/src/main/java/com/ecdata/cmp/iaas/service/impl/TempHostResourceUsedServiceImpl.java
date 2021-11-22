package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.TempHostResourceUsed;
import com.ecdata.cmp.iaas.mapper.TempHostResourceUsedMapper;
import com.ecdata.cmp.iaas.service.ITempHostResourceUsedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @title: TempHostResourceUsedServiceImpl
 * @Author: shig
 * @description:宿主机资源使用情况临时表
 * @Date: 2019/12/11 2:18 下午
 */
@Slf4j
@Service
public class TempHostResourceUsedServiceImpl extends ServiceImpl<TempHostResourceUsedMapper, TempHostResourceUsed> implements ITempHostResourceUsedService {

}