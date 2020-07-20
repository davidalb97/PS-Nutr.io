package pt.isel.ps.g06.httpserver.service

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.authentication.NotAuthenticatedException
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmitterDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.UserRepository

@Service
class UserService(
        private val userRepo: UserRepository,
        private val submitterDbRepository: SubmitterDbRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val userDto = userRepo.getBySubmitterName(username)
                ?: throw NotAuthenticatedException()
        return User(username, userDto.password, emptyList())
    }

    fun registerUser(email: String, username: String, encodedPassword: String) {

        val submitter = submitterDbRepository.insertSubmitter(
                name = username,
                type = "User"
        )

        userRepo.insertUser(
                submitterId = submitter.submitter_id,
                email = email,
                password = encodedPassword
        )
    }

    fun getSubmitterIdFromUserName(username: String): Int {

        val submitterByUsername = submitterDbRepository.getSubmitterByName(username)
                ?: throw NotAuthenticatedException()

        return submitterByUsername.submitter_id
    }
}