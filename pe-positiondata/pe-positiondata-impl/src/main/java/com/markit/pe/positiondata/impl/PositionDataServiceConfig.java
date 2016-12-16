/**
 * 
 */
package com.markit.pe.positiondata.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.markit.pe.portfoliodata.cache.CacheClient;
import com.markit.pe.portfoliodata.cache.InMemoryCacheClient;
import com.markit.pe.positiondata.configuration.AsyncConfiguration;
import com.markit.pe.positiondata.configuration.PositionDataWebSecurityConfig;
import com.markit.pe.positiondata.configuration.ValidationConfig;
import com.markit.pe.positiondata.repository.PositionDataRepoConfig;

/**
 * @author Aditya Gupta
 *
 */
@Configuration
@Import(value={AsyncConfiguration.class,ValidationConfig.class,PositionDataRepoConfig.class,PositionDataWebSecurityConfig.class})
@ComponentScan
public class PositionDataServiceConfig {
	
	
	   @Bean
	   public CacheClient<String, Object> cacheClient(){
		   return new InMemoryCacheClient<String, Object>();
	   }

}
