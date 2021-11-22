package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.IaasAccountingRules;
import com.ecdata.cmp.iaas.entity.dto.IaasAccountingRulesVO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/7 10:50
 * @modified By：
 */
public interface IaasAccountingRulesService extends IService<IaasAccountingRules> {

    /**
     * 新增计量计费
     * @param vo
     * @return
     */
    int save(IaasAccountingRulesVO vo);



    void modifyUpdateRecord(Long id, Long updateUser);


    IPage<IaasAccountingRulesVO> qrList(Page<IaasAccountingRulesVO> page, @Param("map") Map<String, Object> map);


    Integer isTodayExits(String today);

    /**
     * 计划任务
     * 更新最新的计费规则
     * @param today
     */
    void updateTakeEffect(String today);


}
