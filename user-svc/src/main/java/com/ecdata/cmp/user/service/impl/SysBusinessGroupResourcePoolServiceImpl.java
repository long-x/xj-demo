package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.SysBusinessGroupResourcePoolVO;
import com.ecdata.cmp.user.dto.SysBusinessMemberAndUserAndPoolVO;
import com.ecdata.cmp.user.dto.SysBusinessPoolVO;
import com.ecdata.cmp.user.entity.SysBusinessGroupResourcePool;
import com.ecdata.cmp.user.mapper.SysBusinessGroupResourcePoolMapper;
import com.ecdata.cmp.user.service.ISysBusinessGroupResourcePoolService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author ：xuj
 * @date ：Created in 2019/11/21 10:07
 * @modified By：
 */
@Transactional
@Service
public class SysBusinessGroupResourcePoolServiceImpl
        extends ServiceImpl<SysBusinessGroupResourcePoolMapper,SysBusinessGroupResourcePool>
        implements ISysBusinessGroupResourcePoolService{


    @Override
    public int deleteById(Long id) {
        return baseMapper.deleteById(id);
    }

    @Override
    public void update(SysBusinessGroupResourcePool sysBusinessGroupResourcePool) { }

    @Override
    public SysBusinessGroupResourcePool load(int id) {
        return null;
    }

    @Override
    public List<String> getGroupByPoolId(Long poolId) {
        return baseMapper.getGroupByPoolId(poolId);
    }


    /**
     * 调用mybatis plus 在自己的service事务控制
     * @param poolVOList
     * @return
     */
    @Override
    public boolean save(List <SysBusinessPoolVO> poolVOList){
        for (SysBusinessPoolVO poolVO:poolVOList) {
            SysBusinessGroupResourcePool pool = new SysBusinessGroupResourcePool();
            BeanUtils.copyProperties(poolVO,pool);
            pool.setId(SnowFlakeIdGenerator.getInstance().nextId());
            pool.setCreateTime(DateUtil.getNow());
            if (!super.save(pool)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean updateByIds(List<SysBusinessPoolVO> poolVOList) {
        for (SysBusinessPoolVO poolVO:poolVOList) {
            SysBusinessGroupResourcePool pool = new SysBusinessGroupResourcePool();
            BeanUtils.copyProperties(poolVO,pool);
            if (pool.getId() == null) {
                return false;
            }
            if (!super.updateById(pool)) {
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean deleteByPoolId(List<SysBusinessPoolVO> poolVOList) {
        for (SysBusinessPoolVO poolVO:poolVOList) {
            SysBusinessGroupResourcePool pool = new SysBusinessGroupResourcePool();
            BeanUtils.copyProperties(poolVO,pool);
            Long poolId = pool.getPoolId();
            if (baseMapper.deleteByPoolId(poolId)<0) {
                return false;
            }
        }
        return true;
    }


    /**
     * 关联资源池，删除资源池
     * 不管是新增，修改，删除 进行一次删除 再插入
     * 保证事务一致性
     */
    @Override
    public boolean updateCorrelationPool(SysBusinessMemberAndUserAndPoolVO memberAndUserVO) {
        //获取businessGroupId 删除库里面的数据
        Long businessGroupId = memberAndUserVO.getBusinessGroupId();
//        List<String> poolIds = memberAndUserVO.getPoolId();
        List<SysBusinessGroupResourcePoolVO> poolList = memberAndUserVO.getPoolList();
        baseMapper.deletePoolByGroupId(businessGroupId);
            for (SysBusinessGroupResourcePoolVO poolVO:poolList) {
                SysBusinessGroupResourcePool pool = new SysBusinessGroupResourcePool();
                pool.setBusinessGroupId(businessGroupId);
                pool.setPoolId(poolVO.getId());
                pool.setId(SnowFlakeIdGenerator.getInstance().nextId());
                pool.setCreateTime(DateUtil.getNow());
                pool.setCreateUser(Sign.getUserId());
                pool.setType(poolVO.getType());
                if(!super.save(pool)){
                    return false;
                }
            }
        return true;
    }

    @Override
    public IPage<SysBusinessPoolVO> qrySysBusinessPoolInfo(Page<SysBusinessPoolVO> page, String keyword) {
        IPage<SysBusinessPoolVO> result = baseMapper.qrySysBusinessPoolInfo(page, keyword);
        return result;
    }


}
