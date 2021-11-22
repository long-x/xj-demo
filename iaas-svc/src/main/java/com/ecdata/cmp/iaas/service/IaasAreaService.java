package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.IaasArea;
import com.ecdata.cmp.iaas.entity.dto.IaasAreaVO;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/3/16 10:02
 */
public interface IaasAreaService extends IService<IaasArea> {
    List<IaasAreaVO> queryIaasAreas();
}
