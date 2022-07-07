package com.example.resttutorial

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus


@ControllerAdvice
class NotFoundAdvice {


    @ResponseBody
    @ExceptionHandler(EmployeeNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun employeeNotFoundHandler(ex: EmployeeNotFoundException): String {
        return ex.message ?: "Something went wrong"
    }

    @ResponseBody
    @ExceptionHandler(OrderNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun orderNotFoundHandler(ex: OrderNotFoundException): String {
        return ex.message ?: "Something went wrong"
    }

}