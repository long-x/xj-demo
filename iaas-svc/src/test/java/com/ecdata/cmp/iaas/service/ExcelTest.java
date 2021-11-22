package com.ecdata.cmp.iaas.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.ecdata.cmp.iaas.entity.dto.vm.VMGroupVO;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/5/25 9:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExcelTest {

    @Autowired
    private IaasVirtualMachineService service;


    @Test
    public void demo02() throws Exception{
        List<VMGroupVO> vmGroupVOS = service.VmStatisticalList();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), VMGroupVO.class, vmGroupVOS);

        FileOutputStream fos = new FileOutputStream("D:/excel/1111.xls");
        workbook.write(fos);
        fos.close();
    }



    @Test
    public void demo01() throws Exception {
        List<CourseEntity> list = new ArrayList<>();
        CourseEntity course1 = new CourseEntity();
        course1.setId("1");
        course1.setName("海贼王必修1（1）");

//        TeacherEntity teacherEntity = new TeacherEntity();
//        teacherEntity.setId("1");
//        teacherEntity.setName("老王0");
//        course1.setMathTeacher(teacherEntity);

        List<StudentEntity> students = new ArrayList<>();
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId("1");
        studentEntity.setName("学生1");
        studentEntity.setSex(1);
        studentEntity.setBirthday(new Date());
        studentEntity.setRegistrationDate(new Date());
        students.add(studentEntity);

        StudentEntity studentEntity1 = new StudentEntity();
        studentEntity1.setId("2");
        studentEntity1.setName("学生2");
        studentEntity1.setSex(2);
        studentEntity1.setBirthday(new Date());
        studentEntity1.setRegistrationDate(new Date());
        students.add(studentEntity1);
        course1.setStudents(students);

        list.add(course1);

        File savefile = new File("D:/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), CourseEntity.class, list);

        FileOutputStream fos = new FileOutputStream("D:/excel/exportCompanyImg.xls");
        workbook.write(fos);
        fos.close();
    }
}
