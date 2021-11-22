package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.ecdata.cmp.user.entity.SysNotificationReceiver;
import com.ecdata.cmp.user.mapper.SysNotificationReceiverMapper;
import com.ecdata.cmp.user.service.SysNotificationReceiverService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-05 16:33
 */
@Slf4j
@Service
public class SysNotificationReceiverServiceImpl extends ServiceImpl<SysNotificationReceiverMapper, SysNotificationReceiver> implements SysNotificationReceiverService {

    @Override
    public void updateUserMessageStatus(Long userId){
        baseMapper.updateUserMessageStatus(userId);
    }

    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id, updateUser);
    }

    public Boolean removeOne(Long id, Long updateUser) {
        log.info("删除用户通知 sysNotificationReceiver id：{}", id);
        if (SqlHelper.delBool(this.baseMapper.deleteById(id))) {
            baseMapper.modifyUpdateRecord(id, updateUser);
            return true;
        } else {
            return false;
        }
    }
    /*   */

    @Override
    public Integer batchRemove(String ids, Long updateUser){
        if(StringUtils.isBlank(ids)){
            return null;
        }
        int count =0;
        String[] str = ids.split(",");
        for(int i=0; i<str.length; i++){
            Boolean result = removeOne(Long.valueOf(str[i]), updateUser);
            if(!result){
                count++;
            }
        }
        return count;
    }
}
