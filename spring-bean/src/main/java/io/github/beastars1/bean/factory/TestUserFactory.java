package io.github.beastars1.bean.factory;

import io.github.beastars1.ioc_container.entity.User;
import org.springframework.context.annotation.Bean;

public class TestUserFactory implements UserFactory{
    @Bean
    @Override
    public User create() {
        User user = new User();
        user.setName("Wu");
        user.setAge(33);
        return user;
    }
}
