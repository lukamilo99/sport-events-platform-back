package sport.app.sport_connecting_people.security.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowCredentials(true)
                .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("Authorization", "Cache-Control", "Content-Type")
                .exposedHeaders("Location")
                .maxAge(3600);
    }
}
