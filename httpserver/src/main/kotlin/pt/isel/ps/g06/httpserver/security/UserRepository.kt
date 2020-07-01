package pt.isel.ps.g06.httpserver.security

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String?): User
}