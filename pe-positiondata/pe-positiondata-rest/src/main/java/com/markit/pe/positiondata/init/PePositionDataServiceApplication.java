package com.markit.pe.positiondata.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ws.config.annotation.EnableWs;

import com.markit.pe.portfoliodata.util.IDgeneratorUtil;
import com.markit.pe.positiondata.impl.PositionDataServiceConfig;
import com.markit.pe.positiondata.util.SecurityIdGeneratorUtil;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(value="com.markit.pe.positiondata")
@Import(value={PositionDataServiceConfig.class})
@EnableTransactionManagement
@EnableWs
@EnableAsync
public class PePositionDataServiceApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(PePositionDataServiceApplication.class, args);
	}
	
	
	@Bean
	public IDgeneratorUtil idGeneratorUtil(){
		final IDgeneratorUtil iDgeneratorUtil= new SecurityIdGeneratorUtil();
		return iDgeneratorUtil;
	}
	
	/*@Bean
	public CorsFilter corsFilter(){
		return new SimpleCORSFilter();
	}*/
  
}
