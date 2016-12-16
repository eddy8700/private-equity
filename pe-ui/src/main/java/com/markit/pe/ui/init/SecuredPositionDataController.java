package com.markit.pe.ui.init;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.markit.pe.portfoliodata.AuthTokenDTO;
import com.markit.pe.positiondata.domain.PEClient;
import com.markit.pe.positiondata.response.PositionDataResponse;
import com.markit.pe.ui.init.PositionDataApi;

@RestController
public class SecuredPositionDataController extends BaseController {
	
	@Autowired
	private PositionDataApi positionDataApi;
	
	@RequestMapping("/landing")
	public String landingMessage() {
		return "Hello Ui";
	}
	
	
	@RequestMapping(value = "/validateTicket", method = RequestMethod.GET)
	public void authenticate() {
		System.out.println("Inside authentication");
		// Authenticate the user
		// TODO If authentication fails, return an unauthorized error code
		
	/*	AuthTokenDTO authToken =*/ positionDataApi.authenticateUser();

		//return authToken;
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/getAllPeClients")
	public PositionDataResponse<List<PEClient>> getAllPeClients() {
		return positionDataApi.getAllPeClients(getAuthorizationToken());
	}
}