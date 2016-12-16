package com.markit.pe.valuationengine.util;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public class EnumConverter implements Converter {

	@Override
	public <T> T convert(Class<T> type, Object value) {
		String enumValName = (String) value;
		Enum[] enumConstants = (Enum[]) type.getEnumConstants();

		for (Enum enumConstant : enumConstants) {
			if (enumConstant.name().equals(enumValName)) {
				return (T) enumConstant;
			}
		}

		throw new ConversionException(
				String.format("Failed to convert %s value to %s class", enumValName, type.toString()));
	}

}
