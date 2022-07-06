package com.example.resttutorial

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

@Component
class EmployeeModelAssembler : RepresentationModelAssembler<Employee, EntityModel<Employee>> {

    override fun toModel(employee: Employee): EntityModel<Employee> {
        return EntityModel.of(
            employee,
            linkTo(methodOn(EmployeeController::class.java).one(employee.id)).withSelfRel(),
            linkTo(methodOn(EmployeeController::class.java).all()).withRel("employees")
        )
    }

}