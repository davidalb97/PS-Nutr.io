package pt.isel.ps.g06.httpserver.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.common.LOGIN
import pt.isel.ps.g06.httpserver.common.REGISTER
import pt.isel.ps.g06.httpserver.security.JwtUtil
import pt.isel.ps.g06.httpserver.security.MyUserDetailsService
import pt.isel.ps.g06.httpserver.security.dto.UserLoginRequest
import pt.isel.ps.g06.httpserver.security.dto.UserLoginResponse
import pt.isel.ps.g06.httpserver.security.dto.UserRegisterRequest

@RestController
class UserController(
        @Autowired private val authenticationManager: AuthenticationManager,
        @Autowired private val userDetailsService: MyUserDetailsService,
        @Autowired private val jwtUtil: JwtUtil,
        @Autowired private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {
    @PostMapping(REGISTER)
    fun register(@RequestBody userRegisterRequest: UserRegisterRequest): ResponseEntity<*> {
        //val userFound: UserDetails? = userDetails.loadUserByUsername(userRegisterRequest.username)

        val encodedPassword = bCryptPasswordEncoder.encode(userRegisterRequest.password);

        userDetailsService.registerUser(
                userRegisterRequest.email,
                userRegisterRequest.username,
                encodedPassword
        )
        return ResponseEntity("User registered", HttpStatus.CREATED);

    }

    @PostMapping(LOGIN)
    fun login(@RequestBody userLoginRequest: UserLoginRequest): ResponseEntity<*> {
        try {
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(userLoginRequest.username, userLoginRequest.password)
            )
        } catch (e: Exception) {
            return ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(userLoginRequest.username)

        val submitterId = userDetailsService.getSubmitterByUsername(userLoginRequest.username).submitter_id

        val jwt = jwtUtil.generateToken(userDetails)

        return ResponseEntity.ok(UserLoginResponse(jwt, submitterId))
    }
}