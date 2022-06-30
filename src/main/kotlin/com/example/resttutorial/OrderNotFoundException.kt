package com.example.resttutorial

class OrderNotFoundException(id: Long) : RuntimeException("Could not find order $id")