package pt.isel.ps.g06.httpserver.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.JWT_EXPIRATION
import pt.isel.ps.g06.httpserver.springConfig.dto.ServerConfigDto
import java.security.Key
import java.util.*
import java.util.function.Function
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.HashMap

/***
 * The JwtUtil generates and validates tokens.
 */
@Service
class JwtUtil(val serverConfigDto: ServerConfigDto) {

    /**
     * Gets the username from the jwt.
     * @param token - the JSON Web Token
     */
    fun getUsername(token: String): String =
            extractClaim<String>(token, Function { obj: Claims -> obj.subject })

    /**
     * Gets the token's expiration time from the jwt.
     * @param token - the JSON Web Token
     */
    fun getExpirationTime(token: String): Date =
            extractClaim<Date>(token, Function { obj: Claims -> obj.expiration })

    /**
     * Extract a specific claim held by the token.
     * @param token - the JSON Web Token
     * @param claimsResolver - the claim to be extracted from the token
     */
    fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    /**
     * Extract all the claims from the token.
     * @param token - the JSON Web Token
     */
    private fun extractAllClaims(token: String): Claims =
            Jwts.parserBuilder()
                    .setSigningKey(getServerSecretKeySpec())
                    .build()
                    .parseClaimsJws(token)
                    .body

    /**
     * Checks if the token has already expired.
     * @param token - the JSON Web Token
     */
    private fun isTokenExpired(token: String): Boolean = getExpirationTime(token).before(Date())

    /**
     * Starts generating the token, providing the username.
     * @param userDetails - provides the core user information, including its credentials
     */
    fun generateToken(userDetails: UserDetails): String {
        val claims = HashMap<String, Any>()
        return createToken(claims, userDetails.username)
    }

    /**
     * Creates the token using the server secret.
     * @param claims - the token claims' map
     * @param subject - the subject to be held by the token
     */
    private fun createToken(claims: Map<String, Any>, subject: String): String {

        val now = System.currentTimeMillis()

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date(now))
                .setExpiration(Date(now + JWT_EXPIRATION))
                .signWith(getServerSecretKeySpec())
                .compact()
    }

    /**
     * Checks if the user belongs to the user and has not expired.
     * @param token - the JSON Web Token
     * @param userDetails - provides the core user information, including its credentials
     */
    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsername(token)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    /**
     * Converts the secret plain text from the environment variables and converts to a java.security.Key
     */
    private fun getServerSecretKeySpec(): Key {
        val serverSecret = serverConfigDto.secret ?: throw Exception("The server could not generate the token")

        val secretBytes = serverSecret.toByteArray()
        return SecretKeySpec(
                secretBytes,
                Keys.secretKeyFor(SignatureAlgorithm.HS256).algorithm
        )
    }
}