/**
 * WorkflowServicePortType.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecdata.cmp.activiti.webService;

/**
 * @author xxj
 */
public interface WorkflowServicePortType extends java.rmi.Remote {
    public String forward2WorkflowRequest(int in0, String in1, String in2, int in3, String in4) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowRequestInfo[] getAllWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowRequestInfo getWorkflowRequest(int in0, int in1, int in2) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowRequestInfo[] getHendledWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowRequestInfo[] getToDoWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowRequestInfo getWorkflowRequest4Split(int in0, int in1, int in2, int in3) throws java.rmi.RemoteException;

    public String submitWorkflowRequest(com.ecdata.cmp.activiti.webService.WorkflowRequestInfo in0, int in1, int in2, String in3, String in4) throws java.rmi.RemoteException;

    public int getHendledWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException;

    public String getLeaveDays(String in0, String in1, String in2, String in3, String in4) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowRequestInfo[] getHendledWorkflowRequestList4Ofs(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowBaseInfo[] getCreateWorkflowList(int in0, int in1, int in2, int in3, int in4, String[] in5) throws java.rmi.RemoteException;

    public int getToBeReadWorkflowRequestCount(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException;

    public int getCreateWorkflowCount(int in0, int in1, String[] in2) throws java.rmi.RemoteException;

    public int getProcessedWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException;

    public String doCreateRequest(com.ecdata.cmp.activiti.webService.WorkflowRequestInfo in0, int in1) throws java.rmi.RemoteException;

    public String forwardWorkflowRequest(int in0, String in1, String in2, int in3, String in4) throws java.rmi.RemoteException;

    public String doCreateWorkflowRequest(com.ecdata.cmp.activiti.webService.WorkflowRequestInfo in0, int in1) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowRequestInfo[] getToBeReadWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException;

    public String doForceOver(int in0, int in1) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowRequestInfo[] getBeRejectWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException;

    public int getCCWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowRequestInfo[] getProcessedWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException;

    public int getAllWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowRequestInfo getCreateWorkflowRequestInfo(int in0, int in1) throws java.rmi.RemoteException;

    public int getDoingWorkflowRequestCount(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowRequestInfo[] getForwardWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowRequestInfo[] getMyWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowBaseInfo[] getCreateWorkflowTypeList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException;

    public int getMyWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException;

    public String[] getWorkflowNewFlag(String[] in0, String in1) throws java.rmi.RemoteException;

    public void writeWorkflowReadFlag(String in0, String in1) throws java.rmi.RemoteException;

    public int getHendledWorkflowRequestCount4Ofs(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException;

    public int getForwardWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException;

    public int getToDoWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException;

    public String givingOpinions(int in0, int in1, String in2) throws java.rmi.RemoteException;

    public int getCreateWorkflowTypeCount(int in0, String[] in1) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowRequestLog[] getWorkflowRequestLogs(String in0, String in1, int in2, int in3, int in4) throws java.rmi.RemoteException;

    public boolean deleteRequest(int in0, int in1) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowRequestInfo[] getCCWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException;

    public String getUserId(String in0, String in1) throws java.rmi.RemoteException;

    public int getBeRejectWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException;

    public com.ecdata.cmp.activiti.webService.WorkflowRequestInfo[] getDoingWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException;
}
