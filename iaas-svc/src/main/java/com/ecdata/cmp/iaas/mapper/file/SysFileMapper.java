package com.ecdata.cmp.iaas.mapper.file;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.SysFile;
import com.ecdata.cmp.iaas.entity.dto.file.FileVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysFileMapper extends BaseMapper<SysFile> {
    int deleteFile(String id);

    List<FileVo> queryFileByIds(List<String> list);
}