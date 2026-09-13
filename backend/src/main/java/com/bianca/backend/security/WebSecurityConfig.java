package com.bianca.backend.security;

import com.bianca.backend.models.AppRole;
import com.bianca.backend.models.Role;
import com.bianca.backend.models.User;
import com.bianca.backend.repositories.CategoryRepository;
import com.bianca.backend.repositories.RoleRepository;
import com.bianca.backend.repositories.UserRepository;
import com.bianca.backend.security.jwt.AuthEntryPoint;
import com.bianca.backend.security.jwt.AuthTokenFilter;
import com.bianca.backend.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPoint unauthorizedHandler;


    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);

        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/ai/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/categories").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/events/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/categories/*/events").permitAll()
                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/user/**").hasAnyRole("ADMIN", "PARTICIPANT")

                                .anyRequest().authenticated());


        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers(headers -> headers.frameOptions(
                frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }
    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {

        return args -> {



            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() ->
                            roleRepository.save(new Role(AppRole.ROLE_USER)));



            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() ->
                            roleRepository.save(new Role(AppRole.ROLE_ADMIN)));




            if (!userRepository.existsByUsername("Bianca")) {

                User user = new User(
                        "Bianca",
                        "Bianca@yahoo.com",
                        passwordEncoder.encode("password")
                );

                user.setRoles(List.of(userRole));

                userRepository.save(user);
            }

            if (!userRepository.existsByUsername("admin")) {

                User admin = new User(
                        "admin",
                        "admin@example.com",
                        passwordEncoder.encode("password2")
                );

                admin.setRoles(List.of(adminRole));

                userRepository.save(admin);
            }

        };
    }


}