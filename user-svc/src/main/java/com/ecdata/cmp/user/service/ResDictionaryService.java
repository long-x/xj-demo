package com.ecdata.cmp.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.entity.ResDictionary;

import java.util.List;

public interface ResDictionaryService extends IService<ResDictionary> {
    void insertBatchDict(List<ResDictionary> list);
}
