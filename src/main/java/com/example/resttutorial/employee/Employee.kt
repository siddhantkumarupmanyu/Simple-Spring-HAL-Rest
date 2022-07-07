package com.example.resttutorial.employee

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

// i am hating these nullable types
// and null checks
// rest backward compatibility makes shit hard...
// maybe it's spring specific,
// it puts null instead of empty string if in post json value it's not
// and right now idk, if i can configure that

@Entity
data class Employee(
    var firstName: String?,
    var lastName: String?,
    var role: String
) {
    @Id
    @GeneratedValue
    var id: Long = -1

    var name: String? = ""
        get() {
            return "$firstName $lastName"
        }
        set(value) {
            if (value.isNullOrEmpty()) {
                field = ""
                return
            }
            val parts = value.split(" ")
            firstName = parts[0]
            lastName = parts[1]
        }
}
