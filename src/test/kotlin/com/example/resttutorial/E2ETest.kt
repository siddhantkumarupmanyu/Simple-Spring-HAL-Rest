package com.example.resttutorial

import com.jayway.jsonpath.JsonPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class E2ETest {

    @LocalServerPort
    var randomServerPort = 0

    @Autowired
    private lateinit var template: TestRestTemplate

    private lateinit var baseUrl: String

    @BeforeEach
    fun setUp() {
        baseUrl = "http://localhost:$randomServerPort"
    }

    @Test
    fun notFoundForRoot() {
        val response = template.getForEntity<String>("$baseUrl/")
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    // i don't want to complicate stuff for this simple project
    // else this function can be easily broken apart into different ones.
    @Test
    fun employeeFlow() {
        // add one employee
        var headers = HttpHeaders()
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        var requestBody = "{\"firstName\": \"Sam\", \"lastName\": \"Don\", \"role\": \"architect\"}"
        var request = HttpEntity<String>(requestBody, headers)
        var response = template.postForEntity<String>("$baseUrl/employees", request)
        val location = response.headers.location.toString()
        var selfLink = JsonPath.read<String>(response.body, "$._links.self.href")
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(selfLink).isEqualTo(location)

        // get that one employee
        // following the link returned in location header
        response = template.getForEntity(location)
        var firstName = JsonPath.read<String>(response.body, "$.firstName")
        var lastName = JsonPath.read<String>(response.body, "$.lastName")
        var role = JsonPath.read<String>(response.body, "$.role")
        selfLink = JsonPath.read(response.body, "$._links.self.href")
        val employeesLink = JsonPath.read<String>(response.body, "$._links.employees.href")
        assertThat(firstName).isEqualTo("Sam")
        assertThat(lastName).isEqualTo("Don")
        assertThat(role).isEqualTo("architect")
        assertThat(selfLink).isEqualTo(location)
        assertThat(employeesLink).isEqualTo("$baseUrl/employees")

        // update that one employee
        // in returned json check if it's updated
        headers = HttpHeaders()
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        requestBody = "{\"firstName\": \"Mas\", \"lastName\": \"Nod\", \"role\": \"negative\"}"
        request = HttpEntity<String>(requestBody, headers)
        response = template.exchange(location, HttpMethod.PUT, request)
        val updatedLocation = response.headers.location.toString()
        firstName = JsonPath.read(response.body, "$.firstName")
        lastName = JsonPath.read(response.body, "$.lastName")
        role = JsonPath.read(response.body, "$.role")
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(updatedLocation).isEqualTo(location)
        assertThat(firstName).isEqualTo("Mas")
        assertThat(lastName).isEqualTo("Nod")
        assertThat(role).isEqualTo("negative")

        // delete that employee
        response = template.exchange(location, HttpMethod.DELETE)
        assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)

        // should return 404
        response = template.getForEntity(location)
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)

        // get all employee
        response = template.getForEntity("$baseUrl/employees")
        selfLink = JsonPath.read(response.body, "$._links.self.href")
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(selfLink).isEqualTo("$baseUrl/employees")
    }

}
