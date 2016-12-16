package com.markit.pe.security.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
/**
 * 
 * @author aditya.gupta2
 *
 */
@EnableJpaRepositories
@EnableJpaAuditing
@Configuration
@ComponentScan
@EntityScan(basePackages = {"com.markit.pe.commons.security.domain"})
public interface SecurityDataRepoConfig {

}
