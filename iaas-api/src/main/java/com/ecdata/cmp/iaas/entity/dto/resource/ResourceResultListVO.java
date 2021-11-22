package com.ecdata.cmp.iaas.entity.dto.resource;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2020/5/26 16:22
 * @modified By：
 */
@Data
public class ResourceResultListVO {

    @ApiModelProperty("list")
    private Map<String,List<ResourceResultVO> > list;

}
