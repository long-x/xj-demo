package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.huawei.dto.response.ProjectsListResponse;
import com.ecdata.cmp.huawei.dto.vo.ProjectsVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.service.ProjectsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/10 20:31
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/projects")
@Api(tags = "项目")
public class ProjectsController {

    @Autowired
    private ProjectsService projectsService;

    @PutMapping("/get_project_list")
    @ApiOperation(value = "查项目(运营面Token)", notes = "查项目(运营面Token)")
    public ResponseEntity<ProjectsListResponse> getProjectList(@RequestBody RequestVO requestVO) {
        try {
            List<ProjectsVO> list = projectsService.getProjectList(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new ProjectsListResponse(list));
        } catch (Exception e) {
            log.info("getProjectList error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
