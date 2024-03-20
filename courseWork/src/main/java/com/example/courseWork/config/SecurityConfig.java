package com.example.courseWork.config;


import com.example.courseWork.services.authServices.PersonDetailsService;
import com.example.courseWork.services.props.MinioProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.MinioClient;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;

    private final MinioProperties minioProperties;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, MinioProperties minioProperties) {
        this.personDetailsService = personDetailsService;
        this.minioProperties = minioProperties;
    }
    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().
                addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(
                        new Components()
                            .addSecuritySchemes("bearerAuth",
                                    new SecurityScheme()
                                            .type(SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT")
                            )
                )
                .info(new Info()
                        .title("GaveSaves")
                        .description("Demo spring boot app")
                        .version("1.0")
                );
    }


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
        return http.csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/auth/login","/error","/auth/recover-password","/auth/change-password").permitAll()
                                .requestMatchers(HttpMethod.POST,"/auth/registration").permitAll()
                                .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                                .requestMatchers("/auth/me").authenticated()
                                .requestMatchers(HttpMethod.GET, "/games").hasAnyRole("USER","ADMIN")
                                .requestMatchers(HttpMethod.GET, "/games/{id}").hasAnyRole("USER","ADMIN")
                                .requestMatchers(HttpMethod.GET, "/games/image/{id}").hasAnyRole("USER","ADMIN")
                                .requestMatchers("/games/**").hasRole("ADMIN")
                                .anyRequest().hasAnyRole("USER","ADMIN")
                ).
                logout(logout->logout.logoutUrl("/auth/logout").
                        logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))))
                        .exceptionHandling(customizer -> customizer
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                        .build();
        /*.formLogin(
                        form -> form
                                .loginPage("/auth/login")
                                .loginProcessingUrl("/process_login")
                                .defaultSuccessUrl("/hello",true)
                                .failureUrl("/auth/login?error")
                                .permitAll()
                ).logout(
                        logout ->logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login"))*/

    }



    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
