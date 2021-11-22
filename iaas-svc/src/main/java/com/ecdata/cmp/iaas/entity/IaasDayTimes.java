package com.ecdata.cmp.iaas.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("iaas_day_times")
public class IaasDayTimes extends Model<IaasDayTimes> {
    private String time;
}