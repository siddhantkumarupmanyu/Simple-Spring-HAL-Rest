package com.example.resttutorial.repository

import com.example.resttutorial.Employee

interface EmployeeRepository {

    fun allEmployees(): List<Employee>

    fun findEmployeeById(id: Long): Employee

    fun saveEmployee(employee: Employee): Employee

    fun updateEmployee(employee: Employee): Employee

    fun deleteEmployee(employee: Employee)

    fun deleteEmployeeById(id: Long)
}