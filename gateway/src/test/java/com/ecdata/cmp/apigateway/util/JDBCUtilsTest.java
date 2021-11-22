package com.ecdata.cmp.apigateway.util;

import com.ecdata.cmp.apigateway.entity.SysLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JDBCUtilsTest {

    @Autowired
    private JDBCUtils jdbcUtils;

    @Test
    public void insert() throws SQLException {
        SysLog sysLog = new SysLog();
        jdbcUtils.insert(sysLog);
    }
}