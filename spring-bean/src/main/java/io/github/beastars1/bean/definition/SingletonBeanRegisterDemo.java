package io.github.beastars1.bean.definition;

import io.github.beastars1.bean.factory.DefaultUserFactory;
import io.github.beastars1.bean.factory.UserFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

// 注册外部单例对象 Bean
public class SingletonBeanRegisterDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        // 创建外部对象
        UserFactory userFactory = new DefaultUserFactory();
        // 注册外部对象
        SingletonBeanRegistry beanFactory = applicationContext.getBeanFactory();
        beanFactory.registerSingleton("userFactory", userFactory);
        // 启动上下文
        applicationContext.refresh();

        UserFactory userFactory2 = applicationContext.getBean("userFactory", UserFactory.class);
        System.out.println(userFactory == userFactory2);

        applicationContext.close();
    }
}
