package com.example.resttutorial

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.server.core.DummyInvocationUtils.methodOn
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors


// If a bean has one constructor, you can omit the @Autowired, as shown in the following example:
@RestController
class EmployeeController(
    private val repository: EmployeeRepository,
) {

    private val assembler: EmployeeModelAssembler = EmployeeModelAssembler()

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/employees")
    fun all(): CollectionModel<EntityModel<Employee>> {
        val employees = repository.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList())
        return CollectionModel.of(
            employees,
            linkTo(methodOn(EmployeeController::class.java).all()).withSelfRel()
        )
    }
    // end::get-aggregate-root[]

    @PostMapping("/employees")
    fun newEmployee(@RequestBody newEmployee: Employee): ResponseEntity<EntityModel<Employee>> {
        val entityModel = assembler.toModel(repository.save(newEmployee))
        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel)
    }

    // Single item

    @GetMapping("/employees/{id}")
    fun one(@PathVariable id: Long): EntityModel<Employee> {
        val employee = repository.findById(id).orElseThrow {
            EmployeeNotFoundException(id)
        }
        return assembler.toModel(employee)
    }

    @PutMapping("/employees/{id}")
    fun replaceEmployee(
        @RequestBody newEmployee: Employee,
        @PathVariable id: Long
    ): ResponseEntity<EntityModel<Employee>> {
        val updatedEmployee = repository.findById(id)
            .map { employee ->
                employee.name = newEmployee.name
                employee.role = newEmployee.role
                repository.save(employee)
            }.orElseGet {
                newEmployee.id = id
                repository.save(newEmployee)
            }

        val entityModel = assembler.toModel(updatedEmployee)

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel)
    }

    @DeleteMapping("/employees/{id}")
    fun deleteEmployee(@PathVariable id: Long): ResponseEntity<EntityModel<Employee>> {
        repository.deleteById(id)
        return ResponseEntity
            .noContent()
            .build()
    }

}