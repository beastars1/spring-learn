package io.github.beastars1.bean.scope;

import io.github.beastars1.ioc_container.entity.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.util.Map;

public class BeanScopeDemo implements DisposableBean {
    @Autowired
    @Qualifier("singletonUser")
    private User singletonUser1;
    @Autowired
    @Qualifier("singletonUser")
    private User singletonUser2;
    
    @Autowired
    @Qualifier("prototypeUser")
    private User prototypeUser1;
    @Autowired
    @Qualifier("prototypeUser")
    private User prototypeUser2;

    // Singleton Bean 和 Prototype Bean 均会存在一个，而且 Singleton Bean 还是那个 Singleton Bean，Prototype Bean 又是全新的一个
    @Autowired
    private Map<String, User> users; // k,v -> beanName,Bean

    @Autowired
    private ConfigurableListableBeanFactory beanFactory; // Resolvable Dependency

    @Bean
    public User singletonUser() {
        User user = new User();
        user.setName(String.valueOf(System.nanoTime()));
        return user;
    }

    @Bean
    @Scope("prototype")
    public User prototypeUser() {
        User user = new User();
        user.setName(String.valueOf(System.nanoTime()));
        return user;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 导入DependencySourceDemo类
        applicationContext.register(BeanScopeDemo.class);

        // Spring 容器没有办法管理 Prototype Bean 的完整生命周期，也没有办法记录实例的存在
        // 销毁回调方法将不会执行，可以利用 BeanPostProcessor 进行清扫工作
        applicationContext.addBeanFactoryPostProcessor(beanFactory -> {
            beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
                @Override
                public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                    System.out.printf("%s Bean 名称:%s 在初始化后回调...\n", bean.getClass().getName(), beanName);
                    return bean;
                }
            });
        });

        applicationContext.refresh();

        scopedBeansByLookup(applicationContext);
        scopedBeansByInjection(applicationContext);

        applicationContext.close();
    }

    private static void scopedBeansByLookup(AnnotationConfigApplicationContext applicationContext) {
        for (int i = 0; i < 3; i++) {
            User singletonUser = applicationContext.getBean("singletonUser", User.class);
            System.out.println(singletonUser);
        }

        for (int i = 0; i < 3; i++) {
            User prototypeUser = applicationContext.getBean("prototypeUser", User.class);
            System.out.println(prototypeUser);
        }
    }

    private static void scopedBeansByInjection(AnnotationConfigApplicationContext applicationContext) {
        BeanScopeDemo demo = applicationContext.getBean(BeanScopeDemo.class);
        System.out.println("demo.singletonUser1: " + demo.singletonUser1);
        System.out.println("demo.singletonUser2: " + demo.singletonUser2);
        System.out.println("demo.prototypeUser1: " + demo.prototypeUser1);
        System.out.println("demo.prototypeUser2: " + demo.prototypeUser2);
        System.out.println("demo.users: " + demo.users);
    }

    // 实现了DisposableBean接口的Bean，会在Bean销毁时调用其中的destroy()方法，可以在这个方法里销毁prototype Bean
    @Override
    public void destroy() throws Exception {
        this.prototypeUser1.preDestroy();
        this.prototypeUser2.preDestroy();

        // 遍历通过BeanName获取BeanDefinition，判断是否为prototype
        this.users.forEach((name, bean) -> {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
            if (beanDefinition.isPrototype()) {
                bean.preDestroy();
            }
        });
        System.out.println("当前 BeanScopeDemo Bean 销毁完成");
    }
}
