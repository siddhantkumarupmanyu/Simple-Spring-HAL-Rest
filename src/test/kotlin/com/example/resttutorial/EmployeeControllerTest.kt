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

        val jsonString = result.response.contentAsString

        assertEmbeddedEmployee(jsonString, 1, "Sam Don", "Sam", "Don", "architect")
    }


    @Suppress("UNCHECKED_CAST")
    private fun assertEmbeddedEmployee(
        jsonString: String,
        id: Int,
        name: String,
        firstName: String,
        lastName: String,
        role: String
    ) {
        val employee =
            JsonPath.read<JSONArray>(
                jsonString,
                "$._embedded.employeeList[?(@.id == $id)]"
            )[0] as Map<Any, Any>

        assertThat(employee["id"]).isEqualTo(id)
        assertThat(employee["name"]).isEqualTo(name)
        assertThat(employee["firstName"]).isEqualTo(firstName)
        assertThat(employee["lastName"]).isEqualTo(lastName)
        assertThat(employee["role"]).isEqualTo(role)

        val selfHref = JsonPath.read<String>(employee["_links"], "$.self.href")
        assertThat(selfHref).isEqualTo("http://localhost/employees/$id")

        val employeesHref = JsonPath.read<String>(employee["_links"], "$.employees.href")
        assertThat(employeesHref).isEqualTo("http://localhost/employees")
    }
}

// for future ref...
// https://stackoverflow.com/a/64486074