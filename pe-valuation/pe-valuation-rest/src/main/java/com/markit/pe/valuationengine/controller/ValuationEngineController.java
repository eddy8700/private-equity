/**
 * 
 */
package com.markit.pe.valuationengine.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.markit.pe.portfoliodata.PEPositionInfo;
import com.markit.pe.portfoliodata.constants.PEConstants;
import com.markit.pe.positiondata.domain.FloatingSecurityMargin;
import com.markit.pe.positiondata.domain.PEClient;
import com.markit.pe.positiondata.domain.PEPortfolio;
import com.markit.pe.positiondata.domain.RedemptionSchedule;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;
import com.markit.pe.valuationengine.api.ValuationEngineService;
import com.markit.pe.valuationengine.async.IPortfolioAsyncTaskExecutor;
import com.markit.pe.valuationengine.constant.PortfolioValuationConstants;
import com.markit.pe.valuationengine.domain.PECustomizeCompRequest;
import com.markit.pe.valuationengine.interservice.api.PEInterServiceTemplate;
import com.markit.pe.valuationengine.request.PEPortfolioValuationRequest;
import com.markit.pe.valuationengine.request.PEValuationRequest;
import com.markit.pe.valuationengine.response.ValuationEngineResponse;

/**
 * @author mahesh.agarwal
 *
 */
@RestController
@RefreshScope
public class ValuationEngineController {
	
	private static final Logger logger = LoggerFactory.getLogger(ValuationEngineController.class);

	
	@Value("${message}")
	private String message;
	
	@Autowired
	private ValuationEngineService valuationEngineService;	
	
	@Autowired
	private PEInterServiceTemplate peInterServiceTemplate;
	
	@Autowired
	private IPortfolioAsyncTaskExecutor portfolioExecutor;
	
	
	@RequestMapping("/landing")
	public String landingMessage(){
		return this.message;	
	}	
	
