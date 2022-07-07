package com.example.resttutorial.repository

import com.example.resttutorial.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// todo:
// impl.
// i haven't made this @Component so,
// so, it's not registered with the springs di system, rn
// i think e2e tests should take care of this
class EmployeeJpaWrapper(
    private val employeeJpaRepo: EmployeeJpaRepo
) : EmployeeRepository {

    override fun findEmployeeById(id: Long): Employee {
        TODO()
        // return
    }

    override fun allEmployees(): List<Employee> {
        TODO()
    }

    override fun saveEmployee(employee: Employee): Employee {
        TODO()
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