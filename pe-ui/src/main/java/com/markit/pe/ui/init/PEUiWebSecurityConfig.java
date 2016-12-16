package com.markit.pe.ui.init;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.markit.pe.commons.security.config.JsonWebTokenSecurityConfig;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = "com.markit.pe.commons.security")
public class PEUiWebSecurityConfig extends JsonWebTokenSecurityConfig {

	@Override
	protected void setupAuthorization(HttpSecurity http) throws Exception {
		http.authorizeRequests()

				// allow anonymous access to /validateTicket endpoint
				.antMatchers("/validateTicket").permitAll()
				.antMatchers("/landing").permitAll()
				// allow anonymous to common static resources
				.antMatchers(HttpMethod.GET, "/", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js")
				.permitAll()
				// authenticate all other requests
				;
	}
}
