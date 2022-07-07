package com.example.resttutorial.employee

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn

class EmployeeModelAssemblerTest {

    @Test
    fun transformsToEntityModel() {
        val assembler = EmployeeModelAssembler()
        val employee = Employee("Sam", "Don", "gardner")
        employee.id = 100

        val entity = assembler.toModel(employee)
        assertThat(entity.content).isSameAs(employee)

        val selfLink = linkTo(methodOn(EmployeeController::class.java).one(employee.id)).withSelfRel()
        val employeesLink = linkTo(methodOn(EmployeeController::class.java).all()).withRel("employees")

        assertThat(entity.links.contains(selfLink, employeesLink))
            .withFailMessage("contains self and employees link")
            .isTrue()
    }

}