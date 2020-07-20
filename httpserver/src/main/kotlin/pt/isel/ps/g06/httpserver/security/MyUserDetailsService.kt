package pt.isel.ps.g06.httpserver.security

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.BEARER
import pt.isel.ps.g06.httpserver.common.exception.authentication.NotAuthenticatedException
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmitterDbRepository
import java.time.OffsetDateTime

@Service
class MyUserDetailsService(
        private val jwtUtil: JwtUtil,
        private val userRepo: UserRepository,
        private val submitterDbRepository: SubmitterDbRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val submitter = submitterDbRepository.getSubmitterByName(username)
                ?: throw NotAuthenticatedException()//UsernameNotFoundException("The username is missing")
        val user = userRepo.getBySubmitterId(submitter.submitter_id)
        return User(submitter.submitter_name, user!!.password, ArrayList())
    }

    fun registerUser(email: String, username: String, password: String) {
        val submitter = submitterDbRepository.insertSubmitter(
                name = username,
                date = OffsetDateTime.now(),
                type = "User"
        )

        userRepo.insertUser(
                submitterId = submitter.submitter_id,
                email = email,
                password = password
        )
    }

    fun getSubmitterByUsername(username: String): DbSubmitterDto {
        return submitterDbRepository.getSubmitterByName(username)
                ?: throw NotAuthenticatedException()
    }

    fun getSubmitterIdFromJwt(jwt: String): Int {
        // Extract username from sent token using the server secret
        val username = jwtUtil.getUsername(jwt.removePrefix(BEARER))

        // Get submitterId from the obtained username
        val submitterByUsername = getSubmitterByUsername(username)

        return submitterByUsername.submitter_id
    }
}