package mg.bmoi.agiosdevise.config;

import mg.bmoi.agiosdevise.repository.UserAppRepository;
import mg.bmoi.agiosdevise.service.TokenBlacklistService;
import mg.bmoi.agiosdevise.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Date;

@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUtil jwtUtil, UserAppRepository userAppRepository, TokenBlacklistService tokenBlacklistService) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/validate-token").authenticated()
                .antMatchers("/api/**").hasRole("ADMIN")
                .antMatchers("/api/pupitreur/**").hasRole("PUPITREUR")
                .antMatchers("/api/consultant/**").hasRole("CONSULTANT")
                .antMatchers("/api/testeur/**").hasRole("TESTEUR")
                .antMatchers("/api/**").authenticated()

                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    // Log détaillé
                    String authHeader = request.getHeader("Authorization");
                    String username = authHeader != null ? jwtUtil.extractUsername(authHeader.substring(7)) : "ANONYMOUS";

                    System.err.println(String.format(
                            "Accès refusé - Utilisateur: %s - URL: %s - Méthode: %s - Raison: %s",
                            username,
                            request.getRequestURI(),
                            request.getMethod(),
                            accessDeniedException.getMessage()
                    ));

                    // Réponse JSON
                    response.setContentType("application/json");
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write(String.format(
                            "{\"timestamp\": \"%s\", \"status\": 403, \"error\": \"Forbidden\", " +
                                    "\"message\": \"Vous n'avez pas les droits nécessaires pour accéder à cette ressource\", " +
                                    "\"path\": \"%s\"}",
                            new Date(),
                            request.getRequestURI()
                    ));
                })
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new TokenAuthenticationFilter(jwtUtil, userAppRepository, tokenBlacklistService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
