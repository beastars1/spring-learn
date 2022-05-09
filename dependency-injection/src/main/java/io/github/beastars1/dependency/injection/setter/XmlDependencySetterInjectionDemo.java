package io.github.beastars1.dependency.injection.setter;

import io.github.beastars1.dependency.injection.UserHolder;
import io.github.beastars1.ioc_container.entity.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * 基于xml使用setter方式注入依赖
 */
public class XmlDependencySetterInjectionDemo {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:/META-INF/dependency-setter-injection.xml");
        UserHolder userHolder = beanFactory.getBean(UserHolder.class);
        User user = userHolder.getUser();
        System.out.println(user);
    }
}
