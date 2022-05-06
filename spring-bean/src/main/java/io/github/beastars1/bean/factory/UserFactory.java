package io.github.beastars1.bean.factory;

import io.github.beastars1.ioc_container.entity.User;
import org.springframework.context.annotation.Bean;

public interface UserFactory {
    @Bean
    default User create() {
        User user = new User();
        user.setName("Jack");
        user.setAge(50);
        return user;
    }
}
