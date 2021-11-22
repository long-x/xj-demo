package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.IaasTimedTask;
import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
import com.ecdata.cmp.iaas.entity.dto.IaasTimedTaskVO;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineVO;
import com.ecdata.cmp.iaas.mapper.IaasTimedTaskMapper;
import com.ecdata.cmp.iaas.service.IaasTimedTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2020/5/29 16:27
 * @modified By：
 */
@Slf4j
@Service
public class IaasTimedTaskServiceImpl  extends ServiceImpl<IaasTimedTaskMapper, IaasTimedTask> implements IaasTimedTaskService{


    @Override
    public boolean add(IaasTimedTaskVO entity) {
        long id = SnowFlakeIdGenerator.getInstance().nextId();
        IaasTimedTask iaasTimedTask = new IaasTimedTask();
        BeanUtils.copyProperties(entity, iaasTimedTask);
        iaasTimedTask.setCreateTime(DateUtil.getNow());
        iaasTimedTask.setId(id);
        return this.save(iaasTimedTask);
    }

    @Override
    public List<IaasVirtualMachineVO> getVMByGropu(String id) {
        return baseMapper.getVMByGropu(id);
    }

    @Override
    public List<BareMetalVO> getBareByGropu(String id) {
        return baseMapper.getBareByGropu(id);
    }

    @Override
    public boolean delTask(Long id) throws IOException {
        if (this.removeById(id)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public IaasTimedTaskVO getTaskInfo(Long id) {
        return baseMapper.getTaskInfo(id);
    }

    @Override
    public IPage<IaasTimedTaskVO> getIaasTimedTaskVOPage(Page<IaasTimedTaskVO> page, Map map) {
        return baseMapper.getIaasTimedTaskVOPage(page,map);
    }

    @Override
    public List<IaasTimedTaskVO> onOrOffTaskList(Map map) {
        return baseMapper.onOrOffTaskList(map);
    }
}
