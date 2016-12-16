package com.markit.pe.portfoliodata.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.markit.pe.portfoliodata.Currency;

@Converter(autoApply=true)
public class CurrencyConverter implements AttributeConverter<Currency, String>{

	@Override
	public String convertToDatabaseColumn(Currency attribute) {
		// TODO Auto-generated method stub
		return attribute.toString();
	}

	@Override
	public Currency convertToEntityAttribute(String dbData) {
		return Currency.valueOf(dbData);
	}

}
