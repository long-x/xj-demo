//package com.ecdata.cmp.activiti.utils;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.axiom.om.OMAbstractFactory;
//import org.apache.axiom.om.OMElement;
//import org.apache.axiom.om.OMFactory;
//import org.apache.axiom.om.OMNamespace;
//import org.apache.axiom.soap.SOAP11Constants;
//import org.apache.axis2.AxisFault;
//import org.apache.axis2.Constants;
//import org.apache.axis2.addressing.EndpointReference;
//import org.apache.axis2.client.Options;
//import org.apache.axis2.client.ServiceClient;
//import org.apache.axis2.rpc.client.RPCServiceClient;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Component;
//
//import javax.xml.namespace.QName;
//import java.util.List;
//
///**
// * 对接泛微ws
// *
// * @author xuxiaojian
// * @date 2020/4/10 13:53
// */
//@Component
//@Slf4j
//public class AxisClientUtils {
//    private static final String WSDL_URL = "http://10.233.0.9:8080/services/WorkflowService";
//    private static final String NAMESPACE_URL = "http://webservices.services.weaver.com.cn/";
//    private static RPCServiceClient serviceClient = null;
//
//    static {
//        try {
//            serviceClient = new RPCServiceClient();
//            Options options = serviceClient.getOptions();
//            //ws url注意后面不加?wsdl
//            EndpointReference targetEPR = new EndpointReference(WSDL_URL);
//            options.setTo(targetEPR);
//        } catch (AxisFault axisFault) {
//            axisFault.printStackTrace();
//        }
//    }
//
//    /**
//     * 根据人员对应邮箱，查找人员id
//     *
//     * @param filedType  查询的字段名称
//     * @param filedValue 查询的字段值
//     * @return String 返回结果
//     */
//    public static String getUserId(String filedType, String filedValue, String method) {
//        Object[] ret = null;
//        try {
//            //ws名命空间，方法名
//            QName opQName = new QName(NAMESPACE_URL, method);
//            //传递的参数值
//            Object[] param = new Object[]{filedType, filedValue};
//            //返回值类型
//            Class[] returnType = new Class[]{String[].class};
//
//            ret = serviceClient.invokeBlocking(opQName, param, returnType);
//        } catch (AxisFault e) {
//            log.error("获取泛微用户id异常", e);
//            return "";
//        }
//
//        if (ret != null && ret.length > 0) {
//            return ((String[]) ret[0])[0];
//        } else {
//            return "";
//        }
//    }
//
//    /**
//     * 执行创建流程
//     *
//     * @param info   流程信息
//     * @param userid 当前用户
//     * @return String 返回结果
//     */
//    public static void doCreateWorkflowRequest(WorkflowRequestInfo info, int userid) {
//        try {
//            ServiceClient client = new ServiceClient();
//            Options options = client.getOptions();
//            EndpointReference endpointReference = new EndpointReference(WSDL_URL);
//            options.setTo(endpointReference);
//            // 设置SOAPAction
//            options.setAction("http://webservices.workflow.weaver/doCreateWorkflowRequest");
//            // 设置soap版本
//            options.setSoapVersionURI(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
//
//            OMFactory factory = OMAbstractFactory.getOMFactory();
//
//            OMNamespace omNs = factory.createOMNamespace("http://schemas.xmlsoap.org/soap/envelope/", "soapenv");
//            // 命名空间
//            OMElement envelope = factory.createOMElement("Envelope", omNs);
//
////            OMElement header = factory.createOMElement("soapenv:Header", null);
////            OMElement body = factory.createOMElement("soapenv:Body", null);
//
//            OMElement method = assemblerCreateWorkflowRequest(factory, info, userid);
////            body.addChild(method);
////            envelope.addChild(header);
////            envelope.addChild(body);
//
//            OMElement result = client.sendReceive(method);
//            System.err.println(result);
////            System.err.println(result.getFirstElement().getText());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    //组装请求参数
//    private static OMElement assemblerCreateWorkflowRequest(OMFactory factory, WorkflowRequestInfo info, int userId) {
//        OMElement method = factory.createOMElement("web:doCreateWorkflowRequest", null);
//        OMElement in0 = factory.createOMElement("web:in0", null);
//
//        OMElement workflowBaseInfo = factory.createOMElement("web1:workflowBaseInfo", null);
//        OMElement workflowId = factory.createOMElement("web1:workflowId", null);
//        workflowId.addChild(factory.createOMText(workflowId, info.getWorkflowId()));
//        workflowBaseInfo.addChild(workflowId);
//
//        OMElement creatorId = factory.createOMElement("web1:creatorId", null);
//        creatorId.addChild(factory.createOMText(creatorId, info.getCreatorId()));
//
//        OMElement requestLevel = factory.createOMElement("web1:requestLevel", null);
//        requestLevel.addChild(factory.createOMText(requestLevel, info.getRequestLevel()));
//
//        OMElement requestName = factory.createOMElement("web1:requestName", null);
//        requestName.addChild(factory.createOMText(requestName, info.getRequestName()));
//
//        //------------------
//        OMElement workflowMainTableInfo = factory.createOMElement("web1:workflowMainTableInfo", null);
//        OMElement requestRecords = factory.createOMElement("web1:requestRecords", null);
//        OMElement WorkflowRequestTableRecord = factory.createOMElement("web1:WorkflowRequestTableRecord", null);
//        OMElement workflowRequestTableFields = factory.createOMElement("web1:workflowRequestTableFields", null);
//
//        List<WorkflowRequestTableField> fieldList = info.getWorkflowRequestTableField();
//
//        for (WorkflowRequestTableField field : fieldList) {
//            OMElement WorkflowRequestTableField1 = factory.createOMElement("web1:WorkflowRequestTableField", null);
//            OMElement fieldName1 = factory.createOMElement("web1:fieldName", null);
//            fieldName1.addChild(factory.createOMText(fieldName1, field.getFieldName()));
//            WorkflowRequestTableField1.addChild(fieldName1);
//
//            if (StringUtils.isNotBlank(field.getFieldType())) {
//                OMElement fieldType = factory.createOMElement("web1:fieldType", null);
//                fieldType.addChild(factory.createOMText(fieldType, field.getFieldType()));
//                WorkflowRequestTableField1.addChild(fieldType);
//            }
//
//            OMElement fieldValue1 = factory.createOMElement("web1:fieldValue", null);
//            fieldValue1.addChild(factory.createOMText(fieldValue1, field.getFieldValue()));
//            WorkflowRequestTableField1.addChild(fieldValue1);
//
//            OMElement edit1 = factory.createOMElement("web1:edit", null);
//            edit1.addChild(factory.createOMText(edit1, field.getEdit()));
//            WorkflowRequestTableField1.addChild(edit1);
//
//            OMElement view1 = factory.createOMElement("web1:view", null);
//            view1.addChild(factory.createOMText(view1, field.getView()));
//            WorkflowRequestTableField1.addChild(view1);
//            workflowRequestTableFields.addChild(WorkflowRequestTableField1);
//        }
//
//        WorkflowRequestTableRecord.addChild(workflowRequestTableFields);
//        requestRecords.addChild(WorkflowRequestTableRecord);
//        workflowMainTableInfo.addChild(requestRecords);
//        //-------------------
//
//        OMElement in1 = factory.createOMElement("web:in1", null);
//        in1.addChild(factory.createOMText(in1, String.valueOf(userId)));
//
//        in0.addChild(workflowBaseInfo);
//        in0.addChild(creatorId);
//        in0.addChild(requestLevel);
//        in0.addChild(requestName);
//        in0.addChild(workflowMainTableInfo);
//        method.addChild(in0);
//        method.addChild(in1);
//        method.build();
//
//        return method;
//    }
//
//}
