/**
 * WorkflowService.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecdata.cmp.activiti.webService;

/**
 * @author xxj
 */
public interface WorkflowService extends javax.xml.rpc.Service {
    public String getWorkflowServiceHttpPortAddress();

    public com.ecdata.cmp.activiti.webService.WorkflowServicePortType getWorkflowServiceHttpPort() throws javax.xml.rpc.ServiceException;

    public com.ecdata.cmp.activiti.webService.WorkflowServicePortType getWorkflowServiceHttpPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
