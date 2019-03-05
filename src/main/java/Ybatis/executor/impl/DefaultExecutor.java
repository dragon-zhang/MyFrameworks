package Ybatis.executor.impl;


import Ybatis.executor.Executor;
import Ybatis.pojo.Configuration;
import Ybatis.pojo.Jdbc;
import Ybatis.pojo.Method;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SuccessZhang
 */
public class DefaultExecutor implements Executor {

    private Connection connection;

    public DefaultExecutor(Configuration config) {
        try {
            Jdbc jdbc = config.getJdbc();
            Class.forName(jdbc.getDriver());
            this.connection = DriverManager.getConnection(jdbc.getUrl(), jdbc.getUsername(), jdbc.getPassword());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <E> List<E> query(Method method, Object[] parameters) {
        //7.Executor基于jdbc访问数据库获取数据
        List<E> result = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(method.getSql().getOrigin());
            //填充sql中的占位符
            fillPlaceholders(preparedStatement, parameters);
            ResultSet resultSet = preparedStatement.executeQuery();
            //8.通过反射将获取到的数据转换成pojo并且返回给SqlSession
            handleResult(resultSet, result, method.getResultType());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        //9.将数据返回给mapper接口调用方
        return result;
    }

    @Override
    public int update(Method method, Object[] parameters) {
        //7.Executor基于jdbc访问数据库获取数据
        int result = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(method.getSql().getOrigin());
            //填充sql中的占位符
            fillPlaceholders(preparedStatement, parameters);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private <E> void handleResult(ResultSet resultSet, List<E> result, String resultType) throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Class<E> clazz = (Class<E>) Class.forName(resultType);
        while (resultSet.next()) {
            E entity = clazz.newInstance();
            //利用反射将resultSet中的数据填充到entity中
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String fieldName = metaData.getColumnName(i);
                String fieldType = metaData.getColumnClassName(i);
                Field field = clazz.getDeclaredField(higherUnderlineAfterFirstCase(fieldName));
                //允许强制赋值
                field.setAccessible(true);
                if (fieldType.endsWith("Long")) {
                    field.set(entity, resultSet.getLong(fieldName));
                } else if (fieldType.endsWith("String")) {
                    field.set(entity, resultSet.getString(fieldName));
                } else if (fieldType.endsWith("Timestamp")) {
                    field.set(entity, resultSet.getTimestamp(fieldName));
                } else if (fieldType.endsWith("BigDecimal")) {
                    field.set(entity, resultSet.getBigDecimal(fieldName));
                } else if (fieldType.endsWith("Date")) {
                    field.set(entity, resultSet.getDate(fieldName));
                } else if (fieldType.endsWith("Byte")) {
                    field.set(entity, resultSet.getByte(fieldName));
                } else if (fieldType.endsWith("Short")) {
                    field.set(entity, resultSet.getShort(fieldName));
                } else if (fieldType.endsWith("Boolean")) {
                    field.set(entity, resultSet.getBoolean(fieldName));
                } else if (fieldType.endsWith("Time")) {
                    field.set(entity, resultSet.getTime(fieldName));
                } else if (fieldType.endsWith("Bytes")) {
                    field.set(entity, resultSet.getBytes(fieldName));
                } else if (fieldType.endsWith("Array")) {
                    field.set(entity, resultSet.getArray(fieldName));
                } else if (fieldType.endsWith("Float")) {
                    field.set(entity, resultSet.getFloat(fieldName));
                } else if (fieldType.endsWith("Double")) {
                    field.set(entity, resultSet.getDouble(fieldName));
                } else if (fieldType.endsWith("Blob")) {
                    field.set(entity, resultSet.getBlob(fieldName));
                } else if (fieldType.endsWith("Clob")) {
                    field.set(entity, resultSet.getClob(fieldName));
                } else if (fieldType.endsWith("Int")) {
                    field.set(entity, resultSet.getInt(fieldName));
                } else if (fieldType.endsWith("URL")) {
                    field.set(entity, resultSet.getURL(fieldName));
                } else {
                    field.set(entity, resultSet.getObject(fieldName));
                }
            }
            result.add(entity);
        }
    }

    private void fillPlaceholders(PreparedStatement preparedStatement, Object[] parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] instanceof Integer) {
                preparedStatement.setInt(i + 1, (Integer) parameters[i]);
            } else if (parameters[i] instanceof String) {
                preparedStatement.setString(i + 1, (String) parameters[i]);
            } else if (parameters[i] instanceof Boolean) {
                preparedStatement.setBoolean(i + 1, (Boolean) parameters[i]);
            } else if (parameters[i] instanceof Byte) {
                preparedStatement.setByte(i + 1, (Byte) parameters[i]);
            } else if (parameters[i] instanceof Short) {
                preparedStatement.setShort(i + 1, (Short) parameters[i]);
            } else if (parameters[i] instanceof Long) {
                preparedStatement.setLong(i + 1, (Long) parameters[i]);
            } else if (parameters[i] instanceof Float) {
                preparedStatement.setFloat(i + 1, (Float) parameters[i]);
            } else if (parameters[i] instanceof Double) {
                preparedStatement.setDouble(i + 1, (Double) parameters[i]);
            }
        }
    }

    private String higherUnderlineAfterFirstCase(String fieldName) {
        //下划线后首字母大写，原理ASCII码
        char[] chars = fieldName.toCharArray();
        int index = fieldName.indexOf("_");
        if (index == -1) {
            return fieldName;
        } else {
            chars[index + 1] -= 32;
        }
        return higherUnderlineAfterFirstCase(String.valueOf(chars).replaceFirst("_", ""));
    }
}
