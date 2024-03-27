package com.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://127.0.0.1:9000")
                .allowedMethods("POST", "OPTIONS", "GET", "DELETE", "PUT")
                .allowedHeaders("X-Requested-With,Origin,Content-Type,Accept,Authorization")
                .allowCredentials(true).maxAge(3600);
    }
////	@Profile("dev")	
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
////		registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/");
//		registry.addResourceHandler("/api/static/**").addResourceLocations("classpath:/static/");
//	}
//
//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//		registry.addViewController("/").setViewName("/home.html");
//	}
}
