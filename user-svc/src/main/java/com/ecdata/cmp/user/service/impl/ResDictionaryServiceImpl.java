package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.user.entity.ResDictionary;
import com.ecdata.cmp.user.mapper.ResDictionaryMapper;
import com.ecdata.cmp.user.service.ResDictionaryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhaoYX
 * @since 2020/1/13 14:12,
 */
@Service
public class ResDictionaryServiceImpl extends ServiceImpl<ResDictionaryMapper, ResDictionary>
        implements ResDictionaryService {

    @Override
    public void insertBatchDict(List<ResDictionary> list) {
        baseMapper.insertBatchDict(list);
    }
}
