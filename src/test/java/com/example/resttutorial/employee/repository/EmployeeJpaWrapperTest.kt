package com.example.resttutorial.employee.repository

import com.example.resttutorial.employee.Employee
import com.example.resttutorial.employee.EmployeeNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(EmployeeJpaWrapper::class)
class EmployeeJpaWrapperTest {

    // same in memory database is shared across all the tests.
    // so, id given when save in each test depends on the execution order of the test
    // which i don't control and can change in each invocation

    @Autowired
    private lateinit var wrapper: EmployeeJpaWrapper

    @Test
    fun saveEmployee() {
        var employee = Employee("Sam", "Don", "architect")
        employee = wrapper.saveEmployee(employee)

        assertThat(wrapper.findEmployeeById(employee.id)).isEqualTo(employee)
    }

    @Test
    fun throwsEmployeeNotFoundException() {
        val exception = assertThrows<EmployeeNotFoundException> {
            wrapper.findEmployeeById(1)
        }

        assertThat(exception.message).isEqualTo("Could not find employee 1")
    }

    @Test
    fun allEmployees() {
        val employee1 = Employee("Sam", "Don", "architect")
        wrapper.saveEmployee(employee1)

        val employee2 = employee1.copy().apply {
            name = "Mas Nod"
            role = "negative"
        }
        wrapper.saveEmployee(employee2)

        val allEmployees = wrapper.allEmployees()
        assertThat(allEmployees).hasSize(2)

        assertThat(allEmployees).containsExactly(employee1, employee2)
    }

    @Test
    fun updateEmployee() {
        val employee = Employee("Sam", "Don", "architect")
        val savedEmployee = wrapper.saveEmployee(employee)

        val update = savedEmployee.copy("Mas", "Nod", "negative")
        update.id = savedEmployee.id
        val updatedEmployee = wrapper.updateEmployee(update)

        assertThat(wrapper.allEmployees()).hasSize(1)
        assertThat(savedEmployee.id).isEqualTo(updatedEmployee.id)
        assertThat(wrapper.findEmployeeById(savedEmployee.id)).isEqualTo(Employee("Mas", "Nod", "negative"))
    }

    @Test
    fun updateCreatesEmployeeIfItDoesNotExit() {
        val update = Employee("Mas", "Nod", "negative").apply {
            id = 100
        }
        val updatedEmployee = wrapper.updateEmployee(update)

        assertThat(wrapper.allEmployees()).hasSize(1)
        // Jpa creates a new employee but next consecutive id is taken into account rather than the provided id
        assertThat(updatedEmployee.id).isNotEqualTo(100)
        assertThat(wrapper.findEmployeeById(updatedEmployee.id)).isEqualTo(Employee("Mas", "Nod", "negative"))
    }

    @Test
    fun deleteEmployeeById() {
        val employee = Employee("Sam", "Don", "architect")
        val savedId = wrapper.saveEmployee(employee).id

        wrapper.deleteEmployeeById(savedId)

        assertThrows<EmployeeNotFoundException> {
            wrapper.findEmployeeById(savedId)
        }
    }

    @Test
    fun deleteEmployee() {
        val employee = Employee("Sam", "Don", "architect")
        val savedEmployee = wrapper.saveEmployee(employee)

        wrapper.deleteEmployee(savedEmployee)

        assertThrows<EmployeeNotFoundException> {
            wrapper.findEmployeeById(savedEmployee.id)
        }
    }

}