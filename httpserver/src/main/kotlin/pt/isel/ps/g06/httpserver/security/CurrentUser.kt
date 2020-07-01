package pt.isel.ps.g06.httpserver.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class CurrentUser(val user: User): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
            Collections.singleton(SimpleGrantedAuthority("USER"))

    override fun isEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getUsername(): String = user.username

    override fun isCredentialsNonExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPassword(): String = user.password

    override fun isAccountNonExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isAccountNonLocked(): Boolean {
        TODO("Not yet implemented")
    }
}