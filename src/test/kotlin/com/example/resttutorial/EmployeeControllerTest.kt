package com.example.resttutorial

import org.junit.jupiter.api.Test
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {


    @MockBean
    private lateinit var repository: EmployeeRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun getEmployees() {
        mockMvc.perform(get("/employees"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$._links.self.href").value("http://localhost/employees"))
        // .andExpect(jsonPath("$._embedded").doesNotExist())

        verify(repository).findAll()
    }

    // one with @SpringBootTest
    // another with @WebMvnTest
    // and we can see the time taken by each

    // if i am just testing a controller should i just
    // use
    // https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing.spring-boot-applications.spring-mvc-tests
    // https://stackoverflow.com/a/44127619
    // heaviness decreases ↓
    // @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    //      => starts up the whole server with everything
    //      => e2e with TestRestTemplate
    // @SpringBootTest
    // @AutoConfigureMockMvc
    //      => configures the mcv layer with Json and everything
    //      => loads the configs
    //      => test the @RestController with result returning as Json rather than value object(vo).
    // @WebMvcTest - for testing the controller layer only
    //      => no json marshaling or unmarshalling (not sure if that's how it is)(i will test it, nevertheless)
    //          - imo, it should do marshalling and unmarshalling
    //      => ut kind of test for controller, idk one of the guy said that so i just copy pasted
    // for more info visit
    // https://stackoverflow.com/questions/39865596/difference-between-using-mockmvc-with-springboottest-and-using-webmvctest

    // my req is, i want to load mock repo and only test the Controller
    // but if i am integration testing it, i want the result to be in json
    // rather than vo.
    // if @WebMvcTest is lightweight, runs fast, and provides better isolation than @SpringBootTest @AutoConfigureMockMvc,
    // i would just use that.
    // after all tests should be FIRST...
}