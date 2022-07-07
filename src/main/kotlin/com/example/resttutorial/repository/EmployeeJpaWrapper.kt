package com.example.resttutorial.repository

import com.example.resttutorial.Employee
import com.example.resttutorial.EmployeeNotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// todo:
// impl.
// i haven't made this @Component so,
// so, it's not registered with the springs di system, rn
// i think e2e tests should take care of this
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
        TODO()
    }

    override fun deleteEmployee(employee: Employee) {
        TODO()
    }

    override fun deleteEmployeeById(id: Long) {
        TODO("Not yet implemented")
    }

}

@Repository
interface EmployeeJpaRepo : JpaRepository<Employee, Long> {}