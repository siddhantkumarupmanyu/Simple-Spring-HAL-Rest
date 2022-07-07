package com.example.resttutorial.employee.repository

import com.example.resttutorial.employee.Employee

interface EmployeeRepository {

    fun allEmployees(): List<Employee>

    fun findEmployeeById(id: Long): Employee

    fun saveEmployee(employee: Employee): Employee

    fun updateEmployee(employee: Employee): Employee

    fun deleteEmployee(employee: Employee)

    fun deleteEmployeeById(id: Long)
}