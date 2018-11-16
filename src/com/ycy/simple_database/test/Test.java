package com.ycy.simple_database.test;

import com.ycy.simple_database.core.Record;
import com.ycy.simple_database.core.RecordBeanFactory;
import com.ycy.simple_database.model.StudentModel;

import java.util.List;

public class Test {
    public static void main(String[] args) {
//        扫描出带Table注解的类
        RecordBeanFactory.scanRecord("com.ycy.simple_database");

        StudentModel student = (StudentModel)
                new Record().get(StudentModel.class, "04163142");
        System.out.println(student);

        List studentList = new Record().list(StudentModel.class);
        System.out.println(studentList);

//        StudentModel stu = new StudentModel();
//        stu.setId("04163144");
//        stu.setName("遛鸟无");
//        stu.setPeopleId("610126199707022817");
//        stu.setPassword("fsdfsdr");
//        stu.setStatus("0");
//
//        new Record().save(stu);
    }
}
