package com.example.resttutorial

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RestTutorialPayrollApplication

fun main(args: Array<String>) {
    runApplication<RestTutorialPayrollApplication>(*args)
}

// i understood difference b/w @Bean and @Component,
// @Component is class level annotation and @Bean is method level
// @Component makes that class an injectable, like registers that class in the di system
// with Bean you have to create a @Configuration Class and under that you can annotate
// methods @Beans for specific Types/Classes
// like in Dagger2/hilt
// we can define a Module, so think of @Module as @Configuration
// and @Provides like @Bean
// also, we can just annotate the class we want to register with di system with @Inject in its constructor
// same with @Component
// like a repository instead of creating a module in this case a @Configuration
// we can just annotate the class with @Component like in there we can just @Inject
// without going through / creating that/any module

// also, there are only these two things, if i understood correctly
// @Service/@Repository...
// they are just @Component made to be distinguishable from other Components as in reading
// they have no other effect...

// more info
// https://stackoverflow.com/questions/10604298/spring-component-versus-bean
// https://stackoverflow.com/questions/39174669/what-is-the-difference-between-configuration-and-component-in-spring

// https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/using-boot-using-springbootapplication-annotation.html
// @SpringBootApplication
// is nothing more than
// @EnableAutoConfiguration: enable Spring Boot’s auto-configuration mechanism
// @ComponentScan: enable @Component scan on the package where the application is located (see the best practices)
// @Configuration: allow to register extra beans in the context or import additional configuration classes

