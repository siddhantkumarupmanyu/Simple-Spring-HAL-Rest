package com.example.resttutorial

import com.example.resttutorial.repository.EmployeeJpaRepo
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoadDatabase {
    companion object {
        private val log = LoggerFactory.getLogger(LoadDatabase::class.java)
    }

    @Bean
    fun initDatabases(employeeJpaRepo: EmployeeJpaRepo, orderRepository: OrderRepository): CommandLineRunner {
        return CommandLineRunner { args ->
            employeeJpaRepo.save(Employee("Bilbo", "Baggins", "burglar"))
            employeeJpaRepo.save(Employee("Frodo", "Baggins", "thief"))

            employeeJpaRepo.findAll().forEach { employee ->
                log.info("Preloaded $employee")
            }

            orderRepository.save(Order("MacBook Pro", Status.COMPLETED))
            orderRepository.save(Order("iPhone", Status.IN_PROGRESS))

            orderRepository.findAll().forEach { order ->
                log.info("Preloaded $order")
            }
        }
    }
    // Spring Boot will run ALL CommandLineRunner beans once the application context is loaded.

}