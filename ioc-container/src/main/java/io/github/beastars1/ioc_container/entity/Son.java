package io.github.beastars1.ioc_container.entity;

import io.github.beastars1.ioc_container.annotation.Primary;

@Primary
public class Son extends User{
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Son{" +
                "address='" + address + '\'' +
                "} " + super.toString();
    }
}
