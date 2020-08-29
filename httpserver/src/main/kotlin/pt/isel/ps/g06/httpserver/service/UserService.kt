package pt.isel.ps.g06.httpserver.service

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.MOD_USER
import pt.isel.ps.g06.httpserver.common.exception.problemJson.unauthorized.UnauthorizedException
import pt.isel.ps.g06.httpserver.common.exception.problemJson.forbidden.BaseForbiddenException
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.UserResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.submitter.SubmitterResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.InsulinProfileDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmitterDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.UserDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.input.moderation.BanInput
import pt.isel.ps.g06.httpserver.model.Submitter

@Service
class UserService(
        private val userDbRepository: UserDbRepository,
        private val submitterDbRepository: SubmitterDbRepository,
        private val insulinProfileDbRepository: InsulinProfileDbRepository,
        private val submitterMapper: SubmitterResponseMapper,
        private val userMapper: UserResponseMapper
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val userDto = userDbRepository.getByEmail(email)
                ?: throw UnauthorizedException()
        return User(email, userDto.password, emptyList())
    }

    fun registerUser(email: String, username: String, encodedPassword: String) {

        val submitter = submitterDbRepository.insertSubmitter(
                type = "User"
        )

        userDbRepository.insertUser(
                submitterId = submitter.submitter_id,
                email = email,
                username = username,
                password = encodedPassword
        )
    }

    fun deleteUser(userEmail: String) {

        val submitter = getUserFromEmail(userEmail)?.let(::getUserSubmitterInfo)

        // Delete all user's insulin profiles
        insulinProfileDbRepository.deleteAllBySubmitter(submitter!!.identifier)
        // Delete user account
        userDbRepository.deleteUser(userEmail)
    }

    fun updateUserBan(banInput: BanInput) =
            userDbRepository.updateUserBan(banInput.submitterId, banInput.isBanned)


    fun getUserFromEmail(email: String): pt.isel.ps.g06.httpserver.model.User? =
            userDbRepository.getByEmail(email)
                    ?.let(userMapper::mapToModel)

    fun getUserSubmitterInfo(user: pt.isel.ps.g06.httpserver.model.User): Submitter =
            submitterDbRepository
                    .getSubmitterBySubmitterId(user.identifier)
                    .let(submitterMapper::mapTo)

    fun ensureModerator(user: pt.isel.ps.g06.httpserver.model.User) {
        if (user.userRole != MOD_USER) {
            throw BaseForbiddenException()
        }
    }
}