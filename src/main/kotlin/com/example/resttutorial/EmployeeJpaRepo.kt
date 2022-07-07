package com.example.resttutorial

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

interface EmployeeRepository {
    fun allEmployees(): List<Employee>
    fun findEmployeeById(id: Long): Employee
    fun saveEmployee(employee: Employee): Employee
    fun updateEmployee(employee: Employee): Employee
    fun deleteEmployee(employee: Employee)
    fun deleteEmployeeById(id: Long)
}

// todo: create package repositories and separate these class into different files
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

// tbh, i am not liking the way spring is doing this stuff...
// i don't want to mock/fake stuff from those guys...
// i want to mock stuff/interfaces that i own...
// else see, or e.g. faking/creating a fake is harder than ever
// second, when mocking i need to know which method of the spring JpaRepository is called by the class under test
// which is like knowing the implementation, and i hate that...

// this way i am trying to wrap the JpaRepository or something idk
// tbh, room/android pattern's is much better with this one.
// you define you own repository, here i am doing the same thing but with so much hassle
// so EmployeeJpaWrapper is actually kind of a wrapper
// and doing do give an opportunity to make/take repository in my domain
// EmployeeJpaRepo's impl should be provided by Spring.
// and EmployeeJpaWrapper is a wrapper over AutoImplJpaEmployeeRepo JpaRepository
// this makes things in my domain rather than springs...
// now i own the EmployeeRepository interface.
// and things are just wrappers which i can integration test
// and i can have different type of wrappers, Jap, Mongo, etc...
// and with leveraging the springs di system, i can just,
// inject them at places needed.
// now my domain relies on interface EmployeeRepository
// which i actually own...
// take very simple case of controller relying on EmployeeRepository interface
// than the impl here the wrapper...
// i can easily test that guy, mock the interface that i own
// and moreover i can just, leverage the spring's di.
// instead of going around and changing the wrapper type everywhere, which is just worst,
// apart from time-consuming, risk of breaking everything.
// i can just annotate another wrapper and springs di takes care of everything...
// well, tbh controller using concrete impl is just bad.
// how can you even test that.
// if there is a concrete impl. being used. it means that controller is not tested.
// else it would have/take/relies on the interface than a concert impl...
// being a tdd guy, how can i even think of that scenario... lol
// that should be like doomsday thing... lol XD
// i am repeating the same things but yup...
// i am just liking this structure/design so much...
// having the wrapper around stuff... wow...
