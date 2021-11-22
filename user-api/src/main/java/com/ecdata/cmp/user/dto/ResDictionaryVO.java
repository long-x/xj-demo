package com.ecdata.cmp.user.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author ZhaoYX
 * @since 2020/1/14 15:38,
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "字典对象", description = "字典对象")
public class ResDictionaryVO {
}
