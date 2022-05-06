package io.github.beastars1.bean.factory;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class DefaultUserFactory implements UserFactory, InitializingBean, DisposableBean {
    @PostConstruct // 1
    public void postConstruct() {
        System.out.println("@PostConstruct...");
    }

    @Override
    public void destroy() { // 2
        System.out.println("DisposableBean#destroy()...");
    }

    @PreDestroy // 1
    public void preDestroy() {
        System.out.println("@PreDestroy...");
    }

    public void initFactory() { // 3
        System.out.println("initFactory()...");
    }

    public void destroyFactory() { // 3
        System.out.println("destroyFactory()...");
    }

    @Override
    public void afterPropertiesSet() throws Exception { // 2
        System.out.println("InitializingBean#afterPropertiesSet()...");
    }
}
