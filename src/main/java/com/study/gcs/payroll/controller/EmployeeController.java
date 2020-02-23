package com.study.gcs.payroll.controller;

import com.study.gcs.payroll.assembler.EmployeeModelAssembler;
import com.study.gcs.payroll.domain.Employee;
import com.study.gcs.payroll.exception.EmployeeNotFoundException;
import com.study.gcs.payroll.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @Autowired
    private EmployeeModelAssembler assembler;

    @GetMapping("/employees/{id}")
    public EntityModel<Employee> findById(@PathVariable Long id) throws EmployeeNotFoundException {
        Employee employee = service.findById(id);
        return assembler.toModel(employee);
    }

    @PostMapping("/employees")
    public EntityModel<Employee> save(@RequestBody Employee employee) {
        return assembler.toModel(service.save(employee));
    }

    @DeleteMapping("/employees/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteEmployee(id);
    }

    @PutMapping("/employees/{id}")
    public EntityModel<Employee> replaceEmployee(@RequestBody Employee employee, @PathVariable Long id) {
        return assembler.toModel(service.replaceEmployee(employee, id));
    }

    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> findAll() {
        List<EntityModel<Employee>> employees = service.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return new CollectionModel<>(employees,
                linkTo(methodOn(EmployeeController.class).findAll()).withSelfRel());
    }

}
