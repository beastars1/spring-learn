package io.github.beastars1.ioc_container.container;

import io.github.beastars1.ioc_container.entity.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Map;

public class AnnotationApplicationContextAsIoCContainerDemo {
    public static void main(String[] args) throws Exception {
        // 创建 BeanFactory 容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 将当前类 AnnotationApplicationContextAsIoCContainerDemo 作为配置类（Configuration Class）
        applicationContext.register(AnnotationApplicationContextAsIoCContainerDemo.class);
        applicationContext.register(TestFactoryBean.class);
        // 启动应用上下文
        applicationContext.refresh();
        lookupCollectionByType(applicationContext);
        // isSingleton()为false，每次获取的都是不同对象
        Test test = applicationContext.getBean(Test.class);
        System.out.println(test);
        Test test2 = applicationContext.getBean(Test.class);
        System.out.println(test2);
    }

    @Bean
    public User user() {
        User user = new User();
        user.setName("Tony");
        user.setAge(20);
        return user;
    }

    // 根据type实时查找集合
    private static void lookupCollectionByType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory listableBeanFactory) {
            Map<String, User> map = listableBeanFactory.getBeansOfType(User.class);
            System.out.println("根据type实时查找集合:");
            map.forEach((k, v) -> System.out.println(k + " >> " + v));
        }
    }

//    @Component
    static class TestFactoryBean implements FactoryBean<Test> {
        @Override
        public Test getObject() throws Exception {
            Test test = new Test();
            test.setId(222);
            return test;
        }

        @Override
        public Class<?> getObjectType() {
            return Test.class;
        }

        @Override
        public boolean isSingleton() {
            return false;
        }
    }

    static class Test {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
