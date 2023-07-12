package sport.app.sport_connecting_people.security.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sport.app.sport_connecting_people.security.filter.JwtAuthenticationFilter;
import sport.app.sport_connecting_people.security.handler.OAuth2AuthenticationFailureHandler;
import sport.app.sport_connecting_people.security.handler.OAuth2AuthenticationSuccessHandler;
import sport.app.sport_connecting_people.security.jwt.JwtUtil;
import sport.app.sport_connecting_people.security.service.CustomOAuth2UserService;
import sport.app.sport_connecting_people.security.service.CustomUserDetailsService;

@AllArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private JwtUtil jwtUtil;
    private CustomUserDetailsService userDetailsService;
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http
                            .cors()
                        .and()
                            .csrf()
                                .disable()
                            .sessionManagement()
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                            .authorizeHttpRequests()
                                .anyRequest()
                                    .permitAll()
                        .and()
                            .oauth2Login()
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                            .userInfoEndpoint()
                                .userService(customOAuth2UserService);
            http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

            return http.build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
