package com.study.gcs.payroll.service;

import com.study.gcs.payroll.domain.Order;
import com.study.gcs.payroll.exception.OrderNotFoundException;
import com.study.gcs.payroll.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    public List<Order> findAll() {
        return repository.findAll();
    }

    public Order save(Order order) {
        return repository.save(order);
    }

    public Order findById(Long id) throws OrderNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

}
