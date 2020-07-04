package pt.isel.ps.g06.httpserver.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.security.JwtUtil
import pt.isel.ps.g06.httpserver.security.MyUserDetailsService
import pt.isel.ps.g06.httpserver.security.dto.UserLoginRequest
import pt.isel.ps.g06.httpserver.security.dto.UserLoginResponse
import pt.isel.ps.g06.httpserver.security.dto.UserRegisterRequest


@RestController
class AuthenticationController(
        @Autowired private val authenticationManager: AuthenticationManager,
        @Autowired private val userDetails: MyUserDetailsService,
        @Autowired private val jwtUtil: JwtUtil
) {
    @PostMapping("/register")
    fun register(@RequestBody userLoginRequest: UserRegisterRequest): ResponseEntity<*> {
        TODO("check if the user exists then insert")
    }

    @PostMapping("/login")
    fun createAuthenticationToken(@RequestBody userLoginRequest: UserLoginRequest): ResponseEntity<*> {
        try {
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(userLoginRequest.username, userLoginRequest.password)
            )
        } catch (e: Exception) {
            return ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        val userDetails: UserDetails = userDetails.loadUserByUsername(userLoginRequest.username)

        val jwt = jwtUtil.generateToken(userDetails)

        return ResponseEntity.ok(UserLoginResponse(jwt))
    }
}