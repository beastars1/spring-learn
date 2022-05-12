package io.github.beastars1.bean.scope.customize;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.core.NamedThreadLocal;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义线程级别的Scope，同一个线程为单例，多线程为原型
 */
public class ThreadLocalScope implements Scope {
    public static final String SCOPE_NAME = "thread-local";

    private final NamedThreadLocal<Map<String, Object>> threadLocal = new NamedThreadLocal<>("thread-local-scope") {
        // 此处相当于一个匿名类
        {
            // 此处相当于构造方法
        }

        // 重写ThreadLocal的方法
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<>();
        }
    };

    // 获取bean的方法
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Map<String, Object> context = threadLocal.get();
        Object obj = context.get(name);
        if (obj == null) {
            obj = objectFactory.getObject();
            context.put(name, obj);
        }
        return obj;
    }

    @Override
    public Object remove(String name) {
        Map<String, Object> context = threadLocal.get();
        return context.remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        System.out.println(name + " : registerDestructionCallback");
    }

    @Override
    public Object resolveContextualObject(String key) {
        Map<String, Object> context = threadLocal.get();
        return context.get(key);
    }

    @Override
    public String getConversationId() {
        Thread thread = Thread.currentThread();
        return String.valueOf(thread.getId());
    }
}
