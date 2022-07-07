package com.example.resttutorial.employee

class EmployeeNotFoundException(id: Long) : RuntimeException("Could not find employee $id")
