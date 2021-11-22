package com.ecdata.cmp.iaas.entity.dto.file;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-07-31 18:48
 */
@Data
@Accessors(chain = true)
public class FileVo {
    private Long fileId;
    private String filePath;
    private String type;
    private String uploadUserName;
    private String code;
}
