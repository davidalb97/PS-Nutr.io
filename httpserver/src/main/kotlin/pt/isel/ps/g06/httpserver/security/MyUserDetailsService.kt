package pt.isel.ps.g06.httpserver.security

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class MyUserDetailsService : UserDetailsService {

    /*@Autowired
    private lateinit var userRepo: UserRepository*/

    override fun loadUserByUsername(username: String?): UserDetails {
        //val user = userRepo.findByUsername(username) ?: throw UsernameNotFoundException("The user was not found")
        return User("test", "test", ArrayList())
    }
}