package com.markit.pe.comparabledata.impl;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.markit.pe.comparabledata.repository.ComparableDataRepoConfig;

/**
 * @author Aditya Gupta
 *
 */
@Configuration
@Import(value={ComparableDataRepoConfig.class})
@ComponentScan
public class ComparableDataServiceConfig {
	
	

}
