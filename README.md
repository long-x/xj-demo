# ecdata-cmp-parent

简称：ecdata Cloud management platforms  简称 ecdata-cmp

- 拉取项目代码
```bash
https://*******
cd ecdc-cmp-parent
```
#### 介绍
### 注意事项
    0.编码风格：https://checkstyle.sourceforge.io/
        0.1 alibaba版的规范: ecdata-cmp/doc/checkstyle-alibaba.xml
    1. 启用Nacos服务 配置自己的mysql库 版本必是5.7.+
        1.1 参数url：https://nacos.io/zh-cn/docs/quick-start.html
        1.2 下载并解压 nacos-server-1.1.3.tar.gz
        1.3  地址：cd /Users/honglei/dev/alibaba/Nacos/nacos-server-1.1.3/bin
        1.4  环境启动 
            1.4.1 Mac: sh startup.sh -m standalone
            1.4.2 Win: cmd startup.cmd 或者双击startup.cmd运行文件
        1.5  访问：http://127.0.0.1:8848/nacos/#/login  用户名称和密码nacos/nacos (服务器的nacos在10.10.10.112)
        1.6  关闭 
            1.6.1 Mac: sh shutdown.sh
            1.6.2 Win: cmd shutdown.cmd 或者双击shutdown.cmd运行文件
    2. 启动skywalking
        2.1 cd /Users/honglei/dev/skywalking/apache-skywalking-apm-bin/bin
        2.2 ./startup.sh
        2.3 http://127.0.0.1:8080/

### 项目架构

#### skywalking启动变量设置(可选)：
-javaagent:/Users/honglei/dev/skywalking/apache-skywalking-apm-bin/agent/skywalking-agent.jar
-Dcom.alibaba.nacos.naming.log.level=warn
SW_AGENT_NAME=*****

### 启动步骤

### 数据库强分组模式改为弱分组模式

SET @@GLOBAL .sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION'

###swagger-ui 登录时，使用私钥（Ecdata!@#123）加密后的密码。使用前端登录时用户名密码不变。此加密使用RSA加密算法 
I1uUr72RrSM00nuNLmPJi35B0GEkawF0/W4L2KVZhfNR2kS59k9swOQjPR2MWA8l7QmoH228X6XAD+YYrjPg+zq/vsadCCPKU8JK2BnIEWqrbPq9jCWnvej4JOomXc4SBjiwSqJxLKrim1h/65QT5ZEgCv9m5IR+GxJFjWP+7lc=