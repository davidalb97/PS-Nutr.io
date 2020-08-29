package pt.isel.ps.g06.httpserver.service

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.BEARER
import pt.isel.ps.g06.httpserver.common.exception.problemJson.unauthorized.UnauthorizedException
import pt.isel.ps.g06.httpserver.security.JwtUtil

@Service
class AuthenticationService(
        private val authenticationManager: AuthenticationManager,
        private val bCryptPasswordEncoder: BCryptPasswordEncoder,
        private val jwtUtil: JwtUtil
) {

    /**
     * Logins the user generating the token if successful
     * @param email - The user's email
     * @param password - The user's password
     */
    fun login(email: String, password: String): String {
        val userDetails = User(email, password, emptyList())
        try {
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(email, password)
            )
        } catch (e: AuthenticationException) {
            throw UnauthorizedException()
        }

        return jwtUtil.generateToken(userDetails)
    }

    /**
     * Extracts the user's email from the token
     * @param jwt - the JSON Web Token
     */
    fun getEmailFromJwt(jwt: String): String {
        return jwtUtil.getUserEmail(jwt.removePrefix(BEARER))
    }

    /**
     * Encodes the user password using BCrypt to store inside
     * the database later.
     * @param password - the user password
     */
    fun encodePassword(password: String): String {
        return bCryptPasswordEncoder.encode(password)
    }
}