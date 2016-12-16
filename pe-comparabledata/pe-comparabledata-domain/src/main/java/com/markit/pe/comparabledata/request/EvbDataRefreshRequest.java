/**
 * 
 */
package com.markit.pe.comparabledata.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mahesh.agarwal
 *
 */
@Getter
@Setter
public class EvbDataRefreshRequest {
	
	private List<String> compList = new ArrayList<String>();
	
	private Date asOfDate;
	
	

	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EvbDataRefreshRequest [compList=" + compList + ", asOfDate=" + asOfDate + "]";
	}
}
