package com.ycy.simple_database.core;

import com.ycy.simple_database.annotation.Column;
import com.ycy.simple_database.annotation.ID;
import com.ycy.simple_database.annotation.Table;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecordDefinition <T> implements RowMapper {
    private Class<?> klass;
    private String tableName;
    private List<FieldDefinition> fieldList;
    private FieldDefinition key;
    private boolean done;
    private String columnString;
    private String questionMarks;

    public RecordDefinition() {
        done = false;
    }

    List<Object> getFields(Object object) throws IllegalAccessException {
        List<Object> result = new ArrayList<>();

        for (FieldDefinition fd : fieldList) {
            result.add(fd.getField(object));
        }

        return result;
    }

    String getColumnString() {
        if (columnString == null) {
            synchronized (RecordDefinition.class) {
                if (columnString == null) {
                    questionMarks = "";
                    StringBuffer str = new StringBuffer();
                    boolean first = true;
                    for (FieldDefinition fd : fieldList) {
                        str.append(first ? "" : ",").append(fd.getColumn(tableName));
                        questionMarks += (first ? "" : ",") + "?";
                        first = false;
                    }
                    this.columnString = str.toString();
                }
            }
        }

        return columnString;
    }

    String getQuestionMarks() {
        return questionMarks;
    }

    String getKeyCondition() {
        return " " + key.getColumn(tableName) + "=?";
    }

    String getTable() {
        return tableName;
    }

    void setKlass(Class<?> klass) throws Exception {
        this.klass = klass;
        Table table = klass.getAnnotation(Table.class);
//        table.value()的值就是表名
        this.tableName = table.value();

        fieldList = new ArrayList<>();
//        得到studentModel的所有字段
        Field[] fields = klass.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
//            把带有Column注解的字段更名为数据库表里面的字段，例如peopleId改为people_id
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                fieldName = column.value();
            }
//            对studentModel中的字段进行定义
            FieldDefinition fieldDefinition = new FieldDefinition(field, fieldName);
//            把studentModel的每个字段的定义存入fieldList
            fieldList.add(fieldDefinition);
            if (field.isAnnotationPresent(ID.class)) {
                if (done) {
                    throw new Exception("关键字不唯一");
                }
                done  = true;
                key = fieldDefinition;
            }
        }
        if (!done) {
            throw new Exception("缺关键字");
        }
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Object object = null;
        try {
            object = klass.newInstance();
            for (FieldDefinition fieldDefinition : fieldList) {
                fieldDefinition.setField(resultSet, object);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return object;
    }
}
