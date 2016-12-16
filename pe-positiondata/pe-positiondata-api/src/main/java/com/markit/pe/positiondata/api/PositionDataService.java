package com.markit.pe.positiondata.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.markit.pe.positiondata.domain.PEClient;
import com.markit.pe.positiondata.domain.PESecurityDetails;
import com.markit.pe.positiondata.exception.PositionDataException;
import com.markit.pe.positiondata.response.PositionDataResponse;

public interface PositionDataService {

	
	/**
	 * Gets all the Private Equity Clients with details 
	 * 
	 * @return 
	 */
	public List<PEClient> getAllPEClients() throws PositionDataException;
	
	/**
	 * Adds a Private Equity Client
	 * 
	 * @param peClient
	 * @return 
	 */
	public PositionDataResponse<String> addPEClient(PEClient peClient) throws PositionDataException;
	
	
	/**
	 * Adds a Private Equity Client via EDM
	 * 
	 * @param peClient
	 * @return 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void addPEClientViaEdm(PEClient peClient) throws PositionDataException, IOException, InterruptedException;

	public PositionDataResponse<String> addSecurityDetails(PESecurityDetails peSecurityDetails,final PEClient client);

	public void editSecurityDetails(PESecurityDetails peSecurityDetails, PEClient peClient);

	public List<PESecurityDetails> fetchAuditInfo(String peSecurityDetails);

	//public HashMap<String, Object> fetchValuationHistory(Long channelId);

	Map<String, Object> getPortfolioPositions(PEClient peClient);

	Map<String, Object> getSecurityDetails(PEClient peClient);
	
	public Map<String, Object> handleSecurityUpload(MultipartFile file,final PEClient client) throws IOException;

	public Map<String, Object> getSecurityUploadStatus(final PEClient peClient);

	public Map<String, Object> checkForLatestFileStatus(Long portfolioId, String fileName);

	public PESecurityDetails populateRedemptionSchedule(PESecurityDetails peSecurityDetails);

	public Map<String, Object> getPEStaticDataMappings();

	public PESecurityDetails populateResetMargins(PESecurityDetails peSecurityDetails);

	public String getTestMessage(String x);
	
}
