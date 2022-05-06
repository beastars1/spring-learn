# spring-bean

## Alias

给 Bean 起别名，获取 Bean 时可以通过别名获取。
如果 Bean 是单例，那么通过 BeanName 和通过别名获取到的 Bean 对象，是相同的。

1. 在 xml 文件中指定别名
    ```xml
   <!-- 对user启用别名 -->
    <alias name="user" alias="user1"/> 
   ```
2. 在 @Bean 时指定
   ```java
   @Bean(name = {"user", "user1"})
   public User user() {
       return new User();
   }
   ```
   可以通过 user 和 user1 获取 Bean。

## BeanDefinition
BeanDefinition 是 Spring 中定义 Bean 的配置元信息接口，包含：

- Bean 的类名

- Bean 行为配置元素，如作用域、自动绑定的模式，生命周期回调等

- 其他 Bean 引用，又可称作合作者（collaborators）或者依赖（dependencies）

- 配置设置，比如 Bean

### 1. 构建 BeanDefinition

1. 通过 BeanDefinitionBuilder 创建

   ```java
   BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
   // 设置bean属性
   beanDefinitionBuilder.addPropertyValue("name", "John")
       .addPropertyValue("age", 30);
   // 获取BeanDefinition实例
   AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
   // beanDefinition并不是bean的最终状态，还可以进行自定义修改
   beanDefinition.setScope("singleton");
   beanDefinition.setAutowireMode(2); // byType
   ```

2. 通过 AbstractBeanDefinition 以及派生类生成

   ```java
   GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
   // 设置bean类型
   genericBeanDefinition.setBeanClass(User.class);
   // 设置bean属性
   MutablePropertyValues propertyValues = genericBeanDefinition.getPropertyValues();
   propertyValues.add("name", "John")
       .add("age", 40);
   genericBeanDefinition.setPropertyValues(propertyValues);
   ```

BeanDefinition 构建之后还获取不到 Bean，还需要将其注册到对应的 Bean 容器中才行。

### 2. 注册 BeanDefinition

1. 通过 xml 注册

   ```xml
   <bean id="user" class="io.github.beastars1.ioc_container.entity.User">
       <property name="name" value="Jack"/>
       <property name="age" value="12"/>
   </bean>
   ```

2. 通过注解注册

   @Bean：只能用于方法和注解上

   @Component：只能用于类上

   @Import：引入要导入的类

3. 手动进行注册

   1. 自定义 BeanName 注册

      ```java
      // 1.创建BeanDefinition
      BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
      builder.addPropertyValue("name", "Mark")
          .addPropertyValue("age", 20);
      // 2.注册BeanDefinition
      registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
      ```

   2. 自动生成 BeanName 注册

      ```java
      // 1.创建BeanDefinition
      BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
      builder.addPropertyValue("name", "Mark")
          .addPropertyValue("age", 20);
      // 2.注册BeanDefinition，不提供beanName，自动生成一个：全限定类名#编号
      BeanDefinitionReaderUtils.registerWithGeneratedName(builder.getBeanDefinition(), registry);
      ```

   3. 手动注册

      ```java
      AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
      // 导入AnnotationBeanDefinitionDemo类
      applicationContext.register(AnnotationBeanDefinitionDemo.class);
      ```

      