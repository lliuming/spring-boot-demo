package com.example.jpa.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Spring Data JPA 专注于使用 JPA 在关系数据库中存储数据
 * @Entity，表明它是一个JPA实体
 * @Id使JPA将其识别为对象的ID。该id属性还带有注释，@GeneratedValue以指示应自动生成 ID
 */
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;

    /**
     * 默认构造函数只是为了 JPA 而存在,不直接使用
     */
    protected Customer() {}

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s', lastName='%s']",
                getId(), getFirstName(), getLastName());
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
