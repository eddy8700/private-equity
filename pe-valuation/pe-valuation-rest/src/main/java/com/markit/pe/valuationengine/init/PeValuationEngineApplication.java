package com.markit.pe.valuationengine.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import com.markit.pe.valuationengine.domain.PEExceptionProducer;
import com.markit.pe.valuationengine.impl.ValuationEngineServiceConfig;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(value="com.markit.pe.valuationengine")
@RefreshScope
@Import(ValuationEngineServiceConfig.class)
@EnableAsync
@EnableTransactionManagement
@EnableBinding(PEExceptionProducer.class)
public class PeValuationEngineApplication {
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;

	}
	private static final Logger logger = LoggerFactory.getLogger(PeValuationEngineApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Spring Boot Application {}: ", PeValuationEngineApplication.class.getSimpleName());
		SpringApplication.run(PeValuationEngineApplication.class, args);
	}

	
}
