package pt.isel.ps.g06.httpserver.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.JWT_EXPIRATION
import pt.isel.ps.g06.httpserver.springConfig.ServerSecret
import java.util.*
import java.util.function.Function
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter
import kotlin.collections.HashMap

/***
 * The JwtUtil generates and validates tokens.
 */
@Service
class JwtUtil(val serverSecret: ServerSecret) {

    /**
     * Gets the user's email from the jwt.
     * @param token - the JSON Web Token
     */
    fun getUserEmail(token: String): String =
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
                    .setSigningKey(getServerSecretBytes())
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

        val secret = SecretKeySpec(
                getServerSecretBytes(),
                SignatureAlgorithm.HS256.jcaName
        )

        val now = System.currentTimeMillis()

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date(now))
                .setExpiration(Date(now + JWT_EXPIRATION))
                .signWith(secret)
                .compact()
    }

    /**
     * Checks if the user belongs to the user and has not expired.
     * @param token - the JSON Web Token
     * @param userDetails - provides the core user information, including its credentials
     */
    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val email = getUserEmail(token)
        return (email == userDetails.username) && !isTokenExpired(token)
    }

    /**
     * Converts the server secret environment variable value into an array of bytes.
     */
    private fun getServerSecretBytes(): ByteArray =
            DatatypeConverter.parseBase64Binary(serverSecret.secret)
}