package it.sevenbits.backend.taskmanager.config;

import it.sevenbits.backend.taskmanager.web.security.provider.JwtAuthenticationProvider;
import it.sevenbits.backend.taskmanager.web.security.filter.HeaderJwtAuthFilter;
import it.sevenbits.backend.taskmanager.web.security.filter.JwtAuthFilter;
import it.sevenbits.backend.taskmanager.web.service.tokens.JwtTokenService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Configuration for security settings
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenService jwtTokenService;

    /**
     * Create security config
     *
     * @param jwtTokenService service that works with tokens
     */
    public WebSecurityConfig(final JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * Get encoder
     *
     * @return encoder
     */
    @Bean
    @Qualifier(value = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.formLogin().disable(); // disable default login form

        http.logout().disable();
        http.sessionManagement().disable(); // disable session saving
        http.requestCache().disable();
        http.anonymous(); // enable role ANONYMOUS

        RequestMatcher signInPageMatcher = new AntPathRequestMatcher("/signin");
        RequestMatcher notSignInPageMatcher = new NegatedRequestMatcher(signInPageMatcher);
        // enable token filter everywhere, except /signin request

        JwtAuthFilter authFilter = new HeaderJwtAuthFilter(notSignInPageMatcher);
        http.addFilterBefore(authFilter, FilterSecurityInterceptor.class);

        http
                .authorizeRequests().antMatchers("/signin").permitAll()
                .and()
                .authorizeRequests().antMatchers("/tasks/**").hasAuthority("USER")
                .and()
                .authorizeRequests().antMatchers("/users/**").hasAuthority("ADMIN")
                .and()
                .authorizeRequests().antMatchers("/whoami").hasAuthority("USER")
                .and()
                .authorizeRequests().anyRequest().authenticated();
    }

    /**
     * Configure custom authentication provider
     *
     * @param auth builder of a main authentication strategy
     */
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new JwtAuthenticationProvider(jwtTokenService));
    }
}
