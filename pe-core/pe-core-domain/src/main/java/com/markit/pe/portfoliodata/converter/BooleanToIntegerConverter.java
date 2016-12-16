package com.markit.pe.portfoliodata.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class BooleanToIntegerConverter implements AttributeConverter<Boolean, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Boolean attribute) {
		return attribute==true? new Integer(1):new Integer(0);
	}

	@Override
	public Boolean convertToEntityAttribute(Integer dbData) {
		if(dbData.equals(Integer.valueOf(1)))
			return Boolean.TRUE;
		else
		return Boolean.FALSE;
	}

}
