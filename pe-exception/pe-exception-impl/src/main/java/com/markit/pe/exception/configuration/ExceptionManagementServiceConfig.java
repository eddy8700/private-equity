/**
 * 
 */
package com.markit.pe.exception.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.markit.pe.exception.repository.ExceptionManagementRepoConfig;

/**
 * @author Aditya Gupta
 *
 */
@Configuration
@Import(value={ExceptionManagementRepoConfig.class})
@ComponentScan
@EnableConfigurationProperties
public class ExceptionManagementServiceConfig {
	
	

}
