package pt.isel.ps.g06.httpserver.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function
import kotlin.collections.HashMap

@Suppress("DEPRECATION")
@Service
class JwtUtil {

    private val secret = "nutrioprojectsecret2020socorrosocorrosocorrosocorrosocorrosocorrosocorro"

    fun getUsername(token: String): String =
            extractClaim<String>(token, Function { obj: Claims -> obj.subject })

    fun getExpirationTime(token: String): Date =
            extractClaim<Date>(token, Function { obj: Claims -> obj.expiration })

    fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(token: String): Claims =
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body

    private fun isTokenExpired(token: String): Boolean = getExpirationTime(token).before(Date())

    fun generateToken(userDetails: UserDetails): String {
        val claims = HashMap<String, Any>()
        return createToken(claims, userDetails.username)
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String =
            Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(Date(System.currentTimeMillis()))
                    .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact()

    fun validadeToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsername(token)
        return (username == userDetails.username) && !isTokenExpired(token)
    }
}