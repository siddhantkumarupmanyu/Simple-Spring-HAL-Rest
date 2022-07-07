package com.example.resttutorial.order

class OrderNotFoundException(id: Long) : RuntimeException("Could not find order $id")