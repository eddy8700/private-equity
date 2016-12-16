package com.markit.pe.security.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.markit.pe.security.impl.SecurityServiceConfig;
/**
 * 
 * @author aditya.gupta2
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(value="com.markit.pe.security")
@Import(value={SecurityServiceConfig.class})
public class PeSecurityServiceApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(PeSecurityServiceApplication.class, args);
	}
	
	
	
  
}
