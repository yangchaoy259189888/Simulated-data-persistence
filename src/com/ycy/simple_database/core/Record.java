package com.ycy.simple_database.core;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Record {
    private static ComboPooledDataSource dataSource;
    private static JdbcTemplate jdbcTemplate;

    static {
        dataSource = new ComboPooledDataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Record() {
    }

    public int save(Object object) {
        /**
         * @param: [object]
         * @return: int
         * @date: 2018/11/16
         * @Description: 
         */
        Class<?> klass = object.getClass();
        RecordDefinition<?> recordDefinition = RecordBeanFactory.getRecord(klass);

        StringBuffer SQLString = new StringBuffer("INSERT INTO ");
        SQLString.append(recordDefinition.getTable())
                .append('(').append(recordDefinition.getColumnString()).append(')')
                .append(" VALUES (").append(recordDefinition.getQuestionMarks()).append(')');

        return jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(SQLString.toString());
                try {
                    List<Object> values = recordDefinition.getFields(object);
                    for (int i = 1; i <= values.size(); i++) {
                        preparedStatement.setObject(i, values.get(i-1));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                return preparedStatement;
            }
        });
    }

    public <T> List list(Class<?> klass) {
        RecordDefinition<T> recordDefinition
                = (RecordDefinition<T>) RecordBeanFactory.getRecord(klass);
        StringBuffer SQLString = new StringBuffer("SELECT ");
        SQLString.append(recordDefinition.getColumnString())
                .append(" FROM ").append(recordDefinition.getTable());

        return jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(SQLString.toString());
                return preparedStatement;
            }
        }, recordDefinition);
    }

    public <T> Object get(Class<?> klass, Object id) {
        /**
         * @param: [klass, id]
         * @return: java.lang.Object
         * @date: 2018/11/16
         * @Description: 
         */
//        从recordMap中取出StudentModel对应的recordDefinition
        RecordDefinition<T> recordDefinition
                = (RecordDefinition<T>) RecordBeanFactory.getRecord(klass);

        StringBuffer SQLString = new StringBuffer("SELECT ");
//        根据recordDefinition得到数据库字段，表名等构成sql语句
        SQLString.append(recordDefinition.getColumnString())
                .append(" FROM ").append(recordDefinition.getTable())
                .append(" WHERE").append(recordDefinition.getKeyCondition());

//        利用jdbcTemplate进行查询
        return jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(SQLString.toString());
                preparedStatement.setObject(1, id);
                return preparedStatement;
            }
        }, recordDefinition).get(0);
    }
}
