package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.common.BAN
import pt.isel.ps.g06.httpserver.common.LOGIN
import pt.isel.ps.g06.httpserver.common.REGISTER
import pt.isel.ps.g06.httpserver.common.USER
import pt.isel.ps.g06.httpserver.common.exception.problemJson.unauthorized.UnauthorizedException
import pt.isel.ps.g06.httpserver.dataAccess.input.BanInput
import pt.isel.ps.g06.httpserver.dataAccess.input.UserLoginInput
import pt.isel.ps.g06.httpserver.dataAccess.input.UserRegisterInput
import pt.isel.ps.g06.httpserver.dataAccess.output.security.UserLoginOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.security.UserRegisterOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.user.UserInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.user.mapUserToOutput
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.AuthenticationService
import pt.isel.ps.g06.httpserver.service.UserService
import javax.validation.Valid

@RestController
class UserController(private val userService: UserService, private val authenticationService: AuthenticationService) {

    @PostMapping(REGISTER)
    fun register(@Valid @RequestBody userRegisterInput: UserRegisterInput): ResponseEntity<UserRegisterOutput> {

        userService.registerUser(
                userRegisterInput.email,
                userRegisterInput.username,
                authenticationService.encodePassword(userRegisterInput.password)
        )
        val jwt = authenticationService.login(userRegisterInput.email, userRegisterInput.password)

        return ResponseEntity.ok(UserRegisterOutput(jwt))
    }

    @PostMapping(LOGIN)
    fun login(@Valid @RequestBody userLoginInput: UserLoginInput): ResponseEntity<UserLoginOutput> {
        val user = userService.getUserFromEmail(userLoginInput.email)
                ?: throw UnauthorizedException()

        if (user.isUserBanned) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        val jwt = authenticationService.login(userLoginInput.email, userLoginInput.password)

        return ResponseEntity.ok(UserLoginOutput(jwt))
    }

    @GetMapping(USER)
    fun getUserAdditionalInfo(user: User): ResponseEntity<UserInfoOutput> =
            ResponseEntity.ok(
                    userService
                            .getUserSubmitterInfo(user)
                            .let { submitter -> mapUserToOutput(user.userEmail, submitter) }
            )


    @PutMapping(BAN)
    fun putBanUser(
            user: User,
            @RequestBody banInput: BanInput
    ): ResponseEntity<Void> {

        // Check if the user is a moderator
        userService.ensureModerator(user)

        userService.updateUserBan(banInput)

        return ResponseEntity.ok().build()
    }
}