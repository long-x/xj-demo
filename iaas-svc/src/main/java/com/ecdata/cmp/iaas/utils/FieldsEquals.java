package com.ecdata.cmp.iaas.utils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhaoYX
 * @since 2019/11/26 15:24,
 */
public class FieldsEquals {

    public static boolean compareObject(Object obj1, Object obj2) throws Exception{
        Map<String, String> result = new HashMap<String, String>();
        if (obj1.getClass() == obj2.getClass()) {
            Field[] fs = obj1.getClass().getDeclaredFields();// 获取所有属性
            for (Field field : fs) {
                // 设置访问性，反射类的方法，设置为true就可以访问private修饰的东西，否则无法访问
                field.setAccessible(true);
                Object v1 = field.get(obj1);
                Object v2 = field.get(obj2);
                result.put(field.getName(), String.valueOf(equals(v1, v2)));
            }
        }
        Collection<String> collection = result.values();
        for (String str : collection) {
            if ("false".equals(str)) {
                return false;
            }
        }
        return true;
    }

    private static boolean equals(Object obj1, Object obj2) {
        if (obj1 == obj2) {
            return true;
        }
        return obj1.equals(obj2);
    }

//https://blog.csdn.net/steven_sisi/article/details/88191386

}
