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
    // todo: fake would be much better right. it's mocking too much,
    // like i need to even find employee by id and stuff

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun noEmployees() {
        mockMvc.perform(get("/employees"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$._links.self.href").value("http://localhost/employees"))
            .andExpect(jsonPath("$._embedded").doesNotExist())

        verify(repository).allEmployees()
    }

    @Test
    fun getEmployees() {
        val sam = Employee("Sam", "Don", "architect").apply { id = 1 }
        val man = Employee("Man", "Nod", "negative").apply { id = 2 }
        val employees = listOf(sam, man)
        `when`(repository.allEmployees()).thenReturn(employees)

        val result = mockMvc.perform(get("/employees"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$._links.self.href").value("http://localhost/employees"))
            .andExpect(jsonPath("$._embedded").exists())
            .andReturn()

        val jsonString = result.response.contentAsString

        assertEmployee(getEmbeddedEmployeeMap(jsonString, 1), 1, "Sam Don", "Sam", "Don", "architect")
        assertEmployee(getEmbeddedEmployeeMap(jsonString, 2), 2, "Man Nod", "Man", "Nod", "negative")
    }

    // Single item
    @Test
    fun getEmployee() {
        val sam = Employee("Sam", "Don", "architect").apply { id = 1 }
        val man = Employee("Man", "Nod", "negative").apply { id = 2 }
        val employees = listOf(sam, man)
        `when`(repository.findEmployeeById(1)).thenReturn(sam)

        val result = mockMvc.perform(get("/employees/1"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$._links.self.href").value("http://localhost/employees/1"))
            .andReturn()

        TODO()
        // assertEmbeddedEmployee(result.response.contentAsString, 1, "Sam Don", "Sam", "Don", "architect")
    }

    // todo: test 404 when repos throws employee not found

    // todo: make it more reusable. remove the use of _embedded
    @Suppress("UNCHECKED_CAST")
    private fun assertEmployee(
        employeeMap: Map<Any, Any>,
        id: Int,
        name: String,
        firstName: String,
        lastName: String,
        role: String
    ) {
        assertThat(employeeMap["id"]).isEqualTo(id)
        assertThat(employeeMap["name"]).isEqualTo(name)
        assertThat(employeeMap["firstName"]).isEqualTo(firstName)
        assertThat(employeeMap["lastName"]).isEqualTo(lastName)
        assertThat(employeeMap["role"]).isEqualTo(role)

        val selfHref = JsonPath.read<String>(employeeMap["_links"], "$.self.href")
        assertThat(selfHref).isEqualTo("http://localhost/employees/$id")

        val employeesHref = JsonPath.read<String>(employeeMap["_links"], "$.employees.href")
        assertThat(employeesHref).isEqualTo("http://localhost/employees")
    }

    @Suppress("UNCHECKED_CAST")
    private fun getEmbeddedEmployeeMap(
        jsonString: String,
        id: Int
    ) = JsonPath.read<JSONArray>(
        jsonString,
        "$._embedded.employeeList[?(@.id == $id)]"
    )[0] as Map<Any, Any>
}

// for future ref...
// https://stackoverflow.com/a/64486074

// .andDo(MockMvcResultHandlers.print())