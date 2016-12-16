package com.markit.pe.valuationengine.util;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;

import com.markit.pe.portfoliodata.Currency;

public class ApacheBeanUtil {

	
	private static final String DATE_FORMAT="dd/MM/yyyy";
	private static final Object DEFAULT_DATE_VALUE=null;
	private static final Object DEFAULT_BIGDECIMAL_VALUE=null;
	
	
	
	public static void registerConverters(){
		DateConverter converter = new DateConverter(DEFAULT_DATE_VALUE);
		converter.setPattern(DATE_FORMAT);
		Converter bdc = new BigDecimalConverter(DEFAULT_BIGDECIMAL_VALUE);
		ConvertUtils.register(converter, Date.class);	
		ConvertUtils.register(new EnumConverter(), Currency.class);
		ConvertUtils.register(bdc, BigDecimal.class);
		
	}
	
	
	
}
