package io.github.beastars1.bean.definition;

import io.github.beastars1.bean.factory.UserFactory;
import io.github.beastars1.ioc_container.entity.User;
import org.springframework.beans.factory.serviceloader.ServiceLoaderFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Iterator;
import java.util.ServiceLoader;

// spi机制加载bean
public class SPIBeanInstantiationDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(SPIBeanInstantiationDemo.class);
        applicationContext.refresh();
        ServiceLoader<UserFactory> serviceLoader = applicationContext.getBean("serviceLoaderFactoryBean", ServiceLoader.class);
        displayServiceLoader(serviceLoader);

        System.out.println(applicationContext.getBeansOfType(User.class));

        applicationContext.close();
    }

    static void displayServiceLoader(ServiceLoader<UserFactory> serviceLoader) {
        Iterator<UserFactory> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            UserFactory userFactory = iterator.next();
            User user = userFactory.create();
            System.out.println(user);
        }
    }

    static void m() {
        ServiceLoader<UserFactory> serviceLoader = ServiceLoader.load(UserFactory.class, Thread.currentThread().getContextClassLoader());
        displayServiceLoader(serviceLoader);
    }

    @Bean
    public ServiceLoaderFactoryBean serviceLoaderFactoryBean() {
        ServiceLoaderFactoryBean serviceLoaderFactoryBean = new ServiceLoaderFactoryBean();
        serviceLoaderFactoryBean.setServiceType(UserFactory.class);
        return serviceLoaderFactoryBean;
    }
}
