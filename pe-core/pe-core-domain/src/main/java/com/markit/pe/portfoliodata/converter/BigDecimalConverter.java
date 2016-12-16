package com.markit.pe.portfoliodata.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.AttributeConverter;

public class BigDecimalConverter implements AttributeConverter<BigDecimal, BigDecimal> {

	@Override
	public BigDecimal convertToDatabaseColumn(BigDecimal attribute) {
		if (attribute != null && attribute instanceof BigDecimal) {
			return ((BigDecimal) attribute).setScale(5, RoundingMode.HALF_DOWN);
		}
		return null;
	}

	@Override
	public BigDecimal convertToEntityAttribute(BigDecimal dbData) {
		if (dbData != null) {
			return dbData.setScale(5, RoundingMode.HALF_DOWN);
		}
		return dbData;
	}
}
