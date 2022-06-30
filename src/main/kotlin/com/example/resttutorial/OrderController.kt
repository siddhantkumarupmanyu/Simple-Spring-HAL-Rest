package com.example.resttutorial

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

@RestController
class OrderController @Autowired constructor(
    private val orderRepository: OrderRepository,
    private val assembler: OrderModelAssembler
) {

    @GetMapping("/orders")
    fun all(): CollectionModel<EntityModel<Order>> {
        val orders = orderRepository.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList())

        return CollectionModel.of(
            orders,
            linkTo(methodOn(OrderController::class.java).all()).withSelfRel()
        )
    }

    @GetMapping("/orders/{id}")
    fun one(@PathVariable id: Long): EntityModel<Order> {
        val order = orderRepository.findById(id)
            .orElseThrow { OrderNotFoundException(id) }

        return assembler.toModel(order)
    }

    @PostMapping("/orders")
    fun newOrder(@RequestBody order: Order): ResponseEntity<EntityModel<Order>> {
        order.status = Status.IN_PROGRESS
        val newOrder = orderRepository.save(order)
        return ResponseEntity
            .created(linkTo(methodOn(OrderController::class.java).one(newOrder.id)).toUri())
            .body(assembler.toModel(order))
    }

    @DeleteMapping("/orders/{id}/cancel")
    fun cancel(@PathVariable id: Long): ResponseEntity<*> {
        val order = orderRepository.findById(id)
            .orElseThrow {
                OrderNotFoundException(id)
            }

        if (order.status == Status.IN_PROGRESS) {
            order.status = Status.CANCELLED
            return ResponseEntity.ok(
                assembler.toModel(orderRepository.save(order))
            )
        }

        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
            .body(
                Problem.create()
                    .withTitle("Method not Allowed")
                    .withDetail("You can't cancel an order that is in the ${order.status} status")
            )
    }

    @PutMapping("/orders/{id}/complete")
    fun complete(@PathVariable id: Long): ResponseEntity<*> {
        val order = orderRepository.findById(id)
            .orElseThrow { OrderNotFoundException(id) }

        if (order.status == Status.IN_PROGRESS) {
            order.status = Status.COMPLETED
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)))
        }

        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
            .body(
                Problem.create()
                    .withTitle("Method not allowed")
                    .withDetail("You can't complete an order that is in the ${order.status} status")
            )
    }

}
