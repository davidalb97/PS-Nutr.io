package pt.isel.ps.g06.httpserver.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class NutriUserDetailsService(
        @Autowired private val userRepo: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userRepo.findByUsername(username) ?: throw UsernameNotFoundException("The user was not found")
        return CurrentUser(user)
    }
}