package com.markit.pe.valuationengine.domain;
/**
 * 
 */

import java.util.List;

import com.markit.pe.portfoliodata.PEComparableInfo;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author mahesh.agarwal
 *
 */
@Getter
@Setter
@ToString
public class PECustomizeCompRequest {
	
	private PESecurityInfoDTO peSecurityInfoDTO;
	
	private List<PEComparableInfo> comparableInfos;
	
}
