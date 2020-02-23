package com.study.gcs.payroll.controller;

import com.study.gcs.payroll.domain.Employee;
import com.study.gcs.payroll.exception.EmployeeNotFoundException;
import com.study.gcs.payroll.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping("/employees/{id}")
    public Employee findById(@PathVariable Long id) throws EmployeeNotFoundException {
        return service.findById(id);
    }

    @PostMapping("/employees")
    public Employee save(@RequestBody Employee employee) {
        return service.save(employee);
    }

    @DeleteMapping("/employees/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteEmployee(id);
    }

    @PutMapping("/employees/{id}")
    public Employee replaceEmployee(@RequestBody Employee employee, @PathVariable Long id) {
        return service.replaceEmployee(employee, id);
    }

    @GetMapping("/employees")
    public List<Employee> findAll() {
        return service.findAll();
    }

}
