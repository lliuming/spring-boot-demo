package com.example.jpa;

import com.example.jpa.pojo.Customer;
import com.example.jpa.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 使用 JPA 访问数据
 * Spring Data JPA 专注于使用 JPA 在关系数据库中存储数据
 * 它最引人注目的功能是能够在运行时从存储库界面自动创建存储库实现。
 */
@SpringBootApplication
public class JpaApplication {

//    日志记录器
    public static final Logger log = LoggerFactory.getLogger(JpaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class, args);
    }

    /**
     *
     * @param repository 存储库
     * @return CommandLineRunner 在bean初始化后程序启动前执行
     */
    @Bean
    public CommandLineRunner demo(CustomerRepository repository) {
        return (args) -> {
            // save a few customers
            repository.save(new Customer("Jack", "Bauer"));
            repository.save(new Customer("Chloe", "O'Brian"));
            repository.save(new Customer("Kim", "Bauer"));
            repository.save(new Customer("David", "Palmer"));
            repository.save(new Customer("Michelle", "Kessler"));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            for (Customer customer : repository.findAll()) {
                log.info(customer.toString());
            }
            log.info("");

            // fetch an individual customer by ID
            Customer customer = repository.findById(1L).orElse(new Customer("null", "null"));
            log.info("Customer found with findById(1L):");
            log.info("--------------------------------");
            log.info(customer.toString());
            log.info("");

            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            repository.findByLastName("Bauer").forEach(bauer -> log.info(bauer.toString()));
            log.info("");
        };
    }

}
