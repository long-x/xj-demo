package com.ecdata.cmp.huawei.dto.alarm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
@TableName("sys_manageone")
@ApiModel(value = "manageone对象", description = "manageone对象")
public class SysManageOne extends Model<SysManageOne> {
    private static final long serialVersionUID = -4247719264950562825L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 上次查询的时间
     */
    private Long lastQueryTime;

    /**
     * 执行时长
     */
    private Long executeTime;
}
