package com.ecdata.cmp.iaas.entity.dto.apply;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xuxiaojian
 * @date 2020/4/14 14:42
 */
@Data
public class UploadFileParam {

    private MultipartFile file;
    private String type;
}
