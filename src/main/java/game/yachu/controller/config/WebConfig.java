package game.yachu.controller.config;

import game.yachu.controller.interceptor.SessionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/api/record/**", "/api/new",
                        "/css/**", "/js/**", "/images/**", "/*.html", "/*.ico", "/error");
    }
}
