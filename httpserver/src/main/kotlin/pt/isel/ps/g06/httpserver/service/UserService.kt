package pt.isel.ps.g06.httpserver.service

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.authentication.NotAuthenticatedException
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.submitter.SubmitterResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmitterDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.UserRepository
import pt.isel.ps.g06.httpserver.model.Submitter

@Service
class UserService(
        private val userRepo: UserRepository,
        private val submitterDbRepository: SubmitterDbRepository,
        private val submitterMapper: SubmitterResponseMapper
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

    fun getSubmitterFromUsername(username: String): Submitter? {
        return submitterDbRepository
                .getSubmitterByName(username)
                ?.let(submitterMapper::mapTo)
    }
}