/**
 * 
 */
package com.markit.pe.positiondata.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.markit.pe.positiondata.api.PositionDataService;
import com.markit.pe.positiondata.domain.PEClient;
import com.markit.pe.positiondata.domain.PESecurityDetails;
import com.markit.pe.positiondata.exception.PositionDataException;
import com.markit.pe.positiondata.request.PESecuritySetupRequest;
import com.markit.pe.positiondata.response.PositionDataResponse;

/**
 * @author mahesh.agarwal
 *
 */
//@CrossOrigin/*(allowedHeaders={"Origin", "X-Requested-With", "Content-Type", "Accept","x-access-token"},methods={RequestMethod.POST,RequestMethod.GET,RequestMethod.OPTIONS,RequestMethod.DELETE})*/
@RestController
@RefreshScope
public class PositionDataController {

	private static final Logger logger = LoggerFactory.getLogger(PositionDataController.class);

	@Autowired
	private PositionDataService positionDataService;

	@Value("${message}")
	private String message;

	@RequestMapping("/landing")
	public String landingMessage() {
		return this.message;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/addPeClient", consumes = "application/json")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	@PreAuthorize("hasPermission(#user,'ADD_CLIENT')")
	public PositionDataResponse<String> addPEClient(@RequestHeader("Authorization") String authenticationToken,
			@RequestBody final PEClient peClient) throws PositionDataException, IOException, InterruptedException {
		logger.info("POST Request received to addPeClient");
		final PositionDataResponse<String> response = positionDataService.addPEClient(peClient);
		return response;

	}

	@RequestMapping(method = RequestMethod.POST, value = "/getTestMessage")
	public String getTestMessage(final String x) {
		logger.info("POST Request received to get all PE Clients");
		List<PEClient> result = positionDataService.getAllPEClients();
		final String x1 = positionDataService.getTestMessage(x);
		return x1;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getAllPeClients")
	 //@PreAuthorize("hasRole('ROLE_ADMIN')")
	//@PreAuthorize("hasPermission(#user,'VIEW_CLIENT')")
	public PositionDataResponse<List<PEClient>> getAllPeClients(
			/*@RequestHeader("Authorization") String authenticationToken*/) {
		logger.info("GET Request received to get all PE Clients");
		List<PEClient> result = positionDataService.getAllPEClients();
		PositionDataResponse<List<PEClient>> positionDataResponse = new PositionDataResponse<List<PEClient>>(true);
		positionDataResponse.setPayload(result);
		return positionDataResponse;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/securitySetup", consumes = "application/json")
	@PreAuthorize("hasPermission(#user,'ADD_SECURITY')")
	public PositionDataResponse<String> securitySetup(@RequestBody final PESecuritySetupRequest securitySetupRequest) {
		logger.info("POST recieved to setup a security, input :" + securitySetupRequest);
		final PESecurityDetails peSecurityDetails = securitySetupRequest.getSecurityDetails();
		final PEClient peClient = securitySetupRequest.getClientDetails();
		return positionDataService.addSecurityDetails(peSecurityDetails, peClient);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/editSecurity", consumes = "application/json")
	@PreAuthorize("hasPermission(#user,'UPDATE_SECURITY')")
	public PositionDataResponse<String> editSecurity(@RequestBody final PESecuritySetupRequest securitySetupRequest) {
		logger.info("POST recieved to edit a security");
		final PESecurityDetails peSecurityDetails = securitySetupRequest.getSecurityDetails();
		final PEClient peClient = securitySetupRequest.getClientDetails();
		positionDataService.editSecurityDetails(peSecurityDetails, peClient);
		PositionDataResponse<String> response = new PositionDataResponse<String>(true);
		response.setPayload("PE Security updated successfully");
		List<String> message = new ArrayList<>();
		message.add("PE Security updated successfully");
		response.setMessage(message);
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fetchAuditTrail/{securityId:.+}")
	public PositionDataResponse<List<PESecurityDetails>> fetchAuditInfo(@PathVariable final String securityId) {
		System.out.println("Message" + message);
		logger.debug("Debg level check");
		logger.info("GET recieved to fetch audit trail");
		final List<PESecurityDetails> result = positionDataService.fetchAuditInfo(securityId);
		PositionDataResponse<List<PESecurityDetails>> positionDataResponse = new PositionDataResponse<List<PESecurityDetails>>(
				true);
		positionDataResponse.setPayload(result);
		return positionDataResponse;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/securityDetails", consumes = "application/json")
	public PositionDataResponse<Map<String, Object>> securityDetails(@RequestBody final PEClient peClient) {
		logger.info("POST recieved with Client : " + peClient);
		final Map<String, Object> result = positionDataService.getSecurityDetails(peClient);
		PositionDataResponse<Map<String, Object>> positionDataResponse = new PositionDataResponse<>(true);
		positionDataResponse.setPayload(result);
		return positionDataResponse;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/portfolioPositions", consumes = "application/json")
	public PositionDataResponse<Map<String, Object>> getPortfolioPositions(@RequestBody final PEClient peClient) {
		logger.info("POST recieved with : " + peClient);
		final Map<String, Object> result = positionDataService.getPortfolioPositions(peClient);
		PositionDataResponse<Map<String, Object>> positionDataResponse = new PositionDataResponse<>(true);
		positionDataResponse.setPayload(result);
		return positionDataResponse;
	}

	@RequestMapping(value = "/doUpload", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public PositionDataResponse<Map<String, Object>> handleSecurityUpload(
			@RequestPart(value = "clientDetails") PEClient clientDetails,
			@RequestPart(value = "file") MultipartFile file) throws Exception {
		if (!file.isEmpty()) {
			final Map<String, Object> result = positionDataService.handleSecurityUpload(file, clientDetails);
			PositionDataResponse<Map<String, Object>> positionDataResponse = new PositionDataResponse<Map<String, Object>>(
					true);
			positionDataResponse.setPayload(result);
			return positionDataResponse;
		} else {
			// TODO handle it
		}
		return null;
	}

	@RequestMapping(value = "/fetchUploadStatus", method = RequestMethod.POST)
	public PositionDataResponse<Map<String, Object>> getSecurityUploadStatus(@RequestBody final PEClient peClient)
			throws Exception {
		if (peClient != null) {
			final Map<String, Object> result = positionDataService.getSecurityUploadStatus(peClient);
			PositionDataResponse<Map<String, Object>> positionDataResponse = new PositionDataResponse<Map<String, Object>>(
					true);
			positionDataResponse.setPayload(result);
			return positionDataResponse;
		}
		return null;
	}

	@RequestMapping(value = "/fetchSecurityDetails", method = RequestMethod.POST)
	public PositionDataResponse<PESecurityDetails> fetchSecurityDetails(
			@RequestBody final PESecurityDetails peSecurityDetails) throws Exception {
		logger.info("GET Request received to fetchSecurityDetails()");
		if (peSecurityDetails != null) {
			PESecurityDetails result = positionDataService.populateRedemptionSchedule(peSecurityDetails);
			result = positionDataService.populateResetMargins(result);
			PositionDataResponse<PESecurityDetails> positionDataResponse = new PositionDataResponse<PESecurityDetails>(
					true);
			positionDataResponse.setPayload(result);
			return positionDataResponse;
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getPEStaticDataMappings")
	public PositionDataResponse<Map<String, Object>> getPEStaticDataMappings() {
		logger.info("GET Request received to getPEStaticDataMappings()");
		Map<String, Object> map = positionDataService.getPEStaticDataMappings();
		PositionDataResponse<Map<String, Object>> comparableDataResponse = new PositionDataResponse<Map<String, Object>>(
				true);
		comparableDataResponse.setPayload(map);
		return comparableDataResponse;
	}

}
