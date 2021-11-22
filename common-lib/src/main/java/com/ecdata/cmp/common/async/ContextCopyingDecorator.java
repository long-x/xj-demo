package com.ecdata.cmp.common.async;

/**
 * @author honglei
 * @since 2019-08-19
 */

import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * https://stackoverflow.com/questions/23732089/how-to-enable-request-scope-in-async-task-executor
 * 注意两点：
 * 1.被调用的方不能和调用者在同一个bean中，eg:ServiceHelper的trackEventAsync，
 * 2.线程上下文装配copy类,不然会丢失
 */
public class ContextCopyingDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        RequestAttributes context = RequestContextHolder.currentRequestAttributes();
        return () -> {
            try {
                RequestContextHolder.setRequestAttributes(context);
                runnable.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        };
    }
}
