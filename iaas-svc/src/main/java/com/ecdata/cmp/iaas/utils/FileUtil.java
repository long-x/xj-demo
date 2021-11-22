package com.ecdata.cmp.iaas.utils;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.SysFile;
import com.ecdata.cmp.iaas.entity.dto.file.FileVo;
import com.ecdata.cmp.iaas.mapper.file.SysFileMapper;
import com.ecdata.cmp.iaas.service.UploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 描述:文件上传
 *
 * @author xxj
 * @create 2019-04-01 13:41
 */
@Component
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    @Autowired
    private SysFileMapper sysFileMapper;

    @Autowired
    private UploadFileService uploadFileService;

    /**
     * 上传文件
     *
     * @param file
     * @param date
     * @param cjr
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public FileVo saveFile(MultipartFile file, String date, Long cjr) throws FileNotFoundException {

        long id = SnowFlakeIdGenerator.getInstance().nextId();
        uploadFileService.saveFile(file,date,cjr,id);
        FileVo vo = new FileVo();
        vo.setFileId(id);
        vo.setFilePath(file.getOriginalFilename());

        return vo;
    }




    //下载
    public BaseResponse downloadFile(HttpServletResponse response, String fileId) {
        BaseResponse baseResponse = new BaseResponse();

        SysFile sysFile = sysFileMapper.selectById(fileId);

        if (sysFile == null) {
            baseResponse.setCode(201);
            baseResponse.setMessage("文件不存在！");
            return baseResponse;
        }

        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");

        OutputStream os = null;
        try {
            String name = java.net.URLEncoder.encode(sysFile.getFileName(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + name);

            os = response.getOutputStream();
            os.write(sysFile.getImg());
            os.flush();
        } catch (Exception e1) {
            baseResponse.setCode(201);
            baseResponse.setMessage("文件下载异常！");
            return baseResponse;
        }
        return baseResponse;
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static byte[] getFileByte(String filename) throws IOException {

        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

    public void delete(String fileId) {
        sysFileMapper.deleteFile(fileId);
    }

    public List<FileVo> queryFile(List<String> list) {
        return sysFileMapper.queryFileByIds(list);
    }

//    public String uploadFile(InputStream is, String fileName, String date) throws FileNotFoundException, IOException {
//
//        File fileUploadDir = new File(filePath);
//        if (!fileUploadDir.isDirectory()) {
//            fileUploadDir.mkdir();
//        }
//
//        String subPath = createSubFolder(fileUploadDir.getPath(), getDateStr(null, "yyyyMM"));
//        logger.info("Create sub path {}", subPath);
//
//        String nameArray[] = fileName.split("\\.");
//
//        String newFileName = nameArray[0] + "-" + date + "." + nameArray[1];
//        FileOutputStream fos = new FileOutputStream(filePath + File.separator + subPath + File.separator + newFileName);
//
//        byte[] b = new byte[1024];
//        while ((is.read(b)) != -1) {
//            fos.write(b);
//        }
//
//        fos.close();
//        is.close();
//        logger.info("File new name is {}", newFileName);
//        return newFileName;
//    }

    private static String createSubFolder(String parentPath, String subFolderName) throws IOException {
        String folderPath = parentPath + "\\" + subFolderName;
        File file = new File(folderPath);
        if (!file.exists()) {//如果文件夹不存在
            file.mkdir();//创建文件夹
        }
        return subFolderName;
    }

    private static String getDateStr(Date date, String format) {
        Date tempDate = new Date();
        String tempFormat = "yyyy-MM-dd";
        if (date != null)
            tempDate = date;
        if (format != null)
            tempFormat = format;
        SimpleDateFormat df = new SimpleDateFormat(tempFormat);
        return df.format(tempDate);
    }

    /**
     * 写入TXT文件
     */
    public static void writeFile(String dirPath,String path,String txt) {
        try {
            new File(dirPath);

            File writeName = new File(path); // 相对路径，如果没有则要建立一个新的output.txt文件
            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
            try (FileWriter writer = new FileWriter(writeName);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                out.write(txt); // \r\n即为换行
                out.flush(); // 把缓存区内容压入文件
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
