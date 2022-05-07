# 依赖查找

## 单一类型依赖查找

单一类型依赖查找接口：BeanFactory

getBean() 时会加锁，线程安全。

以下方法 `beanFactory.getBean()` 如果没有对应的 Bean，会抛出异常。

1. 根据 Bean Name 查找

   ```java
   beanFactory.getBean(String beanName);
   ```

2. 根据 Bean Type 查找

   1. 实时查找

      ```java
      beanFactory.getBean(Class<T> type);
      ```

   2. 延迟查找

      ```java
      ObjectProvider<T> objectProvider = beanFactory.getBeanProvider(Class<T> type);
      objectProvider.getObject();
      ```

3. 根据 Bean Name 和 Type 查找

   ```java
   beanFactory.getBean(String name, Class<T> requiredType);
   ```

## 集合类型依赖查找

集合类型依赖查找接口：ListableBeanFactory

以下方法如果没有对应的 Bean，会返回一个空集合，而不会抛出找不到 Bean 的异常 `NoSuchBeanDefinitionException`，但是如果是实时查找仍然会有创建 Bean 时的异常。

1. 根据 Bean Type 查找

   ```java
   // 根据类型获取同类型 Bean 的实例，map 的 key 是 BeanName，value 是实例
   Map<String, T> map = listableBeanFactory.getBeansOfType(Class<T> type);
   // 根据类型获取同类型 Bean 的 BeanName
   String[] names = listableBeanFactory.getBeanNamesForType(Class<T> type);
   ```

   getBeansOfType 会导致 Bean 初始化

   getBeanNamesForType 不会初始化 Bean

2. 根据注解类型查找

   ```java
   // 获取被指定注解注解的 Bean 实例
   Map<String, Object> map = listableBeanFactory.getBeansWithAnnotation(Class<? extends Annotation> annotationType);
   // 获取被指定注解注解的 Bean 的 BeanName
   String[] names = listableBeanFactory.getBeanNamesForAnnotation(Class<? extends Annotation> annotationType);
   // 获取指定 Bean 上的指定注解，如果没有返回 null
   <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType);
   ```

   可以是自定义注解

   getBeansWithAnnotation 会导致 Bean 初始化

   getBeanNamesForAnnotation 不会初始化 Bean

## 层次性依赖查找

集合类型依赖查找接口：HierarchicalBeanFactory

```java
ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
HierarchicalBeanFactory parentBeanFactory = createParentBeanFactory();
beanFactory.setParentBeanFactory(parentBeanFactory);
```

ConfigurableListableBeanFactory 实现了 ListableBeanFactory 和 ConfigurableBeanFactory，而 ConfigurableBeanFactory 又实现了 HierarchicalBeanFactory。

```java
// 一直朝ParentBeanFactory寻找Bean，如果父没有，再寻找本地
private boolean containsBean(HierarchicalBeanFactory beanFactory, String beanName) {
    BeanFactory parentBeanFactory = beanFactory.getParentBeanFactory();
    // 如果有父Factory，就向上寻找
    if (parentBeanFactory instanceof HierarchicalBeanFactory parentHierarchicalBeanFactory) {
        if (containsBean(parentHierarchicalBeanFactory, beanName)) {
            return true;
        }
    }
    return beanFactory.containsLocalBean(beanName);
}
```

HierarchicalDependencyLookupDemo

## 延迟依赖查找

1. ObjectFactory
2. ObjectProvider

ObjectProvider 实现了 ObjectFactory，并添加了一些方法。

- getObject() 只有指定类型的 Bean 只有一个的时候才会正常返回 Bean，如果没有会抛出 `NoSuchBeanDefinitionException`，如果存在多个 Bean 会抛出 `NoUniqueBeanDefinitionException`。

- getIfUnique() 只有指定类型的 Bean 只有一个的时候才会正常返回 Bean，如果没有或者存在多个 Bean 都会返回 null。
- getIfAvailable() 只有指定类型的 Bean 只有一个的时候才会正常返回 Bean，如果没有会返回 null，如果有多个 Bean 会抛出 `NoUniqueBeanDefinitionException`。
- 如果同一类型的多个 Bean 指定了主要 Bean（@Primary），那么 getIfUnique() 和 getIfAvailable() 都会返回 Primary Bean。

 ObjectProvider 的 iterator() 如果找不到 Bean 会返回一个空的集合，也不会有找不到 Bean 的异常。

```java
ObjectProvider<T> objectProvider = applicationContext.getBeanProvider(Class<T> type);
objectProvider.getObject();
```

只有调用 getObject() 时才会初始化 Bean。

如果有多个同类型的 Bean，可以通过 ObjectProvider 的 iterator() 进行迭代。

# Spring 内建可查找的依赖

## AbstractApplicationContext

| BeanName                    | Bean 实例                        | 场景用途                |
| --------------------------- | -------------------------------- | ----------------------- |
| environment                 | Environment 对象                 | 外部化配置以及 Profiles |
| systemProperties            | java.util.Properties 对象        | Java 系统属性           |
| systemEnvironment           | java.util.Map 对象               | 操作系统环境变量        |
| lifecycleProcessor          | LifecycleProcessor 对象          | Lifecycle Bean 处理器   |
| applicationEventMulticaster | ApplicationEventMulticaster 对象 | Spring 事件广播         |

