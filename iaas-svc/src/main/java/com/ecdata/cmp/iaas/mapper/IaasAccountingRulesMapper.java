package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.IaasAccountingRules;
import com.ecdata.cmp.iaas.entity.dto.IaasAccountingRulesVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/7 10:56
 * @modified By：
 */
@Mapper
@Repository
public interface IaasAccountingRulesMapper extends BaseMapper<IaasAccountingRules> {

    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);


    IPage<IaasAccountingRulesVO> qrList(Page<IaasAccountingRulesVO> page, @Param("map") Map<String, Object> map);


    /**
     * 查看该时间段是否存在计费设置
     * @param effectiveDate
     * @param expirationDate
     * @return
     */
    Integer isExist(@Param("effectiveDate") String effectiveDate, @Param("expirationDate") String expirationDate);

    /**
     * 查看是否有生效数据
     * @return
     */
    Integer isTakeEffect();

    /**
     * 查看当天有没有计费规则
     */
    Integer isTodayExits(@Param("today") String today);


    /**
     * 重置所有计费
     */
    void resetTakeEffect();

    /**
     * 设置一条最新的
     */

    void updateTakeEffect(@Param("today") String today);


}
