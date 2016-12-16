/**
 * 
 */
package com.markit.pe.comparabledata.api;

import java.util.Map;

import com.markit.pe.comparabledata.domain.EVBCompSearchCriteria;
import com.markit.pe.comparabledata.request.EvbDataRefreshRequest;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;

/**
 * @author mahesh.agarwal
 *
 */
public interface ComparableDataService {
	
	Map<String, Object> getEvbCompFilterMappings();
	
	Map<String, Object> searchEvbData(EVBCompSearchCriteria evbCompSearchCriteria);

	Map<String, Object> getLatestCompsDetails(EvbDataRefreshRequest evbDataRefreshRequest);

	Map<String, Object> getSystemGenerated(PESecurityInfoDTO peSecurityInfoDTO);

	Map<String, Object> getSystemGenerated_proc(PESecurityInfoDTO peSecurityInfoDTO);

	Map<String, Object> getLatestCompsDetails_proc(EvbDataRefreshRequest evbDataRefreshRequest);
	

}
