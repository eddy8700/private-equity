/**
 * 
 */
package com.markit.pe.valuationengine.impl;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.markit.pe.portfoliodata.cache.CacheClient;
import com.markit.pe.portfoliodata.cache.InMemoryCacheClient;
import com.markit.pe.valuationengine.repository.ValuationEngineRepoConfig;

/**
 * @author Aditya Gupta
 *
 */
@Configuration
@Import(ValuationEngineRepoConfig.class)
@EnableConfigurationProperties(CurrencyHolidayCodeQAGMap.class)
@ComponentScan
public class ValuationEngineServiceConfig {
	
	
	   @Bean
	   public CacheClient<String, Object> cacheClient(){
		   return new InMemoryCacheClient<String, Object>();
	   }

}
