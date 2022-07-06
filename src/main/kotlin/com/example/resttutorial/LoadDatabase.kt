package com.example.resttutorial

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// is configuration like module or something
// like in hilt and dagger2 we have this concept of module
// is this the same thing??
// i guess so...
@Configuration
class LoadDatabase {
    companion object {
        private val log = LoggerFactory.getLogger(LoadDatabase::class.java)
    }

    // if this is what i think it is.
    // i don't need to mark my classes with @bean
    // and only classes with bean annotation can be injected.
    // this is better and much like android's hilt/dagger2 module
    // for controller's and other spring outer layer stuff that's necessary, and it makes sense
    // to annotate that class with @controller and stuff
    // but my classes, specially domain or inner classes i don't want to annotate them with framework
    // specific stuff.
    // in comparison with dagger2/hilt @bean is like @provides


    @Bean
    fun initDatabases(employeeRepository: EmployeeRepository, orderRepository: OrderRepository): CommandLineRunner {
        return CommandLineRunner { args ->
            employeeRepository.save(Employee("Bilbo", "Baggins", "burglar"))
            employeeRepository.save(Employee("Frodo", "Baggins", "thief"))

            employeeRepository.findAll().forEach { employee ->
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

//    mvnw spring-boot:run


}