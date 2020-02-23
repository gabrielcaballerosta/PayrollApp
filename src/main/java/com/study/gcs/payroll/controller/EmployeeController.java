package com.study.gcs.payroll.controller;

import com.study.gcs.payroll.assembler.EmployeeModelAssembler;
import com.study.gcs.payroll.domain.Employee;
import com.study.gcs.payroll.exception.EmployeeNotFoundException;
import com.study.gcs.payroll.service.EmployeeService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> findById(@PathVariable Long id) throws EmployeeNotFoundException {
        EntityModel<Employee> employee = assembler.toModel(service.findById(id));
        return ResponseEntity.ok(employee);
    }

    @PostMapping("/employees")
    public ResponseEntity<?> save(@RequestBody Employee employee) {
        EntityModel<Employee> entityModel = assembler.toModel(service.save(employee));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<?> replaceEmployee(@RequestBody Employee employee, @PathVariable Long id) {
        EntityModel<Employee> entityModel = assembler.toModel(service.replaceEmployee(employee, id));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/employees")
    public ResponseEntity<?> findAll() {
        List<EntityModel<Employee>> employees = service.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employees);
    }

}
