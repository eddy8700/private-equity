package com.markit.pe.comparabledata.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



/**
 * @author Aditya Gupta
 *
 */

@EnableJpaRepositories
@EnableJpaAuditing
@Configuration
@EntityScan(basePackages = {"com.markit.pe.comparabledata.domain", "com.markit.pe.portfoliodata"})
@ComponentScan
public interface ComparableDataRepoConfig {

}
