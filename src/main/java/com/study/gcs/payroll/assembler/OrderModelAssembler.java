package com.study.gcs.payroll.assembler;

import com.study.gcs.payroll.controller.OrderController;
import com.study.gcs.payroll.domain.Order;
import com.study.gcs.payroll.domain.Status;
import lombok.SneakyThrows;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

    @SneakyThrows
    @Override
    public EntityModel<Order> toModel(Order order) {
        EntityModel<Order> orderModel = new EntityModel<>(order,
                linkTo(methodOn(OrderController.class).findById(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).findAll()).withRel("orders")
        );

        if (order.getStatus() == Status.IN_PROGRESS) {
            orderModel.add(
                    linkTo(methodOn(OrderController.class)
                            .cancel(order.getId())).withRel("cancel"));
            orderModel.add(
                    linkTo(methodOn(OrderController.class)
                            .complete(order.getId())).withRel("complete"));
        }
        return orderModel;
    }
}
