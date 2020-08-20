package pt.isel.ps.g06.httpserver.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import pt.isel.ps.g06.httpserver.service.UserService

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
        private val userService: UserService,
        private val jwtFilter: JwtFilter
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(userService)
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }

    /**
     * Setup the service
     */
    @Autowired
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService)
    }

    /**
     * Configures what should need authentication.
     * Anything that is included inside 'antMatchers' can be accessed without authentication.
     */
    override fun configure(http: HttpSecurity?) {
        http!!
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,
                        "/",
                        "/restaurant",
                        "/restaurant/**",
                        "/meal",
                        "/meal/*",
                        "/cuisines",
                        "/ingredients"
                ).permitAll()
                .antMatchers(HttpMethod.POST,
                        "/user/login",
                        "/user/register"
                ).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()
}