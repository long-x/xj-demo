package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.IaasResourcePoolDatastore;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolDatastoreVO;
import com.ecdata.cmp.iaas.entity.dto.response.ResourcePoolDatastoreAsResponse;
import com.ecdata.cmp.iaas.entity.dto.response.ResourcePoolDatastoreListResponse;
import com.ecdata.cmp.iaas.entity.dto.response.ResourcePoolDatastorePageResponse;
import com.ecdata.cmp.iaas.service.IResourcePoolDatastoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @title: ResourcePoolDatastore Controller
 * @Author: shig
 * @description: 资源池存储 控制层
 * @Date: 2019/11/19 4:01 下午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/resourcePoolDatastore")
@Api(tags = "🔥资源池存储相关的API")
public class ResourcePoolDatastoreController {
    /**
     * 资源池存储(resourcePoolDatastore) service
     */
    @Autowired
    private IResourcePoolDatastoreService resourcePoolDatastoreService;

    /**
     * 新增:/v1/resourcePoolDatastore/add
     *
     * @param resourcePoolDatastoreVO
     * @return BaseResponse
     * @throws Exception
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增资源池存储", notes = "新增资源池存储")
    public ResponseEntity<BaseResponse> add(@RequestBody ResourcePoolDatastoreVO resourcePoolDatastoreVO) throws Exception {
        IaasResourcePoolDatastore resourcePoolDatastore = new IaasResourcePoolDatastore();
        //保存IaasResourcePoolDatastore
        saveIaasResourcePoolDatastore(resourcePoolDatastoreVO, resourcePoolDatastore);

        //响应基础对象
        BaseResponse baseResponse = null;
        //新增：成功、失败
        if (resourcePoolDatastoreService.save(resourcePoolDatastore)) {
            baseResponse = new BaseResponse();
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse = new BaseResponse();
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/removeByPoolId")
    @ApiOperation(value = "根据资源池id删除资源池存储", notes = "根据资源池id删除资源池存储")
    public ResponseEntity<BaseResponse> removeByPoolId(@RequestParam Long poolId) {
        log.info("删除资源池存储 id：{}", poolId);
        BaseResponse baseResponse = new BaseResponse();
        if (poolId != null) {
            //逻辑删除
//            resourcePoolDatastoreService.removeByPoolId(poolId, Sign.getUserId());
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("删除失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PostMapping("/addList")
    @ApiOperation(value = "新增资源池存储list集合", notes = "新增资源池存储list集合")
    public ResponseEntity<BaseResponse> addList(@RequestBody List<ResourcePoolDatastoreVO> resourcePoolDatastoreVOs) throws Exception {
        //响应基础对象
        BaseResponse baseResponse = new BaseResponse();
        for (ResourcePoolDatastoreVO resourcePoolDatastoreVO : resourcePoolDatastoreVOs) {
            //修改:资源池存储id
            if (resourcePoolDatastoreVO.getPoolDatastoreId() != null) {
                IaasResourcePoolDatastore resourcePoolDatastore = new IaasResourcePoolDatastore();
                BeanUtils.copyProperties(resourcePoolDatastoreVO, resourcePoolDatastore);
                resourcePoolDatastoreService.updateById(resourcePoolDatastore);
            } else {
                //新增
                IaasResourcePoolDatastore resourcePoolDatastore = new IaasResourcePoolDatastore();
                //保存IaasResourcePoolDatastore
                saveIaasResourcePoolDatastore(resourcePoolDatastoreVO, resourcePoolDatastore);
                //新增：成功、失败
                if (resourcePoolDatastoreService.save(resourcePoolDatastore)) {
                    baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                    baseResponse.setMessage("添加成功");
                } else {
                    baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                    baseResponse.setMessage("添加失败");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    /**
     * 保存IaasResourcePoolDatastore
     *
     * @param resourcePoolDatastoreVO
     * @param resourcePoolDatastore
     */
    private void saveIaasResourcePoolDatastore(@RequestBody ResourcePoolDatastoreVO resourcePoolDatastoreVO, IaasResourcePoolDatastore resourcePoolDatastore) {
        //拓展类属性复制给 IaasResourcePoolDatastore
        BeanUtils.copyProperties(resourcePoolDatastoreVO, resourcePoolDatastore);
        //赋值ID，先获取封装的id
        resourcePoolDatastore.setId(SnowFlakeIdGenerator.getInstance().nextId());
        //主机存储id
        resourcePoolDatastore.setDatastoreId(resourcePoolDatastoreVO.getId().toString());
        //createUser+time
        resourcePoolDatastore.setCreateUser(Sign.getUserId());
        resourcePoolDatastore.setCreateTime(DateUtil.getNow());
        //isDeleted:0 未删除数据，boolean false 0，默认为 0
    }


