package com.example.resttutorial.employee

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EmployeeTest {

    @Test
    fun employeeFromName() {
        val employee = Employee(null, null, "gardner")
        employee.name = "Sam Don"

        assertThat(employee.firstName).isEqualTo("Sam")
        assertThat(employee.lastName).isEqualTo("Don")
    }

    @Test
    fun employeeFromLastAndFirstName() {
        val employee = Employee("Sam", "Don", "gardner")

        assertThat(employee.name).isEqualTo("Sam Don")
    }

}