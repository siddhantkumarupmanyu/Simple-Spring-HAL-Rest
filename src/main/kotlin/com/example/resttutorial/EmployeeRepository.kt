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

interface EmployeeRepository : JpaRepository<Employee, Long> {

}