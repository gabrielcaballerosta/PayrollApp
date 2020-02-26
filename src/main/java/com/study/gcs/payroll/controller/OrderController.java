package com.study.gcs.payroll.controller;

import com.study.gcs.payroll.assembler.OrderModelAssembler;
import com.study.gcs.payroll.domain.Order;
import com.study.gcs.payroll.domain.Status;
import com.study.gcs.payroll.exception.OrderNotFoundException;
import com.study.gcs.payroll.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderController {

    @Autowired
    private OrderService service;

    @Autowired
    private OrderModelAssembler assembler;

    @GetMapping("/orders")
    public CollectionModel<EntityModel<Order>> findAll() {
        List<EntityModel<Order>> orders = service.findAll().stream()
                .map(assembler::toModel)
                .collect(toList());

        return new CollectionModel<>(orders,
                linkTo(methodOn(OrderController.class).findAll()).withSelfRel());
    }

    @GetMapping("/orders/{id}")
    public EntityModel<Order> findById(@PathVariable Long id) throws OrderNotFoundException {
        return assembler.toModel(service.findById(id));
    }

    @PostMapping("/orders")
    public ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order order) throws OrderNotFoundException {
        order.setStatus(Status.IN_PROGRESS);
        Order newOrder = service.save(order);

        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).findById(newOrder.getId())).toUri())
                .body(assembler.toModel(newOrder));
    }

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<RepresentationModel> cancel(@PathVariable Long id) throws OrderNotFoundException {
        Order order = service.findById(id);

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.CANCELLED);
            return ResponseEntity.ok(assembler.toModel(service.save(order)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed", "You can't cancel an order that is in the " + order.getStatus() + " status"));
    }

    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<RepresentationModel> complete(@PathVariable Long id) throws OrderNotFoundException {
        Order order = service.findById(id);

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(assembler.toModel(service.save(order)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is in the " + order.getStatus() + " status"));
    }

}
