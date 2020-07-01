package pt.isel.ps.g06.httpserver.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var nutriUserDetailsService: NutriUserDetailsService

    @Bean
    public fun authenticationProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(nutriUserDetailsService)
        provider.setPasswordEncoder(BCryptPasswordEncoder())

        return provider
    }

    /**
     * Configures what should need authentication.
     * Anything that is included inside 'antMatchers' can be accessed without authentication.
     * TODO - For now, all endpoints can be used, except the ones that will be created that only belong to the user context
     */
    override fun configure(http: HttpSecurity?) {
        http!!
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/restaurant",
                        "/restaurant/*",
                        "/meal",
                        "/meal/*",
                        "/cuisines"
                ).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
    }

}