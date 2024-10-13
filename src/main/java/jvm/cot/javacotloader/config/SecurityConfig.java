package jvm.cot.javacotloader.config;

import jvm.cot.javacotloader.servlets.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Order(0)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/cot/**", "/printProps/**", "/swagger-ui/**", "/v3/**")
                        .permitAll());

        return http.build();
    }

    @Order(1)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .httpBasic(Customizer.withDefaults())
//                .sessionManagement(httpSecuritySessionManagementConfigurer ->
//                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/update/**").authenticated());
        http.addFilterBefore(new AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
