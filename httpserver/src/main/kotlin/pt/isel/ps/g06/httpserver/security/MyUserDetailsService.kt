package pt.isel.ps.g06.httpserver.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.user
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmitterDbRepository
import java.time.OffsetDateTime

@Service
class MyUserDetailsService(
        @Autowired private val userRepo: UserRepository,
        @Autowired private val submitterDbRepository: SubmitterDbRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val submitter = submitterDbRepository.getSubmitterByName(username!!)
        val user = userRepo.getBySubmitterId(submitter.submitter_id)
        return User(submitter.submitter_name, user!!.password, ArrayList())
    }

    fun registerUser(email: String, username: String, password: String) {
        submitterDbRepository.insertSubmitter(
                name = username,
                date = OffsetDateTime.now(),
                type = "User"
        )

        val submitterId = submitterDbRepository.getSubmitterByName(username).submitter_id

        userRepo.insertUser(
                submitterId = submitterId,
                email = email,
                password = password
        )
    }

    fun getSubmitterByUsername(username: String) = submitterDbRepository.getSubmitterByName(username)

}