package com.markit.pe.valuationengine.mongo;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


/**
 * @author Aditya Gupta
 *
 */
@EnableMongoRepositories
@EnableMongoAuditing
@Configuration
@ComponentScan
@EntityScan(basePackages = {"com.markit.pe.valuationengine","com.markit.pe.positiondata", "com.markit.pe.portfoliodata"})
public interface ValuationEngineRepoConfig {

}
