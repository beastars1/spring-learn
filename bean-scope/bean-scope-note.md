# Bean 的作用域

## Bean 的作用域

1. singleton

   默认 Spring Bean 作用域，一个 BeanFactory 有且仅有一个实例。

   Singleton Bean 无论依赖查找还是依赖注入，均为同一个对象。

   如果依赖注入集合类型的对象，Singleton Bean 和 Prototype Bean 均会存在一个，而且 Singleton Bean 还是那个 Singleton Bean，Prototype Bean 又是全新的一个。

2. prototype

   Prototype Bean 无论依赖查找还是依赖注入，均为新生成的对象。

   Singleton Bean 和 Prototype Bean 均会执行初始化方法回调，不过 Singleton Bean 会执行销毁方法回调，Prototype Bean 不会。

   Singleton Bean 的初始化方法只会执行一次，Prototype Bean 每次新建都会执行，不过不会执行销毁方法。

   Spring 容器没有办法管理 Prototype Bean 的完整生命周期，也没有办法记录实例的存在。销毁回调方法将不会执行，可以利用 BeanPostProcessor 进行清扫工作。

   ```java
   applicationContext.addBeanFactoryPostProcessor(beanFactory -> {
       beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
           @Override
           public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
               System.out.printf("%s Bean 名称:%s 在初始化后回调...%n", bean.getClass().getName(), beanName);
               return bean;
           }
       });
   });
   ```

   不过这个方法是在初始化之后就进行回调，如果在此进行销毁，后面就不能用了，可以在类 Bean 上实现 DisposableBean 接口，DisposableBean 中的 destroy() 方法会在实现类被销毁时调用。

   ```java
   @Override
   public void destroy() throws Exception {
       this.prototypeUser1.preDestroy();
       this.prototypeUser2.preDestroy();
       // 遍历通过BeanName获取BeanDefinition，判断是否为prototype
       this.users.forEach((name, bean) -> {
           BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
           if (beanDefinition.isPrototype()) {
               bean.preDestroy();
           }
       });
       System.out.println("当前 BeanScopeDemo Bean 销毁完成");
   }
   ```

   BeanScopeDemo

3. request

4. session

5. application

## 自定义 Bean 作用域

1. 实现 org.springframework.beans.factory.config.Scope

   `ThreadLocalScope.class`

2. 将实现类注册到容器
   
   ```java
   applicationContext.addBeanFactoryPostProcessor(beanFactory -> {
       // 注册自定义 scope
       beanFactory.registerScope(ThreadLocalScope.SCOPE_NAME, new ThreadLocalScope());
   });
   ```

3. 在 Bean 上添加 @Scope 注解，指定自定义 Scope 的名字（就是注册到容器时指定的名字）
   
   ```java
    @Bean
    @Scope(ThreadLocalScope.SCOPE_NAME) // 线程级别的scope
    public User user() {
        return new User();
    }
   ```

