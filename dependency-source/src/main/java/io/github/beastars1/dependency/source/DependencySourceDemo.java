package io.github.beastars1.dependency.source;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;

public class DependencySourceDemo {
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * org.springframework.context.support.AbstractApplicationContext#prepareBeanFactory(ConfigurableListableBeanFactory):
     * beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
     * beanFactory.registerResolvableDependency(ResourceLoader.class, this);
     * beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
     * beanFactory.registerResolvableDependency(ApplicationContext.class, this);
     * 以上除了 BeanFactory 是注册了 beanFactory 对象，其他三个注册的都是同一个 AbstractApplicationContext 对象
     * 以上注入在 postProcessProperties 方法执行，早于 setter注入，也早于 @PostConstruct
     * 也就是说以上类型不能通过依赖查找找到，但是可以正常进行依赖注入
     */
    @PostConstruct
    public void init() {
        System.out.println("beanFactory == applicationContext:" + (beanFactory == applicationContext));
        System.out.println("beanFactory == applicationContext:" + (beanFactory == applicationContext.getAutowireCapableBeanFactory()));
        System.out.println("resourceLoader == applicationContext:" + (resourceLoader == applicationContext));
        System.out.println("applicationEventPublisher == applicationContext:" + (applicationEventPublisher == applicationContext));
    }

    @PostConstruct
    public void initByLookup() {
        // 以下类型不能通过依赖查找找到，但是可以正常进行依赖注入
        getBean(BeanFactory.class);
        getBean(ApplicationContext.class);
        getBean(ResourceLoader.class);
        getBean(ApplicationEventPublisher.class);
    }

    private <T> T getBean(Class<T> type) {
        try {
            return beanFactory.getBean(type);
        } catch (NoSuchBeanDefinitionException e) {
            System.err.println("当前类型 " + type + " 无法在 BeanFactory 中查找");
        }
        return null;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 导入DependencySourceDemo类
        applicationContext.register(DependencySourceDemo.class);
        applicationContext.refresh();

        applicationContext.close();
    }
}
