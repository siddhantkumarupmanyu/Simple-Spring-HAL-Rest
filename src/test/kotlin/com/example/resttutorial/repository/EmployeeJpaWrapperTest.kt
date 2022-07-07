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

        assertThat(employee.id).isEqualTo(1)
        assertThat(wrapper.findEmployeeById(1)).isEqualTo(employee)
    }

    @Test
    fun throwsEmployeeNotFoundException() {
        val exception = assertThrows<EmployeeNotFoundException> {
            wrapper.findEmployeeById(1)
        }

        assertThat(exception.message).isEqualTo("Could not find employee 1")
    }



}