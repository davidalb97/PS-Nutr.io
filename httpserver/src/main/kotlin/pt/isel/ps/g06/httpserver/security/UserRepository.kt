package pt.isel.ps.g06.httpserver.security

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/*
@Service
interface UserRepository : JpaRepository<UserAuthRequest, Long> {

    fun findByUsername(username: String?): User

    @Transactional
    fun deleteUser(username: String?)
}*/
