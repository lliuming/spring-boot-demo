package com.example.jpamysql.repository;

import com.example.jpamysql.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * 创建存储库
 * Spring 自动在一个具有相同名称的 bean 中实现了这个存储库接口（在这种情况下有一个变化——它被称为userRepository）。
 */
public interface UserRepository extends CrudRepository<User, Integer> {
}
