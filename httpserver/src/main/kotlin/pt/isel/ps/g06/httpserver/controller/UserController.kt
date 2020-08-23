package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.authentication.NotAuthenticatedException
import pt.isel.ps.g06.httpserver.common.exception.authorization.NotAuthorizedException
import pt.isel.ps.g06.httpserver.dataAccess.input.BanInput
import pt.isel.ps.g06.httpserver.dataAccess.input.UserInfoInput
import pt.isel.ps.g06.httpserver.dataAccess.input.UserLoginInput
import pt.isel.ps.g06.httpserver.dataAccess.input.UserRegisterInput
import pt.isel.ps.g06.httpserver.dataAccess.output.user.UserInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.security.UserLoginOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.security.UserRegisterOutput
import pt.isel.ps.g06.httpserver.model.Submitter
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

        val jwt = authenticationService.login(userLoginInput.email, userLoginInput.password)

        return ResponseEntity.ok(UserLoginOutput(jwt))
    }

    @GetMapping(USER)
    fun getUserInfo(submitter: Submitter?): ResponseEntity<UserInfoOutput> {

        submitter ?: throw NotAuthenticatedException()

        val email = userService.getEmailFromSubmitter(submitter.identifier).userEmail
        val username = submitter.name
        val image = submitter.image

        return ResponseEntity.ok(UserInfoOutput(email, username, image))
    }

    @PutMapping(BAN)
    fun putBanUser(
            @RequestHeader(AUTH_HEADER) jwt: String,
            @RequestBody banInput: BanInput
    ): ResponseEntity<Void> {

        val requester = authenticationService.getEmailFromJwt(jwt).let(userService::getUserFromEmail)

        if (requester.userRole != MOD_USER) {
            throw NotAuthorizedException()
        }

        userService.updateUserBan(banInput)

        return ResponseEntity.ok().build()
    }
}