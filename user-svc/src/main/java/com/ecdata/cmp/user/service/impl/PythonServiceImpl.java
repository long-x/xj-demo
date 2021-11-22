package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.user.entity.Python;
import com.ecdata.cmp.user.mapper.PythonMapper;
import com.ecdata.cmp.user.service.IPythonService;
import org.springframework.stereotype.Service;

/**
 * @author xuxinsheng
 * @since 2019-11-22
 */
@Service
public class PythonServiceImpl extends ServiceImpl<PythonMapper, Python> implements IPythonService {

    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id, updateUser);
    }
}
