package com.example.tennis.model

enum class UserRole {
    MEMBER, ADHERENT
}

data class User(val id: Int, val name: String, var role: UserRole)

fun main() {
    val users = mutableListOf(
        User(1, "Alice", UserRole.MEMBER),
        User(2, "Bob", UserRole.ADHERENT),
        User(3, "Charlie", UserRole.MEMBER),
        User(4, "Dave", UserRole.MEMBER),
        User(5, "Eve", UserRole.ADHERENT)
    )

    val isAdmin = true // Remplacez ceci par votre vérification de rôle d'administrateur

    // Print out list of user names and roles
    println("Users:")
    for (user in users) {
        println("${user.name} (${user.role})")
        if (isAdmin) {
            // Permettre à l'administrateur de modifier le rôle de l'utilisateur
            println("Voulez-vous changer le rôle de ${user.name} ? (y/n)")
            val input = readLine() ?: ""
            if (input == "y") {
                println("Quel rôle voulez-vous donner à ${user.name} ? (Membre/Adhérent)")
                val newRoleInput = readLine() ?: ""
                when (newRoleInput.toLowerCase()) {
                    "membre" -> user.role = UserRole.MEMBER
                    "adhérent" -> user.role = UserRole.ADHERENT
                }
                println("${user.name} a maintenant le rôle ${user.role}")
            }
        }
    }
}

