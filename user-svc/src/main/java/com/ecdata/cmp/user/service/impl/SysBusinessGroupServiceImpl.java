package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.user.dto.DistributionBlockDTO;
import com.ecdata.cmp.user.dto.DistributionDTO;
import com.ecdata.cmp.user.dto.SysBusinessGroupResourcePoolVO;
import com.ecdata.cmp.user.dto.SysBusinessGroupVO;
import com.ecdata.cmp.user.entity.SysBusinessGroup;
import com.ecdata.cmp.user.mapper.SysBusinessGroupMapper;
import com.ecdata.cmp.user.service.ISysBusinessGroupService;
import com.ecdata.cmp.user.utils.SysBusinessGroupTreeUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/20 13:38
 * @modified By：
 */
@Service
public class SysBusinessGroupServiceImpl extends ServiceImpl<SysBusinessGroupMapper, SysBusinessGroup> implements ISysBusinessGroupService {

    @Override
    public void insert(List<Map<String, Long>> list) {
        baseMapper.insert(list);
    }

    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id, updateUser);
    }


    @Override
    public void update(SysBusinessGroup sysBusinessGroup) {
        baseMapper.update(sysBusinessGroup);
    }

    @Override
    public SysBusinessGroup load(int id) {
        return baseMapper.load(id);
    }

    @Override
    public Map<String, Object> pageList(int offset, int pagesize) {
        //return baseMapper.pageList(offset,pagesize);
        return null;
    }

    @Override
    public List<SysBusinessGroupVO> qrySysBusinessGroupInfo(String keyword) {
        List<SysBusinessGroupVO> result = baseMapper.qrySysBusinessGroupInfo(keyword);
//        List<SysBusinessGroupVO> records = result.getRecords();
        List<SysBusinessGroupVO> treeList = new SysBusinessGroupTreeUtil().getTreeList(result);

//        records.clear();
//        result.setRecords(treeList);
        return treeList;
    }

    @Override
    public List<SysBusinessGroupVO> getlistByPoolId(Long poolId) {
        List<SysBusinessGroupVO> result = baseMapper.getlistByPoolId(poolId);
        return result;
    }

    @Override
    public List<SysBusinessGroupVO> getDisBusinessGroupName(String businessGroupName) {
        List<SysBusinessGroupVO> result = baseMapper.getDisBusinessGroupName(businessGroupName);
        return result;
    }

    @Override
    public List<SysBusinessGroupVO> qrGroupByUserId(String userId) {
        return baseMapper.qrGroupByUserId(userId);
    }

    @Override
    public IPage<SysBusinessGroupVO> getGroupList(Page page, String keyword) {
        return baseMapper.getGroupList(page, keyword);
    }

    @Override
    public IPage<SysBusinessGroupResourcePoolVO> getIaasResourcePpool(Page page, Long groupId, String keyword) {
        return baseMapper.getIaasResourcePpool(page, groupId, keyword);
    }

    /**
     * 业务组关联资源池信息 查看
     *
     * @param groupId
     * @return
     */
    @Override
    public List<SysBusinessGroupResourcePoolVO> getIaasResourcePpool(long groupId) {
        return baseMapper.getIaasResourcePpool2(groupId);
    }

    @Override
    public List<SysBusinessGroupResourcePoolVO> getResourcePoolList(List poolId) {
        return baseMapper.getResourcePoolList(poolId);
    }

    @Override
    public List<Long> getPoolIds(Map<String, Object> map) {
        return this.baseMapper.getPoolIds(map);
    }

    @Override
    public DistributionDTO getUserDistribution() {
        DistributionDTO distribution = new DistributionDTO();
        Integer totalNum = 0;
        List<DistributionBlockDTO> blockList = this.baseMapper.getUserDistribution();
        if (blockList != null && blockList.size() > 0) {
            totalNum = blockList.size();
            Integer sum = 0;
            for (DistributionBlockDTO block : blockList) {
                sum += block.getNum();
            }
            for (DistributionBlockDTO block : blockList) {
                block.setRate(Double.valueOf(block.getNum()) / sum);
            }
        }
        distribution.setTotalNum(totalNum);
        distribution.setBlockList(blockList);
        return distribution;

    }

    @Override
    public List<SysBusinessGroupVO> getBusinessGroupByUser(String userId) {
        return baseMapper.qrGroupByUserId(userId);
    }

    /**
     * 删除业务组 条件查询
     *
     * @param id
     * @return
     */
    @Override
    public boolean getParentGroup(String id) {
        Long group = baseMapper.getParentGroup(id);
        Long member = baseMapper.getGroupMemberById(id);
        Long pool = baseMapper.getGroupResourcePoolById(id);
        if (group > 0 || member > 0 || pool > 0) {
            return false;
        }
        return true;
    }

    @Override
    public SysBusinessGroup getBusinessGroupById(String id) {
        return baseMapper.selectById(id);
    }
}
