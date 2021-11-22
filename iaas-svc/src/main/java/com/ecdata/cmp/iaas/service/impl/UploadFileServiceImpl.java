package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.iaas.entity.SysFile;
import com.ecdata.cmp.iaas.mapper.file.SysFileMapper;
import com.ecdata.cmp.iaas.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * @author ：xuj
 * @date ：Created in 2020/5/7 15:53
 * @modified By：
 */
@Service
public class UploadFileServiceImpl implements UploadFileService {
    @Autowired
    private SysFileMapper sysFileMapper;

    @Override
    @Async
    public void saveFile(MultipartFile file, String date, Long cjr,long id) {
        try{
            InputStream is = file.getInputStream();
            SysFile uploadFile = new SysFile();
            uploadFile.setId(id);
            uploadFile.setFileName(file.getOriginalFilename());
            uploadFile.setFileType(file.getContentType());
            uploadFile.setFileSize(file.getSize());
            uploadFile.setCreateUser(cjr);
            uploadFile.setCreateTime(new Date());
            uploadFile.setIsDeleted(0);
            uploadFile.setImg(inputStreamToByte(is));
            is.close();
            sysFileMapper.insert(uploadFile);
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    /**
     * 文件转换byte[]
     *
     * @param is
     * @return
     * @throws IOException
     */
    private byte[] inputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bAOutputStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bAOutputStream.write(ch);
        }
        byte data[] = bAOutputStream.toByteArray();
        bAOutputStream.close();
        return data;
    }

}
