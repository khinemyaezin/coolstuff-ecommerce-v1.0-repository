package com.cs.jupiter.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterRegister {
	
	@Bean
	public FilterRegistrationBean<AuthFilter> filterRegisterBean(AuthFilter filter){
		FilterRegistrationBean<AuthFilter> reg = new FilterRegistrationBean<>();
		reg.setFilter(filter);
		reg.addUrlPatterns("/account/get");
		return reg;
	}
}
