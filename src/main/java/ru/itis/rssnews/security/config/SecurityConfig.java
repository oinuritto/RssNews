package ru.itis.rssnews.security.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsServiceImpl;


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/css/**", "/js/**", "/img/**", "/static/**");
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
                http
                        .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/error")
                        .and()
                        .authorizeHttpRequests()
                        .requestMatchers("/", "/error", "/rss", "/api/likes/**").permitAll()
                        .requestMatchers("/login", "/register").anonymous()
                        .requestMatchers(HttpMethod.DELETE, "/api/likes/**").authenticated()
                        .requestMatchers("/profile/**", "/logout", "/rss/add", "/api/likes/add").authenticated()
                        .requestMatchers( "/admin/**", "/rss/delete/**").hasAuthority("ADMIN")
                        .and()
                        .formLogin()
                        .loginPage("/login")
                        .defaultSuccessUrl("/").failureUrl("/login?error")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .and()
                        .logout().logoutUrl("/logout").logoutSuccessUrl("/")
                        .and().build();
    }

    @Autowired
    public void bindUserDetailsServiceAndPasswordEncoder(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder);
    }
}

