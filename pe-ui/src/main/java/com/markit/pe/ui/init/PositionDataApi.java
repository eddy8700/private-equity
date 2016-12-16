package com.markit.pe.ui.init;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.markit.pe.portfoliodata.AuthTokenDTO;
import com.markit.pe.positiondata.domain.PEClient;
import com.markit.pe.positiondata.response.PositionDataResponse;

@FeignClient(value="http://positiondata-service")
public interface PositionDataApi {
	
	@RequestMapping(value = "/validateTicket", method = RequestMethod.GET)
	void authenticateUser();
	@RequestMapping(value = "/getAllPeClients", method = RequestMethod.GET)
	PositionDataResponse<List<PEClient>> getAllPeClients(@RequestHeader("Authorization") String authorizationToken);
	

}
