package com.markit.pe.security.impl;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.markit.pe.commons.security.config.JsonWebTokenSecurityConfig;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = "com.markit.pe.commons.security")
public class AuthenticationServerWebSecurityConfig extends JsonWebTokenSecurityConfig {

	@Override
	protected void setupAuthorization(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/validateTicket").permitAll()
				.anyRequest().authenticated();
	}
}