	@RequestMapping(method = RequestMethod.POST, value="/performValuation", consumes = "application/json")
	public ValuationEngineResponse<String> performValuation(@RequestBody PEValuationRequest peValuationRequest){
		logger.info("Rest request received at : /valuation-engine/performValuation ");
		logger.info(peValuationRequest.toString());
		final PESecurityInfoDTO dto = peValuationRequest.getPeSecurityInfoDTO();

		if (peValuationRequest.getPeSecurityInfoDTO().getType().equals(PEConstants.SECURITY_TYPE_FLOAT)) {
			logger.info("Check for the type float");
			final List<FloatingSecurityMargin> floatingSecurityMargins = valuationEngineService
					.getFloatingSecurityMarginByFiSecId(peValuationRequest.getPeSecurityInfoDTO().getFiSecId());
			dto.setFloatingSecurityMargins(floatingSecurityMargins);
		}
		if (peValuationRequest.getPeSecurityInfoDTO() != null && peValuationRequest.getPeSecurityInfoDTO()
				.getPrincipalPaymentType().equals(PEConstants.PRINCIPAL_PAYMENT_TYPE_SINKING_FUND)) {
			final List<RedemptionSchedule> redemptionSchedules = valuationEngineService
					.getRedemptionScheduleByFiSecId(peValuationRequest.getPeSecurityInfoDTO().getFiSecId());
			dto.setRedemptionSchedules(redemptionSchedules);
		}
		//peValuationRequest.setPeSecurityInfoDTO(dto);
		valuationEngineService.performValuation(peValuationRequest);

		logger.info("Rest request completed");

		ValuationEngineResponse<String> response = new ValuationEngineResponse<String>(true);
		response.setPayload(PortfolioValuationConstants.PROCESS_COMPLETED);
		response.setMessage(PortfolioValuationConstants.PROCESS_COMPLETED);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/getCustomizedOrLatestUsedCompInfos", consumes = "application/json")
	public ValuationEngineResponse<Map<String, Object>> getCustomizedOrLatestUsedCompInfos(@RequestBody PESecurityInfoDTO peSecurityInfoDTO) {
		logger.info("Rest request received at : /valuation-engine/getCustomizedOrLatestUsedCompInfos ");
		logger.info(peSecurityInfoDTO.toString());
		Map<String, Object> map = valuationEngineService.getCustomizedOrLatestUsedCompInfos(peSecurityInfoDTO);
		ValuationEngineResponse<Map<String, Object>> valuationEngineResponse = new ValuationEngineResponse<Map<String, Object>>(true);
		valuationEngineResponse.setPayload(map);		
		logger.info("Rest request completed");
		return valuationEngineResponse;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/getSystemGeneratedCompInfos", consumes = "application/json")
	public ValuationEngineResponse<Map<String, Object>> getSystemGeneratedCompInfos(@RequestBody PESecurityInfoDTO peSecurityInfoDTO) throws Exception{
		logger.info("Rest request received at : /valuation-engine/getSystemGeneratedCompInfos ");
		logger.info(peSecurityInfoDTO.toString());
		Map<String, Object> map = valuationEngineService.getSystemGeneratedCompInfos(peSecurityInfoDTO);
		ValuationEngineResponse<Map<String, Object>> valuationEngineResponse = new ValuationEngineResponse<Map<String, Object>>(true);
		valuationEngineResponse.setPayload(map);
		logger.info("Rest request completed");
		return valuationEngineResponse;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/customizeComps", consumes = "application/json")
	public ValuationEngineResponse<String> customizeComparables(@RequestBody PECustomizeCompRequest customizeCompRequest) throws Exception{
		logger.info("Rest request received at : /valuation-engine/customizeComps ");
		logger.info(customizeCompRequest.toString());
		
		valuationEngineService.customizeComparables(customizeCompRequest);
		
		logger.info("Rest request completed");		
		ValuationEngineResponse<String> response = new ValuationEngineResponse<String>(true);
		response.setPayload(PortfolioValuationConstants.PROCESS_COMPLETED);
		response.setMessage(PortfolioValuationConstants.PROCESS_COMPLETED);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/valuationHistory/{parentChannelId}")
	public ValuationEngineResponse<Map<String, Map>> valuationHistory(@PathVariable final Long parentChannelId) throws Exception{
		logger.info("Rest request received at : /valuation-engine/valuationHistory ");
		logger.info("Parent Channel Id received : "+parentChannelId);
		Map<String, Map> map = valuationEngineService.getValuationHistory(parentChannelId);
		ValuationEngineResponse<Map<String, Map>> valuationEngineResponse = new ValuationEngineResponse<Map<String, Map>>(true);
		valuationEngineResponse.setPayload(map);
		logger.info("Rest request completed");
		return valuationEngineResponse;
	}
	
	/*@RequestMapping(method = RequestMethod.GET, value="/calValKey/{parentChannelId}")
	public Map<String, String> getLatestCalAndValKey(@PathVariable final Long parentChannelId) throws Exception{
		logger.info("Rest request received at : /valuation-engine/calValKey ");
		logger.info("Parent Channel Id received : "+parentChannelId);
				
		logger.info("Rest request completed");
		return valuationEngineService.getLatestCalAndValKey(parentChannelId);
	}*/
	
	@RequestMapping(method = RequestMethod.POST, value="/performPortfolioValuation", consumes = "application/json")
	public ValuationEngineResponse<String> performPortfolioValuation(@RequestBody PEPortfolioValuationRequest pePortfolioValuationRequest) throws InterruptedException, ExecutionException{
		logger.info("Rest request received at : /valuation-engine/performPortfolioValuation ");
		logger.info(pePortfolioValuationRequest.toString());
		final PEPortfolio portfolio=valuationEngineService.fetchPortfolioForClient(pePortfolioValuationRequest.getClient());
		final Long portfolioValuationId=valuationEngineService.persistPortfolioValuationStatusIndb(portfolio);
		pePortfolioValuationRequest.setPortfolioValuationId(portfolioValuationId);
		final List<PEPositionInfo> pePositionInfos = peInterServiceTemplate.performInterServiceCallForPositionInfo(pePortfolioValuationRequest.getClient());
		final List<PESecurityInfoDTO> peSecurityInfoDTOs = new ArrayList<PESecurityInfoDTO>();
		for (PEPositionInfo pePositionInfo : pePositionInfos) {
			PESecurityInfoDTO peSecurityInfoDTO = new PESecurityInfoDTO();
			BeanUtils.copyProperties(pePositionInfo, peSecurityInfoDTO);
			peSecurityInfoDTO.setActiveCalVersion(pePositionInfo.getCalVersion());
			peSecurityInfoDTOs.add(peSecurityInfoDTO);
		}
		portfolioExecutor.performPortfolioValuation(peSecurityInfoDTOs, pePortfolioValuationRequest, valuationEngineService,portfolio);
		valuationEngineService.persistPortfolioValuationStatusInCache(portfolio);
		ValuationEngineResponse<String> response = new ValuationEngineResponse<String>(true);
		response.setPayload(PortfolioValuationConstants.PORTFOLIO_VALUATION_STARTED);
		response.setMessage(PortfolioValuationConstants.PORTFOLIO_VALUATION_STARTED);
		return response;
	
	}
	
	@RequestMapping(value = "/fetchPortfolioValuationStatus", method = RequestMethod.POST)
    public ValuationEngineResponse<Map<String, Object>> getPortfolioValuationStatus(@RequestBody final PEClient peClient) throws Exception {
		logger.info("Rest request received at : /valuation-engine/fetchPortfolioValuationStatus ");
		logger.info(peClient.toString());
		if(peClient != null){
			final PEPortfolio portfolio=valuationEngineService.fetchPortfolioForClient(peClient);
			 final Map<String, Object> result= valuationEngineService.getPortfolioValuationStatus(portfolio);
			 final ValuationEngineResponse<Map<String, Object>> positionDataResponse= new ValuationEngineResponse<Map<String,Object>>(true);
			 positionDataResponse.setPayload(result);
			 return positionDataResponse;
		}
		return null;
    }  
	/**
	 * @return the valuationEngineService
	 */
}
