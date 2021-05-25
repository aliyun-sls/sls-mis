package com.alibaba.sls.demo.tracelogdemo.entity;

import javax.persistence.*;

@Entity
@Table(name = "CUSTOMER")
public class Customer {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String name;

    public Customer() {
    }

    public Customer(String name) {
        this.name = name;
    }

    public static Customer from(String name) {
        return new Customer(name);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
