package com.inmaytide.exception.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author luomiao
 * @since 2020/11/09
 */
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContextHolder INSTANCE;

    private ApplicationContext applicationContext;

    public ApplicationContextHolder() {
        // 限制创建ApplicationContextHolder实例，保证单例
        if (INSTANCE != null) {
            throw new IllegalStateException();
        }
        INSTANCE = this;
    }

    public static ApplicationContextHolder getInstance() {
        if (INSTANCE == null) {
            throw new NullPointerException();
        }
        return INSTANCE;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public <T> T getBean(Class<? extends T> cls) {
        return getApplicationContext().getBean(cls);
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
