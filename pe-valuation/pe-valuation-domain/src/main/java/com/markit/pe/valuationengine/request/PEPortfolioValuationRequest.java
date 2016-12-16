package com.markit.pe.valuationengine.request;
/**
 * 
 */

import java.util.Date;
import java.util.List;

import com.markit.pe.portfoliodata.PEPositionInfo;
import com.markit.pe.positiondata.domain.PEClient;

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
public class PEPortfolioValuationRequest {
	
	private Date valDate;
	
	private List<PEPositionInfo> positions;

	private PEClient client;
	
	private Long portfolioValuationId;
	

	}
	
	 

