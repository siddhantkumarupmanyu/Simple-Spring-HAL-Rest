package com.example.resttutorial

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

// on @Component,
// if i understood correctly
// @Component gets auto-detected but not @Bean
// from spring tutorial
// And by applying Spring Framework’s @Component annotation,
// the assembler will be automatically created when the app starts.
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