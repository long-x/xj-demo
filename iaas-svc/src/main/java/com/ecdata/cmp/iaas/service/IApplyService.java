package com.ecdata.cmp.iaas.service;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.dto.ApplyInfoVO;
import com.ecdata.cmp.iaas.entity.dto.apply.ApplyAreaUpdateVO;
import com.ecdata.cmp.iaas.entity.dto.apply.SaveDataVO;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/2/27 14:20
 */
public interface IApplyService {
    //审核
    BaseResponse apply(ApplyInfoVO vo);

    BaseResponse saveData(SaveDataVO saveDataVO);

}
