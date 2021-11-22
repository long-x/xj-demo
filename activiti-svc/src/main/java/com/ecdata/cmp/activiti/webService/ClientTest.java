package com.ecdata.cmp.activiti.webService;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;

/**
 * @author xuxiaojian
 * @date 2020/4/10 21:15
 */
@Slf4j
public class ClientTest {

//    public static void main(String[] args) throws ServiceException, RemoteException {
    public void test()throws ServiceException, RemoteException{
        WorkflowService workflowService = new WorkflowServiceLocator();
//
        WorkflowServicePortType workflowServiceHttpPort = workflowService.getWorkflowServiceHttpPort();
        log.info("徐江...进来没.....");
        String userId = workflowServiceHttpPort.getUserId("email", "szghygaoyang@chengtou.com");
        log.info("XJ userId {} ",userId);
        WorkflowRequestInfo ss = ss();
        String s = workflowServiceHttpPort.doCreateWorkflowRequest(ss, 2013);
        log.info(ss.toString());
        System.err.println(s);

//        workflowServiceHttpPort.submitWorkflowRequest(ss())

//        WorkflowServicePortType stub = new WorkflowServiceHttpBindingStub();
//        Call call = ((WorkflowServiceHttpBindingStub) stub).createCall();
//        call.setEncodingStyle();
//        String userId = stub.getUserId();

//        System.out.println(userId);

    }

    private static WorkflowRequestInfo ss() {

        WorkflowRequestTableField tableField1 = new WorkflowRequestTableField();
        tableField1.setFieldName("sqr");
        tableField1.setFieldValue("郜洋");
        tableField1.setEdit(true);
        tableField1.setView(true);

        WorkflowRequestTableField tableField2 = new WorkflowRequestTableField();
        tableField2.setFieldName("sqbm");
        tableField2.setFieldValue("城投研究院");
        tableField2.setEdit(true);
        tableField2.setView(true);

        WorkflowRequestTableField tableField6 = new WorkflowRequestTableField();
        tableField6.setFieldName("sqrq");
        tableField6.setFieldValue("2020-04-08");
        tableField6.setEdit(true);
        tableField6.setView(true);

        WorkflowRequestTableField tableField3 = new WorkflowRequestTableField();
        tableField3.setFieldName("zysx");
        tableField3.setFieldValue("泛微-云管平台测试");
        tableField3.setEdit(true);
        tableField3.setView(true);

        WorkflowRequestTableField tableField4 = new WorkflowRequestTableField();
        tableField4.setFieldName("sqpdf");
        tableField4.setFieldType("http:test.jpg;");
        tableField4.setFieldValue("http://10.235.0.20/interface/iaas-service/oa/download/87145588399394824");
        tableField4.setEdit(true);
        tableField4.setView(true);

        WorkflowRequestTableField tableField5 = new WorkflowRequestTableField();
        tableField5.setFieldName("qtfj");
        tableField5.setFieldType("http:");
        tableField5.setFieldValue("www.baidu.com");
        tableField5.setEdit(true);
        tableField5.setView(true);

        WorkflowRequestTableField[] workflowRequestTableFields = {tableField1, tableField2, tableField6, tableField3, tableField4, tableField5};

        WorkflowRequestTableRecord tableRecord = new WorkflowRequestTableRecord();
        tableRecord.setWorkflowRequestTableFields(workflowRequestTableFields);

        WorkflowRequestTableRecord[] requestRecords = {tableRecord};

        WorkflowMainTableInfo mainTableInfo = new WorkflowMainTableInfo();
        mainTableInfo.setRequestRecords(requestRecords);

        WorkflowRequestInfo workflowRequestInfo = new WorkflowRequestInfo();
        workflowRequestInfo.setWorkflowMainTableInfo(mainTableInfo);
        workflowRequestInfo.setCreatorId("2013");
        workflowRequestInfo.setRequestLevel("0");
        workflowRequestInfo.setRequestName("云资源申请");

        WorkflowBaseInfo workflowBaseInfo = new WorkflowBaseInfo();
//        workflowBaseInfo.setWorkflowId("54008");
        workflowBaseInfo.setWorkflowId("63006");
        workflowRequestInfo.setWorkflowBaseInfo(workflowBaseInfo);

        return workflowRequestInfo;
    }
}
