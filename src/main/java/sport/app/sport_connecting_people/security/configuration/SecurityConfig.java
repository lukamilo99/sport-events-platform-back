package sport.app.sport_connecting_people.security.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sport.app.sport_connecting_people.security.filter.JwtAuthenticationFilter;
import sport.app.sport_connecting_people.security.handler.JwtAuthenticationEntryPoint;
import sport.app.sport_connecting_people.security.handler.OAuth2AuthenticationFailureHandler;
import sport.app.sport_connecting_people.security.handler.OAuth2AuthenticationSuccessHandler;
import sport.app.sport_connecting_people.security.repository.OAuth2AuthorizationRequestRepository;
import sport.app.sport_connecting_people.security.service.CustomOAuth2UserService;

@AllArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private OAuth2AuthorizationRequestRepository oAuth2authorizationRequestRepository;
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http
                    .cors().and()
                    .csrf().disable()
                    .httpBasic().disable()
                    .formLogin().disable()
                    .authorizeHttpRequests()
                    .requestMatchers(
                            "/auth/**",
                            "/oauth2/**",
                            "/event/latest",
                            "/event/search-events",
                            "/geo/address")
                    .permitAll()
                    .requestMatchers(
                            "/user/ban/**",
                            "/user/unban/**",
                            "/user/delete/**",
                            "/user/search-users")
                    .hasAuthority("ADMIN")
                    .anyRequest().authenticated()
                    .and()
                    .oauth2Login()
                    .authorizationEndpoint().baseUri("/oauth2/authorize")
                    .authorizationRequestRepository(oAuth2authorizationRequestRepository)
                    .and()
                    .redirectionEndpoint().baseUri("/oauth2/callback/*")
                    .and()
                    .userInfoEndpoint().userService(customOAuth2UserService)
                    .and()
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler)
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint);
            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
