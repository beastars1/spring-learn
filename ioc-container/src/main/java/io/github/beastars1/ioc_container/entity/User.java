package io.github.beastars1.ioc_container.entity;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class User implements BeanNameAware {
    private String name;
    private int age;
    private transient String beanName;

    @Bean
    public static User createUser() {
        User user = new User();
        user.setName("Tony");
        user.setAge(20);
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBeanName() {
        return beanName;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", beanName='" + beanName + '\'' +
                '}';
    }

    @PostConstruct
    public void postInit() {
        System.out.println("["+ beanName + "] init...");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("["+ beanName + "] destroy...");
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
