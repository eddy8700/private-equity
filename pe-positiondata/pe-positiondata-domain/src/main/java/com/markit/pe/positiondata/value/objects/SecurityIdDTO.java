package com.markit.pe.positiondata.value.objects;

import com.markit.pe.positiondata.domain.PEPortfolio;
import com.markit.pe.positiondata.domain.PESecurityDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SecurityIdDTO {

	
	private PESecurityDetails peSecurityDetails;
	
	private PEPortfolio pePortfolio;
}
