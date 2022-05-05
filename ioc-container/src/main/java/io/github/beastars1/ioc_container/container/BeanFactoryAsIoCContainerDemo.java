package io.github.beastars1.ioc_container.container;

import io.github.beastars1.ioc_container.entity.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.stereotype.Component;

import java.util.Map;

public class BeanFactoryAsIoCContainerDemo {
    public static void main(String[] args) {
        // 创建 BeanFactory 容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        String location = "classpath:/META-INF/dependency-lookup-context.xml";
        // 加载配置
        int beanCount = reader.loadBeanDefinitions(location);
        System.out.println("bean 加载的数量:" + beanCount);
        lookupCollectionByType(beanFactory);
    }

    // 根据type实时查找集合
    private static void lookupCollectionByType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory listableBeanFactory) {
            Map<String, User> map = listableBeanFactory.getBeansOfType(User.class);
            System.out.println("根据type实时查找集合:");
            map.forEach((k, v) -> System.out.println(k + " >> " + v));
        }
    }
}
