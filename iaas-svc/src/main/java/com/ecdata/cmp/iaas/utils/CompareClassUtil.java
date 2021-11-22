package com.ecdata.cmp.iaas.utils;



import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @author ZhaoYX
 * @since 2019/11/26 15:39,
 */
@Slf4j
public class CompareClassUtil {
    /**
     * 比较相同类的相同属性值是否相同
     */
    public static Boolean compareTwoClass(Object class1, Object class2, String...ignoreFieldList)
            throws ClassNotFoundException, IllegalAccessException {


        //动态的获取指定对象的class
        Class<?> clazz1 = class1.getClass();
        Class<?> clazz2 = class2.getClass();

        // 获取类中所有的属性(public、protected、default、private)，但不包括继承的属性，返回 Field 对象的一个数组
        Field[] field1 = clazz1.getDeclaredFields();
        Field[] field2 = clazz2.getDeclaredFields();

        //遍历属性列表field1
        for (int i = 0; i < field1.length; i++) {
            //遍历属性列表field2
            for (int j = 0; j < field2.length; j++) {
                //如果field1[i]属性名与field2[j]属性名内容相同

                if (field1[i].getName().equals(field2[j].getName())) {
                    Boolean checkIgnoreField = true;
                    if (ignoreFieldList!=null&&ignoreFieldList.length>0) {
                        for (int ignoreFieldNum = 0; ignoreFieldNum < ignoreFieldList.length; ignoreFieldNum++) {
                            //如果要忽略的类型里存在，就跳过这个方法
                            if (field1[i].getName().equals(ignoreFieldList[ignoreFieldNum])) {
                                checkIgnoreField = false;
                            }
                        }
                        if (checkIgnoreField) {
                            //调过本次循环的下面语句执行
                            continue;

                        }

                        if (!compareTwo(field1[i], field2[j], class1, class2)) {
                            log.info("CompareClassUtil "+class1.getClass().getSimpleName()+
                                    " "+class1.getClass().getSimpleName()+":"+
                                    field1[i].getName()+" "+field1[j].getName()+false);
                            return false;
                        }
                        break;
                    } else {
                        if (!compareTwo(field1[i], field2[j], class1, class2)) {
                            log.info("CompareClassUtil 无ignoreFieldList"+
                                    class1.getClass().getSimpleName()+
                                    " "+class1.getClass().getSimpleName()+":"+
                                    field1[i]+" "+field1[j]+false);
                            return false;
                        }
                        break;

                    }
                }


            }
        }
        return true;

    }

    /**
     * 对比两个数据是否内容相同
     *
     * @param
     * @return boolean类型
     */
    public static boolean compareTwo(Field field1, Field field2, Object class1, Object class2)
            throws IllegalAccessException {

        //让我们可以访问私有变量的值
        field1.setAccessible(true);
        field2.setAccessible(true);
        //如果field1[i]属性值与field2[j]属性值内容不相同
        //为了不重写equals方法目前只能比较基础的类型
        if ("java".equals(field1.getGenericType().toString())) {

        }
        //返回该类下面对应的该属性值，并返回结果
        Object object1 = field1.get(class1);
        Object object2 = field2.get(class2);
        if (object1 == null && object2 == null) {
            return true;
        }
        if (object1 == null && object2 != null) {
            return false;
        }
        if (object1.equals(object2)) {
            return true;
        }
        return false;
    }



//https://blog.csdn.net/weixin_42369687/article/details/102775511
//public static void main(String[] args) {
//
//}
            //类中的bean属性class类不一样了，就会变成不同
    public static boolean compare(Object obj1,Object obj2,Class clz,String...ignoredFields) throws NoSuchMethodException,
            SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if(obj1.getClass()!=obj2.getClass())
            return false;
//        StringBuilder sb = new StringBuilder();
        Field[] fields = clz.getDeclaredFields();
        ignored:for(Field field:fields){
            field.setAccessible(true);
            //获取属性

            String name = field.getName();
            for(int i=0;i<ignoredFields.length;i++){
                if(name.equalsIgnoreCase(ignoredFields[i]))
                    continue ignored;
            }
//            log.info("CompareClassUtil "+obj1.getClass().getSimpleName()+"==>"+field.getName());
            //获取属性值
            Object v1 = field.get(obj1);
            Object v2 = field.get(obj2);
//            System.out.println("v1 v2 :" +v1+"  "+v2);
            if(v1!=null&&v2!=null) {
                if (!v1.equals(v2)) {
                    log.info("有值 v1 v2 :"+ v1.equals(v2)+ " " + v1 + "  " + v2  );
                    return false;
                }
            }
            else if((v1==null&&v2!=null)||(v1!=null&&v2==null)){
//                log.info("else null CompareClassUtil============compare "+field.getName());
                log.info("v1 v2 :" + v1 + "  " + v2 );
                return false;
            }

            //调用getter
//            String fieldName = field.getName();
//            Method me = clz.getDeclaredMethod("get"+fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
//            String value1 = String.valueOf(me.invoke(obj1))  ;
//            String value2 = String.valueOf(me.invoke(obj2))  ;
//            if(!value1.equals(value2)){
//                return false;
//            }

        }
        return true;
    }
////blog.csdn.net/notears0828/article/details/86515382

}
