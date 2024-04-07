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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import javax.sql.DataSource;

import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.COOKIES;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;
    private final MinioProperties minioProperties;
    private final DataSource dataSource;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, MinioProperties minioProperties, DataSource dataSource) {
        this.personDetailsService = personDetailsService;
        this.minioProperties = minioProperties;
        this.dataSource = dataSource;
    }
    @Bean
    public RememberMeServices rememberMeServices() {
        return new PersistentTokenBasedRememberMeServices("secretKey", personDetailsService, persistentTokenRepository());
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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
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
                                .requestMatchers(HttpMethod.DELETE, "/users/{id}/block").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/game-saves").hasAnyRole("USER","ADMIN")
                                .requestMatchers("/games/**").hasRole("ADMIN")
                                .anyRequest().hasAnyRole("USER","ADMIN")
                )
                .rememberMe(rememberMe -> rememberMe
                        .tokenRepository(persistentTokenRepository())
                        .userDetailsService(personDetailsService)
                        .tokenValiditySeconds(60000)
                        /*.alwaysRemember(true)
                        .rememberMeServices(rememberMeServices())*/
                        .key("secretKey")
                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredUrl("/auth/logout")

                ).logout(logout->logout.logoutUrl("/auth/logout")
                        .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(COOKIES)))
                        .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))))
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
                )*//*.logout(
                        logout ->logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login"))*/

    }
    /*@Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/coursework?currentSchema=cloud_game_saves");
        dataSource.setUsername("postgres");
        dataSource.setPassword("Airbender1");
        return dataSource;
    }*/

    /*@Bean
    public PersistentTokenRepository persistentTokenRepository() {
        InMemoryTokenRepositoryImpl memory = new InMemoryTokenRepositoryImpl();
        return memory;
    }*/
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
