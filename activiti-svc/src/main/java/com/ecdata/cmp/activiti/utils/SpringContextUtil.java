package com.ecdata.cmp.activiti.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author honglei
 * @since 2019/6/11
 * 参照一些案例在此对 在此对网上分享者说声感谢 by：zxm
 * 通过封装applicationContext上线文
 * 获取 spring bean对象 bean启动时候 已经被打印出，可直接根据name、class、name class获取
 *
 * 很多地方能用得到
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    /**
     * 应用上下文
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtil.applicationContext == null) {
            SpringContextUtil.applicationContext = applicationContext;
        }
    }

    /**
     *  获取上下文
     * @return 上下文
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /***
     * 根据name获取bean
     * @param name  名
     * @param <T>   泛型
     * @return  bean
     */
    public static <T> T getBean(String name) {
        return (T) getApplicationContext().getBean(name);
    }

    /**
     * 根据类获取bean
     * @param clazz 类
     * @param <T>   泛型
     * @return  bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 根据名和类获取bean
     * @param name  名
     * @param clazz 类
     * @param <T>   泛型
     * @return  bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
