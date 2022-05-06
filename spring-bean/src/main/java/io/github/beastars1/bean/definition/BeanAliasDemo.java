package io.github.beastars1.bean.definition;

import io.github.beastars1.ioc_container.entity.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class BeanAliasDemo {
    public static void main(String[] args) {
        // 配置xml，启动上下文
        ClassPathXmlApplicationContext beanFactory = new ClassPathXmlApplicationContext("classpath:/META-INF/bean-definition-context.xml");
        User user = beanFactory.getBean("user", User.class);
        User user2 = beanFactory.getBean("user2", User.class);
        // user bean默认是单例，所以取出来的相等
        System.out.println(user == user2);

        System.out.println("user bean的别名：");
        Arrays.stream(beanFactory.getAliases("user")).forEach(System.out::println);
        System.out.println("beanFactory中所有的的bean name：");
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);

        beanFactory.close();
    }
}
