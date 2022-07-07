package com.example.resttutorial.repository

import com.example.resttutorial.Employee
import com.example.resttutorial.EmployeeNotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
class EmployeeJpaWrapper(
    private val jpaRepo: EmployeeJpaRepo
) : EmployeeRepository {

    override fun findEmployeeById(id: Long): Employee {
        return jpaRepo.findById(id).orElseThrow {
            EmployeeNotFoundException(id)
        }
    }

    override fun allEmployees(): List<Employee> {
        return jpaRepo.findAll()
    }

    override fun saveEmployee(employee: Employee): Employee {
        return jpaRepo.save(employee)
    }

    override fun updateEmployee(employee: Employee): Employee {
        return jpaRepo.save(employee)
    }

    override fun deleteEmployee(employee: Employee) {
        return jpaRepo.delete(employee)
    }

    override fun deleteEmployeeById(id: Long) {
        return jpaRepo.deleteById(id)
    }

}

@Repository
interface EmployeeJpaRepo : JpaRepository<Employee, Long> {}