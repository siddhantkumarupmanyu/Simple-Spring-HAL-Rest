package com.example.resttutorial

import com.jayway.jsonpath.JsonPath
import net.minidev.json.JSONArray
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(EmployeeController::class)
class EmployeeControllerTest {

    @MockBean
    private lateinit var repository: EmployeeRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun noEmployees() {
        mockMvc.perform(get("/employees"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$._links.self.href").value("http://localhost/employees"))
            .andExpect(jsonPath("$._embedded").doesNotExist())

        verify(repository).findAll()
    }

    @Test
    fun employees() {
        val sam = Employee("Sam", "Don", "architect").apply { id = 1 }
        val man = Employee("Man", "Nod", "negative").apply { id = 2 }
        val employees = listOf(sam, man)
        `when`(repository.findAll()).thenReturn(employees)

        val result = mockMvc.perform(get("/employees"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$._links.self.href").value("http://localhost/employees"))
            .andExpect(jsonPath("$._embedded").exists())
            .andReturn()

        val employee1 =
            JsonPath.read<JSONArray>(
                result.response.contentAsString,
                "$._embedded.employeeList[?(@.id == 1)]"
            )[0] as Map<Any, Any>

        assertThat(employee1["id"]).isEqualTo(1)
        assertThat(employee1["name"]).isEqualTo("Sam Don")
        assertThat(employee1["firstName"]).isEqualTo("Sam")
        assertThat(employee1["lastName"]).isEqualTo("Don")
        assertThat(employee1["role"]).isEqualTo("architect")

        println(employee1)

        val selfHref = JsonPath.read<String>(employee1["_links"], "$.self.href")
        assertThat(selfHref).isEqualTo("http://localhost/employees/1")

        val employeesHref = JsonPath.read<String>(employee1["_links"], "$.employees.href")
        assertThat(employeesHref).isEqualTo("http://localhost/employees")
    }
}

// for future ref...
// https://stackoverflow.com/a/64486074