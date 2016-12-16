/**
 * 
 */
package com.markit.pe.valuationengine.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author mahesh.agarwal
 *
 */
@Component
@ConfigurationProperties(locations="classpath:currencyHolidayCodeQAGMap.properties")
public class CurrencyHolidayCodeQAGMap {
	
	private final Map<String, String> namespace = new HashMap<>();

    public Map<String, String> getNamespace() {
        return namespace;
    }
}
