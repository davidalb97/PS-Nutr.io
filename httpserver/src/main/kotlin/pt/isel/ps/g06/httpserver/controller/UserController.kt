package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.common.LOGIN
import pt.isel.ps.g06.httpserver.common.REGISTER
import pt.isel.ps.g06.httpserver.dataAccess.input.UserLoginInput
import pt.isel.ps.g06.httpserver.dataAccess.input.UserRegisterInput
import pt.isel.ps.g06.httpserver.dataAccess.output.security.UserLoginOutput
import pt.isel.ps.g06.httpserver.service.AuthenticationService
import pt.isel.ps.g06.httpserver.service.UserService

@RestController
class UserController(private val userService: UserService, private val authenticationService: AuthenticationService) {

    @PostMapping(REGISTER)
    fun register(@RequestBody userRegisterInput: UserRegisterInput): ResponseEntity<*> {

        userService.registerUser(
                userRegisterInput.email,
                userRegisterInput.username,
                authenticationService.encodePassword(userRegisterInput.password)
        )
        //TODO pass created uri!
        return ResponseEntity("User registered", HttpStatus.CREATED)
    }

    @PostMapping(LOGIN)
    fun login(@RequestBody userLoginInput: UserLoginInput): ResponseEntity<*> {

        val jwt = authenticationService.login(userLoginInput.username, userLoginInput.password)

        return ResponseEntity.ok(UserLoginOutput(jwt))
    }
}