package edu.sjsu.cmpe275.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class StaticResourceConfigurer extends WebMvcConfigurerAdapter {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/images/**").addResourceLocations("file:///Users/Michaelzhd/images/");
		registry.addResourceHandler("/images/**").addResourceLocations("file:///home/ec2-user/images/");
	}

}
