package com.ecdata.cmp.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;

/**
 * @author xuxinsheng
 * @since 2019-10-23
 */
@Slf4j
public class ReflectUtil {

    /**
     * 反射调用方法
     *
     * @param object     对象
     * @param methodName 方法名
     * @param args       方法参数
     * @return 反复返回值
     * @throws ReflectiveOperationException 反射操作异常
     */
    public static Object invokeMethod(Object object, String methodName, Object... args) throws ReflectiveOperationException {
        int length = args == null ? 0 : args.length;
        Class[] parameterTypes = new Class[length];
        for (int i = 0; i < length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        Method method = object.getClass().getMethod(methodName, parameterTypes);
        return method.invoke(object, args);
    }

    /**
     * 通过字段名获取字段值(反射调用其get方法)
     *
     * @param object    对象
     * @param fieldName 字段名
     * @return 字段值
     * @throws ReflectiveOperationException 反射操作异常
     */
    public static Object getFieldValueByName(Object object, String fieldName) throws ReflectiveOperationException {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        Method method = object.getClass().getMethod(getter);
        return method.invoke(object);
    }

    /**
     * 通过字段名获取字段值
     *
     * @param object    对象
     * @param fieldName 字段名
     * @return 字段值
     * @throws ReflectiveOperationException 反射操作异常
     */
    public Object getFieldValueByFieldName(Object object, String fieldName) throws ReflectiveOperationException {
        Field field = object.getClass().getDeclaredField(fieldName);
        //设置对象的访问权限，保证对private的属性的访问
        field.setAccessible(true);
        return field.get(object);
    }

    /**
     * 通过参数名设置参数值
     *
     * @param object    对象
     * @param fieldName 参数名
     * @param value     参数值
     * @throws ReflectiveOperationException 反射操作异常
     */
    public static void setFieldValueByName(Object object, String fieldName, Object value) throws ReflectiveOperationException {
        // 获取obj类的字节文件对象
        Class c = object.getClass();
        // 获取该类的成员变量
        Field f = c.getDeclaredField(fieldName);
        // 取消语言访问检查
        f.setAccessible(true);
        // 给变量赋值
        f.set(object, value);
    }

    /**
     * 通过参数名设置参数值
     *
     * @param object    对象
     * @param fieldName 参数名
     * @param value     参数值字符串
     * @throws ReflectiveOperationException 反射操作异常
     * @throws ParseException               日期字符串解析异常
     */
    public static void setStrToFieldByName(Object object, String fieldName, String value) throws ReflectiveOperationException, ParseException {
        // 获取obj类的字节文件对象
        Class c = object.getClass();
        // 获取该类的成员变量
        Field f = c.getDeclaredField(fieldName);
        // 取消语言访问检查
        f.setAccessible(true);
        // 获取变量类型
        String type = f.getType().toString();
        // 根据类型给变量赋值
        switch (type) {
            case "class java.lang.Integer":
                f.set(object, Integer.valueOf(value));
                break;
            case "class java.lang.Long":
                f.set(object, Long.valueOf(value));
                break;
            case "class java.lang.Float":
                f.set(object, Float.valueOf(value));
                break;
            case "class java.lang.Double":
                f.set(object, Double.valueOf(value));
                break;
            case "class java.lang.Short":
                f.set(object, Short.valueOf(value));
                break;
            case "class java.lang.Boolean":
                f.set(object, Boolean.valueOf(value));
                break;
            case "class java.lang.Byte":
                f.set(object, Byte.valueOf(value));
                break;
            case "class java.lang.Character":
                f.set(object, value.toCharArray()[0]);
                break;
            case "class java.util.Date":
                f.set(object, DateUtil.parseStr(value));
                break;
            case "class java.lang.String":
                f.set(object, value);
                break;
            case "class java.lang.Object":
                f.set(object, value);
                break;
            default:
                log.info("未定义类型:{}", type);
        }
    }

    /**
     * 通过参数名生成字段实例
     *
     * @param object    对象
     * @param fieldName 参数名
     * @return 字段实例
     * @throws NoSuchFieldException   无字段异常
     * @throws IllegalAccessException 非法访问异常
     * @throws InstantiationException 实例化异常
     */
    public static Object newFieldInstance(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        Field field = object.getClass().getDeclaredField(fieldName);
        return field.getType().newInstance();
    }
}
