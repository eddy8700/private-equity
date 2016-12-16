package com.markit.pe.valuationengine.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.markit.pe.valuationengine.domain.PortfolioValuationUploadStatus.PortfolioValuationStatus;


@Converter(autoApply=true)
public class PortfolioValuationStatusEnumConverter implements AttributeConverter<PortfolioValuationStatus, String>{

	@Override
	public String convertToDatabaseColumn(PortfolioValuationStatus attribute) {
		// TODO Auto-generated method stub
		return attribute.displayName();
	}

	@Override
	public PortfolioValuationStatus convertToEntityAttribute(String dbData) {
			return PortfolioValuationStatus.convert(dbData);

	}



}
