/**
 * 
 */
package com.markit.pe.security.impl;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.markit.pe.security.repository.SecurityDataRepoConfig;

/**
 * @author Aditya Gupta
 *
 */
@Configuration
@Import(value={SecurityDataRepoConfig.class,AuthenticationServerWebSecurityConfig.class})
@ComponentScan
public class SecurityServiceConfig {
	
	
}
