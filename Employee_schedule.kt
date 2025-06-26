data class Employee(val name: String, val preferences: Map<String, String>)

val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
val shifts = listOf("morning", "afternoon", "evening")

fun main() {
    val employees = listOf(
        Employee("Ram", mapOf("Monday" to "morning", "Tuesday" to "afternoon", "Wednesday" to "morning")),
        Employee("Gita", mapOf("Monday" to "morning", "Wednesday" to "evening", "Friday" to "afternoon")),
        Employee("Gaurab", mapOf("Tuesday" to "morning", "Thursday" to "evening", "Saturday" to "afternoon")),
        Employee("Saurab", mapOf("Monday" to "afternoon", "Tuesday" to "morning", "Wednesday" to "afternoon")),
        Employee("Sita", mapOf("Friday" to "morning", "Saturday" to "evening", "Sunday" to "morning"))
    )

    val schedule = mutableMapOf<String, MutableMap<String, MutableList<String>>>()
    val employeeDaysWorked = mutableMapOf<String, Int>()

    for (day in days) {
        schedule[day] = mutableMapOf()
        for (shift in shifts) {
            schedule[day]!![shift] = mutableListOf()
        }
    }

    for (employee in employees) {
        employeeDaysWorked[employee.name] = 0
    }

    // Assign preferred shifts
    for (employee in employees) {
        for ((day, preferredShift) in employee.preferences) {
            if (employeeDaysWorked[employee.name]!! >= 5) continue
            if (schedule[day]!![preferredShift]!!.size < 2) {
                schedule[day]!![preferredShift]!!.add(employee.name)
                employeeDaysWorked[employee.name] = employeeDaysWorked[employee.name]!! + 1
            } else {
                for (altShift in shifts) {
                    if (altShift != preferredShift && schedule[day]!![altShift]!!.size < 2) {
                        schedule[day]!![altShift]!!.add(employee.name)
                        employeeDaysWorked[employee.name] = employeeDaysWorked[employee.name]!! + 1
                        break
                    }
                }
            }
        }
    }

    // Fill remaining shift gaps
    for (day in days) {
        for (shift in shifts) {
            while (schedule[day]!![shift]!!.size < 2) {
                val candidates = employees.filter {
                    employeeDaysWorked[it.name]!! < 5 &&
                            !schedule[day]!!.values.flatten().contains(it.name)
                }
                if (candidates.isNotEmpty()) {
                    val randomEmployee = candidates.random()
                    schedule[day]!![shift]!!.add(randomEmployee.name)
                    employeeDaysWorked[randomEmployee.name] = employeeDaysWorked[randomEmployee.name]!! + 1
                } else break
            }
        }
    }

    // Output final schedule
    println("Final Weekly Schedule:")
    for (day in days) {
        println("$day:")
        for (shift in shifts) {
            val assigned = schedule[day]!![shift]!!
            val names = if (assigned.isNotEmpty()) assigned.joinToString(", ") else "No employee is assigned"
            println("  $shift: $names")
        }
    }
}