package com.alibaba.sls.demo.tracelogdemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_ORDER")
public class Order {
    @Id
    private int id;

    @Column(name = "name")
    private String name;

    public Order() {
    }

    public Order(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Order(String name) {
        this.name = name;
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
