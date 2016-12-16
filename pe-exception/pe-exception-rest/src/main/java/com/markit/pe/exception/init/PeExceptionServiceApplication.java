package com.markit.pe.exception.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Import;

import com.markit.pe.exception.configuration.ExceptionManagementServiceConfig;
import com.markit.pe.exception.consumer.api.PEExceptionConsumer;

@EnableDiscoveryClient
@SpringBootApplication
@Import(ExceptionManagementServiceConfig.class)
@EnableBinding(PEExceptionConsumer.class)
@RefreshScope
public class PeExceptionServiceApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(PeExceptionServiceApplication.class, args);
		
	}
	
}
