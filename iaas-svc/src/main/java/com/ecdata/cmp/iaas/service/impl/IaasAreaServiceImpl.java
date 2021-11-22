package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.IaasArea;
import com.ecdata.cmp.iaas.entity.dto.IaasAreaVO;
import com.ecdata.cmp.iaas.mapper.IaasAreaMapper;
import com.ecdata.cmp.iaas.service.IaasAreaService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xuxiaojian
 * @date 2020/3/16 10:03
 */
@Service
public class IaasAreaServiceImpl extends ServiceImpl<IaasAreaMapper, IaasArea> implements IaasAreaService {

    @Override
    public List<IaasAreaVO> queryIaasAreas() {
        LambdaQueryWrapper<IaasArea> eq = Wrappers.<IaasArea>lambdaQuery().eq(IaasArea::getIsDeleted, 0);
        List<IaasAreaVO> iaasAreas = this.list(eq).stream().map(item -> {
                    IaasAreaVO vo = new IaasAreaVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }
        ).collect(Collectors.toList());
        return iaasAreas;
    }
}
