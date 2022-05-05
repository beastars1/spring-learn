package io.github.beastars1.ioc_container.repository;

import io.github.beastars1.ioc_container.entity.User;

import java.util.Collection;

public class UserRepository {
    private Collection<User> collection;

    public Collection<User> getCollection() {
        return collection;
    }

    public void setCollection(Collection<User> collection) {
        this.collection = collection;
    }
}
