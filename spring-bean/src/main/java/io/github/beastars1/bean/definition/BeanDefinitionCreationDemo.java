package io.github.beastars1.bean.definition;

import io.github.beastars1.ioc_container.entity.User;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;

// 创建BeanDefinition
public class BeanDefinitionCreationDemo {
    public static void main(String[] args) {
        // 1.通过BeanDefinitionBuilder创建
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
        // 设置bean属性
        beanDefinitionBuilder.addPropertyValue("name", "John")
                .addPropertyValue("age", 30);
        // 获取BeanDefinition实例
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        // beanDefinition并不是bean的最终状态，还可以进行自定义修改
        beanDefinition.setScope("singleton");
        beanDefinition.setAutowireMode(2); // byType

        // 2.通过AbstractBeanDefinition以及派生类生成
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        // 设置bean类型
        genericBeanDefinition.setBeanClass(User.class);
        // 设置bean属性
        MutablePropertyValues propertyValues = genericBeanDefinition.getPropertyValues();
        propertyValues.add("name", "John")
                .add("age", 40);
        genericBeanDefinition.setPropertyValues(propertyValues);
    }
}