    /**
     * 修改：/resourcePoolDatastore/update
     *
     * @param resourcePoolDatastoreVO
     * @return BaseResponse
     * @throws Exception
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改资源池存储", notes = "修改资源池存储")
    public ResponseEntity<BaseResponse> update(@RequestBody ResourcePoolDatastoreVO resourcePoolDatastoreVO) throws Exception {
        //连接池对象
        IaasResourcePoolDatastore resourcePoolDatastore = new IaasResourcePoolDatastore();
        /**
         * 资源池存储修改：IaasResourcePoolDatastore
         */
        if (updateIaasResourcePoolDatastore(resourcePoolDatastoreVO, resourcePoolDatastore))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.MISS_PRIMARY_KEY));

        //响应
        BaseResponse baseResponse = null;
        if (resourcePoolDatastoreService.updateById(resourcePoolDatastore)) {
            baseResponse = new BaseResponse();
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse = new BaseResponse();
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    /**
     * 资源池存储修改：IaasResourcePoolDatastore
     *
     * @param resourcePoolDatastoreVO
     * @param resourcePoolDatastore
     * @return
     */
    private boolean updateIaasResourcePoolDatastore(@RequestBody ResourcePoolDatastoreVO resourcePoolDatastoreVO, IaasResourcePoolDatastore resourcePoolDatastore) {
        BeanUtils.copyProperties(resourcePoolDatastoreVO, resourcePoolDatastore);
        //响应失败，缺少主键
        if (resourcePoolDatastore.getId() == null) {
            return true;
        }
        //update user+time
        resourcePoolDatastore.setUpdateUser(Sign.getUserId());
        resourcePoolDatastore.setUpdateTime(DateUtil.getNow());
        return false;
    }

    /**
     * 删除（逻辑删除）,0 false 删除
     *
     * @param id
     * @return BaseResponse
     */
    @PutMapping("/remove")
    @ApiOperation(value = "删除资源池存储", notes = "删除资源池存储(逻辑删除)")
    public ResponseEntity<BaseResponse> remove(@RequestParam Long id) {
        log.info("删除资源池存储 user id：{}", id);
        BaseResponse baseResponse = new BaseResponse();
        if (resourcePoolDatastoreService.getById(id) != null) {
            //逻辑删除
            resourcePoolDatastoreService.updateResourcePoolDatastore(id, Sign.getUserId());
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("删除失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return BaseResponse
     */
    @PutMapping("/removeBatch")
    @ApiOperation(value = "批量删除资源池存储", notes = "批量删除资源池存储(逻辑删除)")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("批量删除资源池存储 ids：{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        //ids参数为空
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("参数不识别");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            //删除
            this.resourcePoolDatastoreService.removeByIds(Arrays.asList(idArray));
            for (String id : idArray) {
                this.resourcePoolDatastoreService.updateResourcePoolDatastore(Long.parseLong(id), Sign.getUserId());
            }
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }

    /**
     * 获取资源池存储列表
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取资源池存储列表", notes = "获取资源池存储列表")
    public ResponseEntity<ResourcePoolDatastoreListResponse> list() {
        List<ResourcePoolDatastoreVO> resourcePoolDatastoreVOList = new ArrayList<>();
        //查询
        List<IaasResourcePoolDatastore> resourcePoolDatastoreList = resourcePoolDatastoreService.list();
        //不为空，复制到拓展类
        if (resourcePoolDatastoreList != null && resourcePoolDatastoreList.size() > 0) {
            for (IaasResourcePoolDatastore resourcePoolDatastore : resourcePoolDatastoreList) {
                ResourcePoolDatastoreVO resourcePoolDatastoreVO = new ResourcePoolDatastoreVO();
                //资源池存储
                BeanUtils.copyProperties(resourcePoolDatastore, resourcePoolDatastoreVO);
                //封装返回的大对象
                resourcePoolDatastoreVOList.add(resourcePoolDatastoreVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResourcePoolDatastoreListResponse(resourcePoolDatastoreVOList));
    }

    /**
     * 根据id 获取资源池存储信息
     *
     * @param id
     * @return
     */
    @GetMapping("/info")
    @ApiOperation(value = "获取资源池存储信息", notes = "获取资源池存储信息")
    @ApiImplicitParam(name = "id", value = "资源池存储id", paramType = "query", dataType = "long")
    public ResponseEntity<ResourcePoolDatastoreAsResponse> info(@RequestParam(required = false) Long id) {
        ResourcePoolDatastoreAsResponse resourcePoolDatastoreAsResponse = new ResourcePoolDatastoreAsResponse();
        ResourcePoolDatastoreVO resourcePoolDatastoreVO = new ResourcePoolDatastoreVO();
        if (id == null) {
            id = Sign.getUserId();
        }
        //查询改id是否存在
        IaasResourcePoolDatastore resourcePoolDatastore = resourcePoolDatastoreService.getById(id);
        if (resourcePoolDatastore == null) {
            return ResponseEntity.status(HttpStatus.OK).body(resourcePoolDatastoreAsResponse);
        }
        BeanUtils.copyProperties(resourcePoolDatastore, resourcePoolDatastoreVO);

        resourcePoolDatastoreAsResponse.setData(resourcePoolDatastoreVO);
        return ResponseEntity.status(HttpStatus.OK).body(resourcePoolDatastoreAsResponse);
    }

    /**
     * 分页查询资源池存储
     *
     * @param pageNo   当前页
     * @param pageSize 每页的数量
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "根据集群ID分页查看资源池存储", notes = "根据集群ID分页查看资源池存储")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "100"),
            @ApiImplicitParam(name = "clusterId", value = "资源入口", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "poolId", value = "资源池id", paramType = "query", dataType = "long"),
    })
    public ResponseEntity<ResourcePoolDatastorePageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                                  @RequestParam(defaultValue = "100", required = false) Integer pageSize,
                                                                  @RequestParam(required = false) Long clusterId,
                                                                  @RequestParam(required = false) Long poolId) {
        Page<ResourcePoolDatastoreVO> page = new Page<>(pageNo, pageSize);
        //调用分页查询方法
        Map<String, Object> params = new HashMap<>();
        if (clusterId != null) {
            params.put("clusterId", clusterId);
        }
        if (poolId != null) {
            params.put("poolId", poolId);
        }
        IPage<ResourcePoolDatastoreVO> result = resourcePoolDatastoreService.queryResourcePoolDatastorePage(page, params);
        //响应编码设置
        return ResponseEntity.status(HttpStatus.OK).body(new ResourcePoolDatastorePageResponse(new PageVO<>(result)));
    }

}