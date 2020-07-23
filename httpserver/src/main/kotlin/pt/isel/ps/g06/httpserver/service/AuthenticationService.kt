package pt.isel.ps.g06.httpserver.service

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.BEARER
import pt.isel.ps.g06.httpserver.common.exception.authentication.NotAuthenticatedException
import pt.isel.ps.g06.httpserver.security.JwtUtil

@Service
class AuthenticationService(
        private val authenticationManager: AuthenticationManager,
        private val bCryptPasswordEncoder: BCryptPasswordEncoder,
        private val jwtUtil: JwtUtil
) {

    fun login(userName: String, password: String): String {
        val userDetails = User(userName, password, emptyList())
        try {
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(userName, password)
            )
        } catch (e: AuthenticationException) {
            throw NotAuthenticatedException()
        }

        return jwtUtil.generateToken(userDetails)
    }

    // Extract username from sent token using the server secret
    fun getUsernameByJwt(jwt: String): String {
        return jwtUtil.getUsername(jwt.removePrefix(BEARER))
    }

    fun encodePassword(password: String): String {
        return bCryptPasswordEncoder.encode(password)
    }
}