package com.markit.pe.portfoliodata.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.markit.pe.portfoliodata.PrincipalPaymentType;

@Converter(autoApply=true)
public class PrincipalPaymentTypeEnumConverter implements AttributeConverter<PrincipalPaymentType, String> {

	@Override
	public String convertToDatabaseColumn(PrincipalPaymentType attribute) {
		return attribute.displayName();
	}

	@Override
	public PrincipalPaymentType convertToEntityAttribute(String dbData) {
		// TODO Auto-generated method stub
		return PrincipalPaymentType.convert(dbData);
	}

}
