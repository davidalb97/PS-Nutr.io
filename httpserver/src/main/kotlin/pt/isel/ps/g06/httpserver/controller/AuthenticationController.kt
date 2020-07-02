package pt.isel.ps.g06.httpserver.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.security.JwtUtil
import pt.isel.ps.g06.httpserver.security.MyUserDetailsService
import pt.isel.ps.g06.httpserver.security.UserAuthRequest
import pt.isel.ps.g06.httpserver.security.UserAuthResponse


@RestController
class AuthenticationController(
        @Autowired private val authenticationManager: AuthenticationManager,
        @Autowired private val userDetails: MyUserDetailsService,
        @Autowired private val jwtUtil: JwtUtil
) {
    @PostMapping("/authenticate")
    fun createAuthenticationToken(@RequestBody userAuthRequest: UserAuthRequest): ResponseEntity<*>{
        try {
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(userAuthRequest.username, userAuthRequest.password)
            )
        } catch (e: Exception) {
            print(e.message)
        }

        val userDetails: UserDetails = userDetails.loadUserByUsername(userAuthRequest.username)

        val jwt = jwtUtil.generateToken(userDetails)

        return ResponseEntity.ok(UserAuthResponse(jwt))
    }
}