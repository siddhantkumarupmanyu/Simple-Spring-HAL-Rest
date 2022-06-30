package com.example.resttutorial

class EmployeeNotFoundException(id: Long) : RuntimeException("Could not find employee $id")
