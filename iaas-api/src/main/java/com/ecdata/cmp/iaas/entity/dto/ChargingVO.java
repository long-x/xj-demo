package com.ecdata.cmp.iaas.entity.dto;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/21 16:36
 * @modified By：
 */
@Data
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "查询出来计费信息", description = "查询出来计费信息")
public class ChargingVO extends Model<ChargingVO> {


    private static final long serialVersionUID = -904218476259510341L;


    @ApiModelProperty("虚拟机id")
    private Long vmId;


    @ApiModelProperty("主机id")
    private Long hostId;


    @ApiModelProperty("资源池id")
    private Long poolId;

    @ApiModelProperty("业务组id")
    private Long groupId;


    @ApiModelProperty("业务组名称")
    private String businessGroupName;

    @ApiModelProperty("cup核数")
    private Long vcpuTotal;

    @ApiModelProperty("内存大小")
    private Long memoryTotal;

    @ApiModelProperty("存储大小")
    private Long diskTotal;


}
