package com.markit.pe.positiondata.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.markit.pe.positiondata.domain.SecurityUploadStatus.SecurityLoadStatus;


@Converter(autoApply=true)
public class SecurityLoadStatusEnumConverter implements AttributeConverter<SecurityLoadStatus, String>{

	@Override
	public String convertToDatabaseColumn(SecurityLoadStatus attribute) {
		// TODO Auto-generated method stub
		return attribute.displayName();
	}

	@Override
	public SecurityLoadStatus convertToEntityAttribute(String dbData) {
			return SecurityLoadStatus.convert(dbData);

	}



}
