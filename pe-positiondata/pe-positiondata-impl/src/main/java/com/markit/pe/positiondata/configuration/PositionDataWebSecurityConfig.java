package com.markit.pe.positiondata.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import com.markit.pe.commons.security.config.JsonWebTokenSecurityConfig;
import com.markit.pe.positiondata.filter.SimpleCORSFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = "com.markit.pe.commons.security")
public class PositionDataWebSecurityConfig extends JsonWebTokenSecurityConfig {

	@Override
	protected void setupAuthorization(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				// allow anonymous access to /authenticate endpoint
				.antMatchers("/validateTicket").permitAll()
				// authenticate all other requests
				.anyRequest().permitAll()
				;
	}

	
}
