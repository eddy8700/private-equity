package com.markit.pe.positiondata.request;

import com.markit.pe.positiondata.domain.PEClient;
import com.markit.pe.positiondata.domain.PESecurityDetails;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 *
 */
@Getter
@Setter
public class PESecuritySetupRequest {

	private PEClient clientDetails;
	
	private PESecurityDetails securityDetails;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PESecuritySetupRequest [clientDetails=" + clientDetails + ", securityDetails=" + securityDetails + "]";
	}
}
