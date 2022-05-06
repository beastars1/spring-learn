package io.github.beastars1.bean.definition;

import io.github.beastars1.ioc_container.entity.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanInstantiationDemo {
    public static void main(String[] args) {
        // 配置xml文件，启动上下文
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/META-INF/bean-instantiation-context.xml");
        User userByStaticMethod = beanFactory.getBean("userByStaticMethod", User.class);
        System.out.println(userByStaticMethod);
        User userByFactoryMethod = beanFactory.getBean("userByFactoryMethod", User.class);
        System.out.println(userByFactoryMethod);
        User userByFactoryBean1 = beanFactory.getBean("userByFactoryBean", User.class);
        System.out.println(userByFactoryBean1);
        User userByFactoryBean2 = beanFactory.getBean("userByFactoryBean", User.class);
        System.out.println(userByFactoryBean2);
        // isSingleTon() = false
        System.out.println(userByFactoryBean1 == userByFactoryBean2);
    }
}
