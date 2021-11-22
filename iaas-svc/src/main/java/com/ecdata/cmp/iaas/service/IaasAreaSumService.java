package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.IaasAreaSum;

/**
 * @author hhj
 * @date 2020/5/26
 */
public interface IaasAreaSumService extends IService<IaasAreaSum> {

    int getByServerNameAndAreaName(String serverName, String areaName);
}
