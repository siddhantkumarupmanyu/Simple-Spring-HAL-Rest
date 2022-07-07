package com.example.resttutorial.repository

import com.example.resttutorial.Employee
import org.springframework.data.jpa.repository.JpaRepository


// tbh i don't find myself comfortable around extending interfaces
// i feel interfaces are abstract things and then there are impl. of those.
// with extending interfaces i am creating an unneeded coupling
// and most importantly hierarchy
// which not only hinders understandability and readability
// but makes it harder to makes sense of code.
// no i need to navigate between multiple interfaces
// someone would say this is just DRY but
// tbh imo, this is abuse of DRY

// i hate interface hierarchy
// another reason i hate them is cause they make the testing harder.
// now i need to like fake/mock everything by JpaRepository... :(
// and another issue is i don't own that interface... :(

// should be auto implemented by spring, and injected in EmployeeJpaWrapper
interface EmployeeJpaRepo : JpaRepository<Employee, Long> {}

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

