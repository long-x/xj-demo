/**
 * WorkflowServiceLocator.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecdata.cmp.activiti.webService;

/**
 * @author xxj
 */
public class WorkflowServiceLocator extends org.apache.axis.client.Service implements com.ecdata.cmp.activiti.webService.WorkflowService {

    public WorkflowServiceLocator() {
    }


    public WorkflowServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WorkflowServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WorkflowServiceHttpPort
//    private String WorkflowServiceHttpPort_address = "http://10.233.0.9:8080/services/WorkflowService";
    private String WorkflowServiceHttpPort_address = "http://10.230.0.22/services/WorkflowService";
    @Override
    public String getWorkflowServiceHttpPortAddress() {
        return WorkflowServiceHttpPort_address;
    }

    // The WSDD service name defaults to the port name.
    private String WorkflowServiceHttpPortWSDDServiceName = "WorkflowServiceHttpPort";

    public String getWorkflowServiceHttpPortWSDDServiceName() {
        return WorkflowServiceHttpPortWSDDServiceName;
    }

    public void setWorkflowServiceHttpPortWSDDServiceName(String name) {
        WorkflowServiceHttpPortWSDDServiceName = name;
    }

    @Override
    public com.ecdata.cmp.activiti.webService.WorkflowServicePortType getWorkflowServiceHttpPort() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WorkflowServiceHttpPort_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWorkflowServiceHttpPort(endpoint);
    }

    @Override
    public com.ecdata.cmp.activiti.webService.WorkflowServicePortType getWorkflowServiceHttpPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.ecdata.cmp.activiti.webService.WorkflowServiceHttpBindingStub _stub = new com.ecdata.cmp.activiti.webService.WorkflowServiceHttpBindingStub(portAddress, this);
            _stub.setPortName(getWorkflowServiceHttpPortWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWorkflowServiceHttpPortEndpointAddress(String address) {
        WorkflowServiceHttpPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    @Override
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.ecdata.cmp.activiti.webService.WorkflowServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.ecdata.cmp.activiti.webService.WorkflowServiceHttpBindingStub _stub = new com.ecdata.cmp.activiti.webService.WorkflowServiceHttpBindingStub(new java.net.URL(WorkflowServiceHttpPort_address), this);
                _stub.setPortName(getWorkflowServiceHttpPortWSDDServiceName());
                return _stub;
            }
        } catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    @Override
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("WorkflowServiceHttpPort".equals(inputPortName)) {
            return getWorkflowServiceHttpPort();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    @Override
    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "WorkflowService");
    }

    private java.util.HashSet ports = null;

    @Override
    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "WorkflowServiceHttpPort"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {

        if ("WorkflowServiceHttpPort".equals(portName)) {
            setWorkflowServiceHttpPortEndpointAddress(address);
        } else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
