package com.ecdata.cmp.huawei.service;

/**
 * @author ：xuj
 * @date ：Created in 2020/5/15 15:08
 * @modified By：
 */
public interface OnOffMachineService {


    /**
     * 关闭/开启虚拟机
     * @param projectId
     * @param vmId
     * @return
     */
    boolean closeVm(String projectId,String vmId,String type);



    /**
     * 关闭开启裸金属服务器
     * @param projectId
     * @param BareId
     * @return
     */
    boolean closeBaremetal(String projectId,String BareId,String type);




}
