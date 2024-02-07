package com.example.courseWork.config;


import com.example.courseWork.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }


   /* protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //Настраивает аутентификацию.
        auth.userDetailsService(personDetailsService);
    }*/

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(personDetailsService);
        authProvider.setPasswordEncoder(getPasswordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Конфигурируем сам Spring Security
        //Конфигурируем саму авторизацию.
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/auth/login","/auth/registration","/error")
                                .permitAll()
                                .anyRequest().hasAnyRole("USER","ADMIN")
                )
                .formLogin(
                        form -> form
                                .loginPage("/auth/login")
                                .loginProcessingUrl("/process_login")
                                .defaultSuccessUrl("/hello",true)
                                .failureUrl("/auth/login?error")
                                .permitAll()
                ).logout(
                        logout ->logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login"));
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
