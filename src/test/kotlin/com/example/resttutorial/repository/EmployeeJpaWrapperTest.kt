package com.example.resttutorial.repository

import com.example.resttutorial.Employee
import com.example.resttutorial.EmployeeNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(EmployeeJpaWrapper::class)
class EmployeeJpaWrapperTest {

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

}