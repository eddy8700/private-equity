package com.markit.pe.valuationengine.domain;
/**
 * 
 */

import java.util.Date;

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
@ToString
public class PEValuationInput {	
	
	private String type;
	
	private String calVersion;
	
	private Date valDate;
	
}
