package com.markit.pe.valuationengine.request;
/**
 * 
 */

import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;
import com.markit.pe.valuationengine.domain.PEValuationInput;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author mahesh.agarwal
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PEValuationRequest {
	
	private PEValuationInput peValuationInput;
	
	private PESecurityInfoDTO peSecurityInfoDTO;
	
}
