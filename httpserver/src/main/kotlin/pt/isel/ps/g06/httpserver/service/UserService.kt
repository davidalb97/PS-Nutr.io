package pt.isel.ps.g06.httpserver.service

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.MOD_USER
import pt.isel.ps.g06.httpserver.common.exception.authentication.NotAuthenticatedException
import pt.isel.ps.g06.httpserver.common.exception.authorization.NotAuthorizedException
import pt.isel.ps.g06.httpserver.common.exception.notFound.UserNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.UserResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.submitter.SubmitterResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmitterDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.UserDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.input.BanInput
import pt.isel.ps.g06.httpserver.model.Submitter

@Service
class UserService(
        private val userDbRepository: UserDbRepository,
        private val submitterDbRepository: SubmitterDbRepository,
        private val submitterMapper: SubmitterResponseMapper,
        private val userMapper: UserResponseMapper
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val userDto = userDbRepository.getByEmail(email)
                ?: throw NotAuthenticatedException()
        return User(email, userDto.password, emptyList())
    }

    fun registerUser(email: String, username: String, encodedPassword: String) {

        val submitter = submitterDbRepository.insertSubmitter(
                name = username,
                type = "User"
        )

        userDbRepository.insertUser(
                submitterId = submitter.submitter_id,
                email = email,
                password = encodedPassword
        )
    }

    fun getEmailFromSubmitter(submitterId: Int): pt.isel.ps.g06.httpserver.model.User =
            userDbRepository
                    .getBySubmitter(submitterId)
                    .let { dto -> userMapper.mapToModel(dto ?: throw UserNotFoundException()) }

    fun getUserFromEmail(email: String): pt.isel.ps.g06.httpserver.model.User =
            userDbRepository
                    .getByEmail(email)
                    .let { dto -> userMapper.mapToModel(dto ?: throw UserNotFoundException()) }

    fun getSubmitterFromEmail(email: String): Submitter? {

        val user = getUserFromEmail(email)

        return submitterDbRepository
                .getSubmitterBySubmitterId(user.identifier)
                .let(submitterMapper::mapTo)
    }

    fun updateUserBan(banInput: BanInput) =
            userDbRepository.updateUserBan(banInput.submitterId, banInput.isBanned)

    fun ensureModerator(user: pt.isel.ps.g06.httpserver.model.User) {
        if (user.userRole != MOD_USER) {
            throw NotAuthorizedException()
        }
    }
}