import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/13 11:11
 * @modified By：
 */
@Slf4j
@SpringBootTest
public class DatabaseTest {


    /**
     * 执行test
     * 自动创建activiti25张表
     */
    @Test
    public void testCreateTable() {
        // 引擎配置
        ProcessEngineConfiguration pec = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        pec.setJdbcDriver("com.mysql.jdbc.Driver");
        pec.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/cmp_workflow2?allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf-8&useSSL=false&&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true");
        pec.setJdbcUsername("root");
        pec.setJdbcPassword("110308");

        /**
         * false 不能自动创建表
         * create-drop 先删除表再创建表
         * true 自动创建和更新表
         */
        pec.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        // 获取流程引擎对象
        ProcessEngine processEngine = pec.buildProcessEngine();

    }


    @Test
    public void createActiviti(){
        //获取流程引擎配置文件（默认）
        //ProcessEngineConfiguration config = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        ProcessEngineConfiguration config = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti_druid.cfg.xml");

        //创建引擎
        ProcessEngine engine = config.buildProcessEngine();

        log.info("流程名称 {}，版本{}",engine.getName(),engine.VERSION);

        //获取RepositoryService,用于加载流程图，部署流程图
        RepositoryService repositoryService = engine.getRepositoryService();

        //部署流程文件
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/multi_approve.bpmn20.xml").deploy();
        String id = deployment.getId();
        ProcessDefinition processDefinition = repositoryService.
                createProcessDefinitionQuery().
                deploymentId(id).singleResult();
        log.info("流程定义对象 [{}],流程ID [{}]",processDefinition.getName(),processDefinition.getId());

        //获取运行时服务 RuntimeService
        RuntimeService runtimeService = engine.getRuntimeService();

        //获取流程实例 ，打开流程图可以找到key
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("second_approve");

        //获取任务TaskService
        TaskService taskService = engine.getTaskService();
//
//        //查询当前流程实例的task节点
        Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
        log.info("节点名称 [{}]",task.getName());
//
//        //处理task，完成当前节点的任务
        taskService.complete("y");
        log.info("完成当前节点id [{}]",task.getId());
        engine.close();

    }






}
