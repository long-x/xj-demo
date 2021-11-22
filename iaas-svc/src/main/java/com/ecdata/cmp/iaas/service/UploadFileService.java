package com.ecdata.cmp.iaas.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author ：xuj
 * @date ：Created in 2020/5/7 15:52
 * @modified By：
 */
public interface UploadFileService {


    void saveFile(MultipartFile file, String date, Long cjr,long id);

}
