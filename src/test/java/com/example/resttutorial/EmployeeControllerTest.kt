package com.example.resttutorial

import com.example.resttutorial.repository.EmployeeRepository
import com.jayway.jsonpath.JsonPath
import net.minidev.json.JSONArray
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

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

        verify(repository).allEmployees()
    }

    @Test
    fun getEmployees() {
        val sam = Employee("Sam", "Don", "architect").apply { id = 1 }
        val man = Employee("Mas", "Nod", "negative").apply { id = 2 }
        val employees = listOf(sam, man)
        `when`(repository.allEmployees()).thenReturn(employees)

        val result = mockMvc.perform(get("/employees"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$._links.self.href").value("http://localhost/employees"))
            .andExpect(jsonPath("$._embedded").exists())
            .andReturn()

        val jsonString = result.response.contentAsString

        assertEmployee(getEmbeddedEmployeeMap(jsonString, 1), 1, "Sam Don", "Sam", "Don", "architect")
        assertEmployee(getEmbeddedEmployeeMap(jsonString, 2), 2, "Mas Nod", "Mas", "Nod", "negative")
    }

    // Single item

    @Test
    fun getEmployee() {
        val sam = Employee("Sam", "Don", "architect").apply { id = 1 }
        val mas = Employee("Mas", "Nod", "negative").apply { id = 2 }
        val employees = listOf(sam, mas)
        `when`(repository.findEmployeeById(1)).thenReturn(sam)
        `when`(repository.findEmployeeById(2)).thenReturn(mas)

        var result = mockMvc.perform(get("/employees/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$._links.self.href").value("http://localhost/employees/1"))
            .andReturn()

        var employeeMap = JsonPath.read<Map<Any, Any>>(result.response.contentAsString, "$")
        assertEmployee(employeeMap, 1, "Sam Don", "Sam", "Don", "architect")

        result = mockMvc.perform(get("/employees/2"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$._links.self.href").value("http://localhost/employees/2"))
            .andReturn()

        employeeMap = JsonPath.read(result.response.contentAsString, "$")
        assertEmployee(employeeMap, 2, "Mas Nod", "Mas", "Nod", "negative")
    }

    // tbh, employee not found shouldn't be an exception.
    // it should be following Null Object Pattern cause employee not there is imo not an exception
    // exceptions should be exceptional
    // but fine. it depends on domain. in this case it as simple as it gets
    // but Null Object Pattern should be used
    // if for domain No Employee is an actual result and not exception...
    @Test
    fun employeeNotFound() {
        `when`(repository.findEmployeeById(3)).thenThrow(EmployeeNotFoundException(3))

        var result = mockMvc.perform(get("/employees/3"))
            .andExpect(status().isNotFound)
            .andExpect(content().string("Could not find employee 3"))
            .andReturn()
    }

    @Test
    fun newEmployee() {
        `when`(repository.saveEmployee(any())).thenAnswer { i ->
            val employee = i.getArgument<Employee>(0)
            employee.id = 1
            employee
        }

        val result = mockMvc.perform(
            post("/employees")
                .content("{\"firstName\": \"Sam\", \"lastName\": \"Don\", \"role\": \"architect\"}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "http://localhost/employees/1"))
            .andReturn()

        verify(repository).saveEmployee(Employee("Sam", "Don", "architect"))

        val employee = JsonPath.read<Map<Any, Any>>(result.response.contentAsString, "$")
        assertEmployee(employee, 1, "Sam Don", "Sam", "Don", "architect")
    }

    @Test
    fun newEmployeeFromName() {
        `when`(repository.saveEmployee(any())).thenAnswer { i ->
            val employee = i.getArgument<Employee>(0)
            employee.id = 1
            employee
        }

        val result = mockMvc.perform(
            post("/employees")
                .content("{\"name\": \"Sam Don\", \"role\": \"architect\"}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "http://localhost/employees/1"))
            .andReturn()

        verify(repository).saveEmployee(Employee("Sam", "Don", "architect"))

        val employee = JsonPath.read<Map<Any, Any>>(result.response.contentAsString, "$")
        assertEmployee(employee, 1, "Sam Don", "Sam", "Don", "architect")
    }

    @Test
    fun deleteEmployee() {
        mockMvc.perform(delete("/employees/1"))
            .andExpect(status().isNoContent)

        verify(repository).deleteEmployeeById(1)
    }

    @Test
    fun updateEmployee() {
        val updatedEmployee = Employee("Mas", "Nod", "negative").apply {
            id = 1
        }

        `when`(repository.updateEmployee(updatedEmployee)).thenReturn(updatedEmployee)

        val result = mockMvc.perform(
            put("/employees/1")
                .content("{\"name\": \"Mas Nod\", \"role\": \"negative\"}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "http://localhost/employees/1"))
            .andReturn()

        verify(repository).updateEmployee(updatedEmployee)

        val employee = JsonPath.read<Map<Any, Any>>(result.response.contentAsString, "$")
        assertEmployee(employee, 1, "Mas Nod", "Mas", "Nod", "negative")
    }

    @Test
    fun updateEmployeeByName() {
        val updatedEmployee = Employee("Mas", "Nod", "negative").apply {
            id = 1
        }

        `when`(repository.updateEmployee(updatedEmployee)).thenReturn(updatedEmployee)

        val result = mockMvc.perform(
            put("/employees/1")
                .content("{\"firstName\": \"Mas\", \"lastName\": \"Nod\", \"role\": \"negative\"}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "http://localhost/employees/1"))
            .andReturn()

        verify(repository).updateEmployee(updatedEmployee)

        val employee = JsonPath.read<Map<Any, Any>>(result.response.contentAsString, "$")
        assertEmployee(employee, 1, "Mas Nod", "Mas", "Nod", "negative")
    }

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