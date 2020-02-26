package com.study.gcs.payroll.initialization;

import com.study.gcs.payroll.domain.Employee;
import com.study.gcs.payroll.domain.Order;
import com.study.gcs.payroll.domain.Status;
import com.study.gcs.payroll.repository.EmployeeRepository;
import com.study.gcs.payroll.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initEmployeeDatabase(EmployeeRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Employee("Bilbo Baggins", "burglar")));
            log.info("Preloading " + repository.save(new Employee("Frodo Baggins", "thief")));
        };
    }

    @Bean
    CommandLineRunner initOrderDatabase(OrderRepository repository) {
        return args -> {
            repository.save(new Order("MacBook Pro", Status.COMPLETED));
            repository.save(new Order("iPhone", Status.IN_PROGRESS));

            repository.findAll().forEach(order -> {
                log.info("Preloaded " + order);
            });
        };
    }

}
