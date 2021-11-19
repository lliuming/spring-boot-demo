package com.example.jpa.repository;

import com.example.jpa.pojo.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 存储库接口
 * CustomerRepository扩展CrudRepository接口。使用的实体类型和 IDCustomer和Long，在 上的通用参数中指定CrudRepository。
 * 通过扩展CrudRepository,CustomerRepository继承了几种处理Customer持久性的方法，包括保存、删除和查找Customer实体的方法
 * Spring Data JPA 还允许您通过声明方法签名来定义其他查询方法。例如，CustomerRepository包括findByLastName()方法。
 * 在典型的 Java 应用程序中，您可能希望编写一个实现CustomerRepository.
 * 然而，这正是 Spring Data JPA 如此强大的原因：您无需编写存储库接口的实现。Spring Data JPA 在您运行应用程序时创建一个实现。
 *
 */
@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);

}
