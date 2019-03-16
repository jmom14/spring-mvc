package com.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{

//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		// TODO Auto-generated method stub
//		super.addResourceHandlers(registry);
//		
//		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
//				
//		
//		registry.addResourceHandler("/uploads/**")
//		.addResourceLocations(resourcePath);
//		
//		//.addResourceLocations("file:C:/shared/");
//	}
	
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/error_403").setViewName("error_403");
	}

}