package com.ecdata.cmp.apigateway.util;

import com.ecdata.cmp.apigateway.entity.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * Description: it's purpose...
 *
 * @author shig 2019-12-10
 * @version 1.0
 */
@Component
@Slf4j
public class JDBCUtils {
    private static String driverClassName;
    private static String url;
    private static String username;
    private static String password;
    private static String tenant;

    @Value("${spring.datasource.driver-class-name}")
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    @Value("${spring.datasource.url}")
    public void setUrl(String url) {
        this.url = url;
    }

    @Value("${spring.datasource.username}")
    public void setUsername(String username) {
        this.username = username;
    }

    @Value("${spring.datasource.password}")
    public void setPassword(String password) {
        this.password = password;
    }

    @Value("${tenant.defaultId}")
    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    //创建数据库的连接
    public static Connection getConnection() {
        try {
            Class.forName(driverClassName);
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("数据库连接异常！", e);
        }
        return null;
    }

    //关闭数据库的连接
    public static void close(ResultSet rs, Statement stmt, Connection con) throws SQLException {
        if (rs != null)
            rs.close();
        if (stmt != null)
            stmt.close();
        if (con != null)
            con.close();
    }

    public static void insert(SysLog sysLog) throws SQLException {
        if (sysLog == null) {
            log.error("插入日志信息为空！");
            return;
        }
        //注册驱动    使用驱动连接数据库
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = JDBCUtils.getConnection();
            if (con == null) {
                log.error("获取数据库连接为空！");
                return;
            }
            String sql = "INSERT INTO sys_log(id,tenant_id,username,method,uri,params,type,ip,remark,create_user,create_time,display_name) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, sysLog.getId());
            stmt.setLong(2, Long.parseLong(tenant));
            stmt.setString(3, sysLog.getUsername());
            stmt.setString(4, sysLog.getMethod());
            stmt.setString(5, sysLog.getUri());
            stmt.setString(6, sysLog.getParams());
            stmt.setLong(7, sysLog.getType());
            stmt.setString(8, sysLog.getIp());
            //remark
            stmt.setString(9, sysLog.getMethod());
            stmt.setLong(10, sysLog.getCreateUser() == null ? 2777994821132290L : sysLog.getCreateUser());
            stmt.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
            stmt.setString(12, sysLog.getDisplayName());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(rs, stmt, con);
        }
    }
}
