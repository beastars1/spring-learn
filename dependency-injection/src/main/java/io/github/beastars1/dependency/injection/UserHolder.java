package io.github.beastars1.dependency.injection;

import io.github.beastars1.ioc_container.entity.User;

public class UserHolder {
    private User user;

    public UserHolder() {
    }

    public UserHolder(User user) {
        System.out.println("1");
        this.user = user;
    }

    public UserHolder(User user, User user2) {
        System.out.println("2");
        this.user = user2;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserHolder{" +
                "user=" + user +
                '}';
    }
}
