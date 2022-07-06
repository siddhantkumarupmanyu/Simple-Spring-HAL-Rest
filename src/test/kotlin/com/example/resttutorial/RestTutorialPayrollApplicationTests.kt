package com.example.resttutorial

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RestTutorialPayrollApplicationTests {

    // this test fails cause i didn't register EmployeeJpaWrapper with the spring's di system yet...
    // it's failing for the right reason and i am liking that...
    // we will fix it when writing e2e tests...
    @Test
    fun contextLoads() {
    }

}
