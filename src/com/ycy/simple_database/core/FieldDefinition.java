package com.ycy.simple_database.core;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FieldDefinition {
    private Field field;
    private String column;

    public FieldDefinition() {
    }

    public FieldDefinition(Field field, String column) {
        this.field = field;
        this.column = column;
    }

    public Object getField(Object object) throws IllegalAccessException {
//        用反射时可以访问私有变量
        field.setAccessible(true);

        return field.get(object);
    }

    public void setField(ResultSet rs, Object object) throws IllegalAccessException, SQLException {
        Object value = rs.getObject(column);
        field.setAccessible(true);
        field.set(object, value);
    }

    public String getColumn(String tableName) {
        return tableName + '.' + column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}
