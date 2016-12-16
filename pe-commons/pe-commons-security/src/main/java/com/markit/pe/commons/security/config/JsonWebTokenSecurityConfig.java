package com.markit.pe.commons.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import com.markit.pe.commons.security.authentication.JsonWebTokenAuthenticationProvider;
import com.markit.pe.commons.security.filter.JsonWebTokenAuthenticationFilter;
/**
 * 
 * @author aditya.gupta2
 *
 */
public abstract class JsonWebTokenSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JsonWebTokenAuthenticationProvider authenticationProvider;

	@Autowired
	private JsonWebTokenAuthenticationFilter jsonWebTokenFilter;
	

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				// disable CSRF, http basic, form login
		     
				.csrf().disable() //
				.httpBasic().disable() //
				.formLogin().disable()
				// ReST is stateless, no sessions
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //
				.and()
				// return 403 when not authenticated
				.exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint());

		// Let implementing classes set up authorization paths
		setupAuthorization(http);
		http.addFilterBefore(jsonWebTokenFilter, AbstractPreAuthenticatedProcessingFilter.class);
	}

	protected abstract void setupAuthorization(HttpSecurity http) throws Exception;
}
