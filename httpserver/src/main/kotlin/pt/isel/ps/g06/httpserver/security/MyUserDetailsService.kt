package pt.isel.ps.g06.httpserver.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmitterDbRepository

@Service
class MyUserDetailsService(
        @Autowired private val userRepo: UserRepository,
        @Autowired private val submitterDbRepository: SubmitterDbRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val submitter = submitterDbRepository.getSubmitterByName(username!!)
        val user = userRepo.getBySubmitterId(submitter.submitter_id)
        return User(submitter.submitter_name, user!!.password, ArrayList())
                ?: throw UsernameNotFoundException("The user was not found")
    }
}