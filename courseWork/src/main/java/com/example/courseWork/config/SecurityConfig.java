package com.example.courseWork.config;


import com.example.courseWork.services.authServices.PersonDetailsService;
import com.example.courseWork.services.props.MinioProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.COOKIES;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableJdbcHttpSession
@EnableScheduling
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;
    private final MinioProperties minioProperties;
    private final JdbcIndexedSessionRepository sessionRepository;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, MinioProperties minioProperties, JdbcIndexedSessionRepository sessionRepository) {
        this.personDetailsService = personDetailsService;
        this.minioProperties = minioProperties;
        this.sessionRepository = sessionRepository;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(personDetailsService);
        authProvider.setPasswordEncoder(getPasswordEncoder());

        return authProvider;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(customizer -> customizer.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/login","/error","/auth/recover-password","/auth/change-password").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/registration").permitAll()
                        .requestMatchers("/auth/redirect").permitAll()
                        .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                        .requestMatchers("/auth/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/games").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.GET, "/games/{id}").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.GET, "/games/image/{id}").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}/block").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/game-saves").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/games/**").hasRole("ADMIN")
                        .anyRequest().hasAnyRole("USER","ADMIN")
                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredUrl("/auth/logout")
                        .sessionRegistry(sessionRegistry())
                ).logout(logout -> logout.logoutUrl("/auth/logout")
                        .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(COOKIES)))
                        .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
                )
                .exceptionHandling(customizer -> customizer
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
         return ((request) -> {
             CorsConfiguration corsConfig = new CorsConfiguration();
             corsConfig.setAllowCredentials(true);
             corsConfig.setAllowedOrigins(List.of("http://localhost:5173/"));
             corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); //to set allowed http methods
             corsConfig.setAllowedHeaders(Arrays.asList("Accept", "Content-Type", "Authorization", "Cache-Control", "Content-Type"));
             UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
             source.registerCorsConfiguration("/**", corsConfig);
             return corsConfig;
        });
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
