package com.ecdata.cmp.iaas.controller;

import com.alibaba.nacos.client.utils.StringUtils;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.dto.file.FileVo;
import com.ecdata.cmp.iaas.utils.FileUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-04-01 13:52
 */
@RestController
@RequestMapping("/v1/file")
@Slf4j
public class SysFileController {
    private static Logger logger = LoggerFactory.getLogger(SysFileController.class);

    @Autowired
    private FileUtil fileUtil;

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    public FileVo uploadFile(@RequestBody MultipartFile file) {
        try {
            FileVo fileVo = fileUtil.saveFile(file, dateStr(new Date()), null);
            fileVo.setCode("200");
            log.info("fileVo {}",fileVo);
            return fileVo;
        } catch (IOException e) {
            e.printStackTrace();
            return new FileVo().setCode("500");
        }
    }

    @GetMapping(value = "/downFile")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @ApiImplicitParams({@ApiImplicitParam(name = "fileId", value = "文件id", paramType = "query", dataType = "string")})
    public void downFile(@RequestParam(required = true) String fileId, HttpServletResponse response) {
        fileUtil.downloadFile(response, fileId);
    }

    @GetMapping(value = "/deleteFile")
    @ApiOperation(value = "删除文件", notes = "删除文件")
    @ApiImplicitParams({@ApiImplicitParam(name = "fileId", value = "文件id", paramType = "query", dataType = "string")})
    public BaseResponse delete(@RequestParam(required = true) String fileId) {
        BaseResponse baseResponse = new BaseResponse();
        if (StringUtils.isBlank(fileId)) {
            baseResponse.setCode(201);
            baseResponse.setMessage("文件id不能为空！");
            return baseResponse;
        }
        try {
            fileUtil.delete(fileId);
        } catch (Exception e) {
            baseResponse.setCode(201);
            baseResponse.setMessage("删除文件失败！");
        }
        return baseResponse;
    }

    @RequestMapping(value = "/queryFile", method = RequestMethod.GET)
    @ResponseBody
    public List<FileVo> queryFile(String fileId) {
        return fileUtil.queryFile(Arrays.asList(fileId.split(",")).stream().map(s -> s.trim()).collect(Collectors.toList()));
    }

    @RequestMapping(value = "/document", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> uploadStationPic(@RequestParam("file") MultipartFile[] updateFiles) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<String> uploadFileNameList = new ArrayList<String>();
            for (MultipartFile file : updateFiles) {

//                uploadFileNameList.add(fileUtil.uploadFile(file.getInputStream(), file.getOriginalFilename(), dateStr(new Date())));
            }

            map.put("resultCode", "200");
            map.put("data", uploadFileNameList);
            logger.info("Upload file is successful {}", uploadFileNameList);
            return map;
        } catch (Exception e) {
            logger.error("upload error,{}", e);

            map.put("resultCode", "201");
            map.put("resultMsg", "上传失败");
            map.put("data", "");
            return map;
        }
    }

    private String dateStr(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(date);
    }
}
