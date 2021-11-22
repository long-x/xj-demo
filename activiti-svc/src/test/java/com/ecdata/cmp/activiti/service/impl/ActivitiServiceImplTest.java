package com.ecdata.cmp.activiti.service.impl;

import com.ecdata.cmp.activiti.dto.vo.ActProcessDefinitionVO;
import com.ecdata.cmp.activiti.entity.ActDeploymentEntity;
import com.ecdata.cmp.activiti.entity.ActProcessDefinitionEntity;
import com.ecdata.cmp.activiti.service.IActivitiService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;


@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ActivitiServiceImplTest {

    @Autowired
    private IActivitiService activitiService;

    @Autowired
    private ProcessEngineFactoryBean processEngineFactoryBean;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void deployProcess() {
        String deploymentName = "审批流程2";
        String zipPath = "bpmn/secend_approve.zip";
        activitiService.deployProcess(deploymentName, zipPath);

//        ProcessEngineConfigurationImpl configuration = processEngineFactoryBean.getProcessEngineConfiguration();
//        ProcessEngine engine = configuration.buildProcessEngine();
//        RepositoryService service = engine.getRepositoryService();
//        Deployment deployment = service.createDeployment().addClasspathResource("bpmn/secend_approve.bpmn20.xml").deploy();
//        String id = deployment.getId();
//        ProcessDefinition processDefinition = service.
//                createProcessDefinitionQuery().
//                deploymentId(id).singleResult();
//        log.info("流程定义对象 [{}],流程ID [{}]",processDefinition.getName(),processDefinition.getId());


    }


    @Test
    //查询最新流程 ACT_RE_PROCDEF
    public void queryLatestVersionProcessDefinition() {
        List<ActProcessDefinitionVO> list = activitiService.queryLatestVersionProcessDefinition();
        log.info("输出list {}", list);
    }

    @Test
    //根据ACT_RE_DEPLOYMENT 里面的name id 去查ACT_RE_PROCDEF记录
    public void queryProcessDefinitionByDeploymentName() {
        List<ActProcessDefinitionEntity> list = activitiService.queryProcessDefinitionByDeploymentName("审批流程2");
        log.info("输出list {}", list);
    }


    @Test
    //根据 ACT_RU_TASK 表的id 查询 去查ACT_RE_PROCDEF记录
    public void queryProcessDefinitionByTaskId() {
        ActProcessDefinitionEntity approve = activitiService.queryProcessDefinitionByTaskId("27508");
        log.info("输出 {}", approve);
    }


    @Test
    public void deploymentProcessDefinition_zip() {
        //ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("bpmn/secend_approve.zip");
        ZipInputStream zipInputStream = new ZipInputStream(in);
        Deployment deployment = repositoryService.createDeployment()//与流程定义和部署对象相关的Service//创建一个部署对象
                .name("流程定义")//添加部署名称
                .addZipInputStream(zipInputStream)//完成zip文件的部署
                .deploy();//完成部署
        System.out.println("部署ID：" + deployment.getId());
        System.out.println("部署名称:" + deployment.getName());

    }


    @Test
    public void queryProcessDeployment() {
        ActDeploymentEntity actDeploymentEntity = new ActDeploymentEntity();
        actDeploymentEntity.setId("40001");
        List<ActDeploymentEntity> entities = activitiService.queryProcessDeployment(actDeploymentEntity);
        log.info("部署流程 {}", entities.toString());
    }


    /**
     * 开始流程
     *
     */
    @Test
    public void startProcess() {
        String processDefinitionKey = "second_approve";
        String processType = "vm";
        Integer businessId = 00000001;
        Integer userId = 110110;
//        activitiService.startProcess(processDefinitionKey, processType, businessId, userId);
    }


    @Test
    public void queryTaskId() {
        String processType = "vm";
        Integer businessId = 00000001;
//        String id = activitiService.queryTaskId(processType, businessId);
//        log.info("id {}", id);
    }


    @Test
    public void queryAssigneeTask() {
        String userId = "110110";
        String processType = "";
//        List<Task> list = activitiService.queryAssigneeTask(userId, processType);
//        log.info("返回 {}", list.toString());
    }


//    @Test
//    public void getTenantId() {
//        String tenantId = activitiService.getTenantId();
//        log.info("租户id {}", tenantId);
//
//    }

    @Test
    public void completeTask() {
        String taskId = "57512";
        // 1.根据任务id查询任务对象
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 2.从任务里面取出流程实例id
        String processInstanceId = task.getProcessInstanceId();
        log.info("processInstanceId = {}", processInstanceId);

        // 3.设置批注人
        Authentication.setAuthenticatedUserId("10086");
        // 4.添加批注信息
        Comment comment = taskService.addComment(taskId, processInstanceId, "同意否");
        log.info("comment ={}", comment);
        // 5.完成任务,并指定流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("outcome", "同意否");
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId);
        // 完成任务前先获取到业务id
        log.info("业务id...{}", processInstanceQuery.singleResult().getBusinessKey());
//        String[] info = this.queryBusinessInfo(processInstanceQuery.singleResult().getBusinessKey());
//        log.info("info = []",info.toString());
//        Integer businessId = Integer.valueOf(info[1]);
//
//        taskService.complete(taskId, variables);
    }


//    private String[] queryBusinessInfo(String businessKey) {
//        String[] info = businessKey.split(ActivitiService.SEPARATOR);
//        ProcessTypeEnum e = ProcessTypeEnum.TYPE_MAP.get(info[0]);
//        info[0] = e.getEntity();
//        return info;
//    }


    @Test
    public void getBpmnModel() {

        InputStream resouceStream = this.getClass().getClassLoader().getResourceAsStream("bpmn/secend_approve.bpmn20.xml");
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in;
        XMLStreamReader xtr;
        try {
            in = new InputStreamReader(resouceStream, "UTF-8");
            xtr = xif.createXMLStreamReader(in);
            BpmnModel model = new BpmnXMLConverter().convertToBpmnModel(xtr);
            Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
            for (FlowElement e : flowElements) {
                System.out.println("flowelement id:" + e.getId() + "  name:" + e.getName() + "   class:" + e.getClass().toString());
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }
    }

    //根据流程实例id挂起任务
    @Test
    public void suspendProcessInstance() {
        String processInstanceId = "57501";
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    /**
     * 根据一个流程实例的id激活流程实例
     *
     */
    @Test
    public void activateProcessInstance() {
        String processInstanceId = "57501";
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    @Test
    public void queryHistoryTaskOutcome() {
        String taskId = "15029";
        String outcome = activitiService.queryHistoryTaskOutcome(taskId);
        System.out.println(outcome);
    }


}