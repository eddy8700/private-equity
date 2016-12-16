/**
 * 
 */
package com.markit.pe.positiondata.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.markit.pe.portfoliodata.PEPositionInfo;
import com.markit.pe.portfoliodata.constants.PEConstants;
import com.markit.pe.portfoliodata.util.DuplicateDomainHandler;
import com.markit.pe.portfoliodata.util.IDgeneratorUtil;
import com.markit.pe.portfoliodata.validator.SingleValidationError;
import com.markit.pe.portfoliodata.validator.ValidationError;
import com.markit.pe.portfoliodata.validator.Validator;
import com.markit.pe.positiondata.api.FloatingRateMarginService;
import com.markit.pe.positiondata.api.PositionDataService;
import com.markit.pe.positiondata.api.ProcessLauncherService;
import com.markit.pe.positiondata.api.RedemptionScheduleService;
import com.markit.pe.positiondata.domain.FloatingSecurity;
import com.markit.pe.positiondata.domain.FloatingSecurityMargin;
import com.markit.pe.positiondata.domain.PEClient;
import com.markit.pe.positiondata.domain.PEClientFundInfo;
import com.markit.pe.positiondata.domain.PEFund;
import com.markit.pe.positiondata.domain.PEPortfolio;
import com.markit.pe.positiondata.domain.PEPortfolioSecurityInfo;
import com.markit.pe.positiondata.domain.PESecurityDetails;
import com.markit.pe.positiondata.domain.PESecurityDetailsAudit;
import com.markit.pe.positiondata.domain.PEStaticDataMap;
import com.markit.pe.positiondata.domain.RedemptionSchedule;
import com.markit.pe.positiondata.domain.SecurityUploadStatus;
import com.markit.pe.positiondata.domain.SecurityUploadStatus.SecurityLoadStatus;
import com.markit.pe.positiondata.exception.PositionDataException;
import com.markit.pe.positiondata.repository.PEClientFundRepository;
import com.markit.pe.positiondata.repository.PEClientRepository;
import com.markit.pe.positiondata.repository.PEFundRepository;
import com.markit.pe.positiondata.repository.PEPortfolioRepository;
import com.markit.pe.positiondata.repository.PEPortfolioSecurityInfoRepository;
import com.markit.pe.positiondata.repository.PESecurityAuditRepository;
import com.markit.pe.positiondata.repository.PESecurityDetailsRepository;
import com.markit.pe.positiondata.repository.PEStaticDataMapRepository;
import com.markit.pe.positiondata.repository.PEValuationRepository;
import com.markit.pe.positiondata.repository.SecurityUploadStatusRepository;
import com.markit.pe.positiondata.response.PositionDataResponse;
import com.markit.pe.positiondata.util.CacheManager;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;
import com.markit.pe.positiondata.value.objects.SecurityIdDTO;

/**
 * @author mahesh.agarwal
 *
 */
@Service
@Transactional
public class PositionDataServiceImpl implements PositionDataService,ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(PositionDataServiceImpl.class);
	
	
	@Autowired
	PEClientRepository peClientRepository;
	
	@Autowired
	ApplicationContext applicationContext;
	
   @Autowired
   private PEStaticDataMapRepository peStaticDataMapRepository;
	
	@Autowired
	private RedemptionScheduleService  redemptionScheduleService;
	
	@Autowired
	private FloatingRateMarginService  floatingRateMarginService;
	
	@Autowired
	PEFundRepository peFundRepository;

	@Autowired
	PEClientFundRepository peClientFundRepository;

	@Autowired
	PEPortfolioRepository pePortfolioRepository;

	@Autowired
	PESecurityDetailsRepository peSecurityDetailsRepository;
	
	@Autowired
	private IDgeneratorUtil idGeneratorUtil;
	
	@Autowired
	private CacheManager cacheManager;
	
	

	@Autowired
	private PEPortfolioSecurityInfoRepository pePortfolioSecRepo;
	
	@Resource(name="peDuplicateHandler")
	private DuplicateDomainHandler<PESecurityDetails> duplicateSecurityDetailsHandler;
	
	@Value("${edm.security.bulk.upload.temp.file.location}")
	private String tempDocLocaltion;
	
	@Autowired
	private PESecurityAuditRepository peSecurityAuditrepo;

	@Autowired
	private PEValuationRepository peValuationRepo;
	
	@Autowired
	private ProcessLauncherService processLauncherService;
	
	@Autowired
	private SecurityUploadStatusRepository securityUploadRepo;
	
	@Autowired
	private Validator<PEClient, ValidationError<PEClient>> peClientValidator;
	
	@Autowired
	private Validator<PESecurityDetails, ValidationError<PESecurityDetails>> peSecurityDetailsValidator;
	
	
	
	@Override
	public List<PEClient> getAllPEClients() throws PositionDataException {
		LOGGER.debug("Fetching all the pe clients");
		List<PEClient> peClients = (List<PEClient>) peClientRepository.findAll();
		if (null != peClients && !peClients.isEmpty()) {
			return peClients;
		} else {
			throw new PositionDataException("No Private Equity Clients Found");
		}
	}

	@Override
	public PositionDataResponse<String> addPEClient(final PEClient peClient) throws PositionDataException {
		LOGGER.info("Validating the client input");
		final List<SingleValidationError<PEClient>> validationErrorSet=peClientValidator.validate(peClient);
		if(CollectionUtils.isEmpty(validationErrorSet)){
		LOGGER.info("No validtiion error for pe client with the name {}",peClient.getClientAbbrName());	
		LOGGER.debug("Persisting the pe client into the database");
		final PEClientFundInfo clientFundInfo = new PEClientFundInfo();
		final PEFund fund = buildPEFundInfo(peClient);
		clientFundInfo.setClient(peClient);
		clientFundInfo.setFund(fund);
		final PEClientFundInfo persistedPEClientFund = peClientFundRepository.save(clientFundInfo);
		buildPortfolioInfo(peClient.getClientAbbrName(), persistedPEClientFund);
		PositionDataResponse<String> response = new PositionDataResponse<String>(true);
		response.setPayload("PE Client on-boarded successfully");
		return response;
		}
		else{
			LOGGER.info("Validation error occuered while adding pe client");
			PositionDataResponse<String> response = new PositionDataResponse<String>(true);
			response.setPayload("PE Client Validation failed");
			response.setMessage(peClientValidator.buildErrorMessage(validationErrorSet));
			return response;
		   
		}

	}

	private void buildPortfolioInfo(final String portfolioName, final PEClientFundInfo persistedPEClientFund) {
		PEPortfolio pePortfolio = new PEPortfolio(portfolioName);
		pePortfolio.setPeClientFund(persistedPEClientFund);
		pePortfolio.setSecurityCount(Integer.valueOf(0));
		pePortfolio = (PEPortfolio) pePortfolioRepository.save(pePortfolio);
		LOGGER.debug("PE portfolio saved successfully ", pePortfolio.getId());
	}

	public PEFund buildPEFundInfo(final PEClient persistedPeClient) {
		final PEFund fund = PEFund.builder().fundName(persistedPeClient.getClientAbbrName()).build();
		return fund;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.markit.pe.positiondata.service.IPositionDataService#addPEClientViaEdm
	 * (com.markit.pe.portfoliodata.PEClient)
	 */
	@Override
	public void addPEClientViaEdm(PEClient peClient) throws PositionDataException, IOException, InterruptedException {
		String response = null;

		Gson gson = new Gson();
		String jsonInString = gson.toJson(peClient);

		//String inputParam = "-input " + jsonInString;

		// String[] cmd = { filePath, inputParam };

		String[] cmdArray = new String[8];

		cmdArray[0] = "CADISProcessAgent.exe";

		cmdArray[1] = "/component:Solution";

		cmdArray[2] = "/process:\"UI EDM JSON TEST\"";

		cmdArray[3] = "/platform:SQL";

		cmdArray[4] = "/server:LON6DVEDMSQL01";

		cmdArray[5] = "/db:PE_DEV";

		cmdArray[6] = "/integrated:Yes";

		cmdArray[7] = "/parameters:JsonString=" + jsonInString;

		Process p = Runtime.getRuntime().exec(cmdArray);
		p.waitFor();
		InputStream in = p.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		int c = -1;
		while ((c = in.read()) != -1) {
			baos.write(c);
		}

		response = new String(baos.toByteArray());

		if (StringUtils.isNotEmpty(response) && response.equalsIgnoreCase("")) {

		} else {
			throw new PositionDataException("PE Client not saved");
		}
	}
	
	@Override
	public PositionDataResponse<String> addSecurityDetails(final PESecurityDetails peSecurityDetails, final PEClient client) {
		LOGGER.info("Validating the security details input");
		final List<SingleValidationError<PESecurityDetails>> validationErrorSet=new ArrayList<>();//peSecurityDetailsValidator.validate(peSecurityDetails);
		if(CollectionUtils.isEmpty(validationErrorSet)){
			LOGGER.info("No Validation error in setting up the security and now looking for any default values");
			PESecurityDetails defaultSecurityDetails =peSecurityDetailsValidator.buildForDefaultValues(peSecurityDetails);
			LOGGER.info("Saving the security details into db");
			final PEFund fund = peFundRepository.findByFundName(client.getClientAbbrName());
			final PEPortfolio pePortfolio = pePortfolioRepository.findByPortfolioName(client.getClientAbbrName());
			defaultSecurityDetails.setFund(fund);
			final boolean isSecurityExists = duplicateSecurityDetailsHandler.processDuplicateDomain(defaultSecurityDetails);
			if (!isSecurityExists) {
				LOGGER.info("updating the security count in portfolio");
				updatePortfolioSecurityCount(pePortfolio);
				SecurityIdDTO dto= new SecurityIdDTO(peSecurityDetails, pePortfolio);
				defaultSecurityDetails.setSecurityId(idGeneratorUtil.buildPattern(dto));
				defaultSecurityDetails.setSecurityVersion(Integer.valueOf(1));
				 PEPortfolioSecurityInfo pePortfolioSecurityInfo = new PEPortfolioSecurityInfo();
				pePortfolioSecurityInfo.setPePortfolio(pePortfolio);
				pePortfolioSecurityInfo.setPeSecurityDetails(defaultSecurityDetails);
				defaultSecurityDetails = peSecurityDetailsRepository.save(defaultSecurityDetails);
				buildSecurityDetailsPostProcessorInfo(defaultSecurityDetails);
				LOGGER.debug("Fetching the channel id ");
				LOGGER.debug("Building portfolio security info");
				pePortfolioSecurityInfo= pePortfolioSecRepo.save(pePortfolioSecurityInfo);
				buildParentChannelIdAndUpdateRecord(pePortfolioSecurityInfo);
				LOGGER.info("Security details saved with the id", defaultSecurityDetails.getFiSecId());
				PositionDataResponse<String> response = new PositionDataResponse<String>(true);
				response.setPayload("PE Security setup successfull");
				List<String> message=new ArrayList<>();
				message.add("PE Security setup successfull");
				response.setMessage(message);
				return response;

			} else {
				throw new PositionDataException("Security Already Exists with the given parameter .Please try again");
			}
		}
		else{
			LOGGER.info("Validation error in setting up the security");
			PositionDataResponse<String> response = new PositionDataResponse<String>(true);
			response.setPayload("PE Security setup validation failed");
			response.setMessage(peSecurityDetailsValidator.buildErrorMessage(validationErrorSet));
			return response;
		}
		
	}

	

	private void buildSecurityDetailsPostProcessorInfo(PESecurityDetails peSecurityDetails) {
		if((peSecurityDetails instanceof FloatingSecurity)){
			buildFloatingRateMarginInfo(peSecurityDetails);
		}
		if(null != peSecurityDetails.getPrincipalPaymentType() && 
				peSecurityDetails.getPrincipalPaymentType().equals(PEConstants.PRINCIPAL_PAYMENT_TYPE_SINKING_FUND)){
			buildRedemptionInfo(peSecurityDetails);
		}
		
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private synchronized void updatePortfolioSecurityCount(PEPortfolio portfolio) {
      LOGGER.info("Updating the security count in portfolio info");
      final Integer intialCount=portfolio.getSecurityCount();
      portfolio.setSecurityCount(intialCount + Integer.valueOf(1));
      pePortfolioRepository.save(portfolio);
	}

	private void buildRedemptionInfo(PESecurityDetails peSecurityDetails) {
		LOGGER.info("Security has Sinking repayments..Hence building the redemption info");
		if(CollectionUtils.isNotEmpty(peSecurityDetails.getRedemptionSchedules())){
			redemptionScheduleService.persistRedemptionInfo(peSecurityDetails); //TODO
			Long fiSecId = peSecurityDetails.getFiSecId();
			String redemptionId = peSecurityDetails.getSecurityId() + "_"
					+ peSecurityDetails.getSecurityVersion();
			redemptionScheduleService.persistSecurityRedemptionMapping(fiSecId, redemptionId); //TODO
		}
	
		
	}

	private void buildFloatingRateMarginInfo(PESecurityDetails peSecurityDetails) {
		LOGGER.info("Building the floating margin info for the securiity");
		final FloatingSecurity floatingSecurity=(FloatingSecurity) peSecurityDetails;
		if(CollectionUtils.isNotEmpty(floatingSecurity.getResetMargins())){
			floatingRateMarginService.persistFloatingSecurityMarginInfo(floatingSecurity); //TODO
			Long fiSecId = peSecurityDetails.getFiSecId();
			String marginId = peSecurityDetails.getSecurityId() + "_"
					+ peSecurityDetails.getSecurityVersion();
			floatingRateMarginService.persistSecurityMarginMapping(fiSecId, marginId);
		}
	
		
	}

	/**
	 * @param pePortfolioSecurityInfo
	 */
	private void buildParentChannelIdAndUpdateRecord(PEPortfolioSecurityInfo pePortfolioSecurityInfo) {
		LOGGER.debug("Fetching the channel id for portfolio security info"+pePortfolioSecurityInfo.getId());
		  final Long channelId=pePortfolioSecurityInfo.getId();
		  final Integer updatedResultInfo=pePortfolioSecRepo.updateParentChannelId(channelId);
		  System.out.println(updatedResultInfo);
		  LOGGER.debug("Portolio sec info updated with the parent channel id",updatedResultInfo);
		
	}

	/**
	 * @param peSecurityDetails
	 * @param fund
	 * @return
	 */
	/*private Integer buildSecVersion(PESecurityDetails peSecurityDetails, final PEFund fund) {
		final Integer maxSecurityVersion = peSecurityDetailsRepository
				.findByFundIdAndSecurityIdAndFetchMaxSecurityVersion(fund.getId(), peSecurityDetails.getSecurityId());
		if (null != maxSecurityVersion && maxSecurityVersion != 0)
			return maxSecurityVersion + 1;
		else
			return Integer.valueOf(1);

	}*/
	
	/* (non-Javadoc)
	 * @see com.markit.pe.positiondata.service.IPositionDataService#getPortfolioPositions(com.markit.pe.portfoliodata.PEClient)
	 */
	@Override
	public Map<String, Object> getPortfolioPositions(final PEClient peClient){
		
		Map<String, Object> results = new HashMap<String, Object>();
		//final PEPortfolio pePortfolio = pePortfolioRepository.findByPortfolioName(peClient.getClientAbbrName());
		List<PEPositionInfo> latestValuationsPostition = peValuationRepo.getPortfolioPositions(peClient.getClientAbbrName());
		List<PEPositionInfo> latestSecurityPositions= peValuationRepo.findLatestSecurityWithoutValuationAndCalibration(peClient.getClientAbbrName());
		latestValuationsPostition.addAll(latestSecurityPositions);
		results.put("resultsCount", latestValuationsPostition.size());
		results.put("content", latestValuationsPostition);
		LOGGER.info("Returned Positions for Portfolio '"+peClient.getClientName()+"'");
		LOGGER.info("Number of Positions returned: "+latestValuationsPostition.size());
		return results;
	}
	
	@Override
	public void editSecurityDetails(final PESecurityDetails peSecurityDetails, final PEClient peClient) {
		LOGGER.debug("Editing the security details");
		final PEFund fund = peFundRepository.findByFundName(peClient.getClientAbbrName());
		final String securityId = peSecurityDetails.getSecurityId();
		final Integer securityVersion = peSecurityDetails.getSecurityVersion();
		peSecurityDetails.setSecurityVersion(updateSecurityVersion(securityVersion));
		peSecurityDetails.setFund(fund);
		final PEPortfolioSecurityInfo pePortfolioSecurityInfo = pePortfolioSecRepo
				.findByPeSecurityDetailsFundIdAndPeSecurityDetailsSecurityIdAndPeSecurityDetailsSecurityVersion(
						fund.getId(), securityId, securityVersion);
		
		if(pePortfolioSecurityInfo != null){
			final Long parentChannelId = pePortfolioSecurityInfo.getParentChannelId();
			final Long previousChannelId=pePortfolioSecurityInfo.getId();
			final PESecurityDetails persistedSecurityDetails = peSecurityDetailsRepository.save(peSecurityDetails);
			LOGGER.debug("Persisted security details with security id", persistedSecurityDetails.getFiSecId());
			final PEPortfolioSecurityInfo psecurityInfo = new PEPortfolioSecurityInfo(persistedSecurityDetails,
					pePortfolioSecurityInfo.getPePortfolio());
			psecurityInfo.setParentChannelId(parentChannelId);
			psecurityInfo.setPreviousChannelId(previousChannelId);
			LOGGER.debug("Persisting the portfolio security info with the parent channel id ",parentChannelId);
			pePortfolioSecRepo.save(psecurityInfo);
		}
		
	}

	private synchronized Integer updateSecurityVersion(Integer securityVersion) {
		return securityVersion+1;
	}

	@Override
	public List<PESecurityDetails> fetchAuditInfo(final String securityId) {
		       final List<PESecurityDetails> listpeSecurityDetails=peSecurityDetailsRepository.findBySecurityId(securityId);
		       for (PESecurityDetails peSecurityDetails : listpeSecurityDetails) {
		    	   final List<PESecurityDetailsAudit> audits=peSecurityAuditrepo.findByPeSecurityDetailsFiSecId(peSecurityDetails.getFiSecId()); 
		    	   peSecurityDetails.setAudits(audits);
			}
		       return listpeSecurityDetails;
		
	}

	/*@Override
	public HashMap<String, Object> fetchValuationHistory(final Long channelId) {
		   final List<PEValuationInfo> peValuationInfos=peValuationRepo.findByChannelId(channelId);
		   final Set<PECalibrationInfo> calibrationInfos=new HashSet<>();
		   final HashMap<String,Object> map =new HashMap<>();
		   for (PEValuationInfo peValuationInfo : peValuationInfos) {
			   final Long calibratrionId=peValuationInfo.getCalibrationId();
			   if(calibratrionId != null){
				       final PECalibrationInfo peCalInfo=calibrationRepo.findOne(calibratrionId);
			        calibrationInfos.add(peCalInfo);
			        map.put("valuationInfo", peValuationInfo);
			        map.put("calibrationInfo", calibrationInfos);
			   }
		}
		return map;
	}*/
	
	
	@Override
	public Map<String, Object> getSecurityDetails(final PEClient peClient){
		
		Map<String, Object> results = new HashMap<String, Object>();
		
		List<PESecurityInfoDTO> peSecurityInfoDTOs = peValuationRepo.getSecurityDetails(peClient.getClientAbbrName());
		
		results.put("resultsCount", peSecurityInfoDTOs.size());
		results.put("content", peSecurityInfoDTOs);
		LOGGER.info("Returned Securities for Portfolio '"+peClient.getClientName()+"'");
		LOGGER.info("Number of Securities returned: "+peSecurityInfoDTOs.size());
		return results;
	}

	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Map<String, Object> handleSecurityUpload(MultipartFile file,final PEClient client) throws IOException {
		final String originalFileName=file.getOriginalFilename();
		 final String modifiedFileName=appendTimeStamp(originalFileName);
		Files.copy(file.getInputStream(), Paths.get(tempDocLocaltion, modifiedFileName),StandardCopyOption.REPLACE_EXISTING);
		File fileToRead = new File(tempDocLocaltion,modifiedFileName);
		
		if (fileToRead.exists()) {
			return processLauncherService.launchEDMProcess(fileToRead, client);
		}
		else{
			throw new IllegalArgumentException("File does not exists");
		}
	}

	private String appendTimeStamp(String originalFileName) {
		   final long timeStamp=System.currentTimeMillis();
		   return String.valueOf(timeStamp)+originalFileName ;			   
	}

	@Override
	public Map<String, Object> getSecurityUploadStatus(final PEClient peClient) {
	  Map<String, Object> result= new HashMap<>();
	 // LOGGER.info("Fetching the data from the cache for the portfolio id {}",portfolioId);
	  final PEPortfolio pePortfolio = pePortfolioRepository.findByPortfolioName(peClient.getClientAbbrName());
	  final Long portfolioId=pePortfolio.getId();
	  final List<String> fileNames=cacheManager.getValue(portfolioId.toString());
	  if(null != fileNames && !fileNames.isEmpty()){
		  final String latestFile=fileNames.get(fileNames.size()-1);
		  LOGGER.info("Some files still present in the pending or new state");
		  result=checkForLatestFileStatus(portfolioId,latestFile);
		  cacheManager.evictCache(result, portfolioId, latestFile);
	  }
	return result;
	}

	@Override
	public Map<String, Object> checkForLatestFileStatus(final Long portfolioId,final String fileName){
		 final Map<String, Object> result= new HashMap<>();
		 final SecurityUploadStatus secUploadStatus=securityUploadRepo.findByPePortfolioIdAndFileName(portfolioId,fileName);
		 //TODO handle case where mismatch in cache is db. Present in cache but deleted from db
		 LOGGER.info("Found the status {} from db for the portfolio id {}",secUploadStatus.getLoadStatus(),secUploadStatus.getPePortfolio().getId());
		  final SecurityLoadStatus loadStatus=secUploadStatus.getLoadStatus();
		  final String message= loadStatus.parse(loadStatus, secUploadStatus.getFileName());
		  result.put("uploadStatus", loadStatus);
	      result.put("uploadMessage", message);
	      return result;
	}

	@Override
	public PESecurityDetails populateRedemptionSchedule(final PESecurityDetails peSecurityDetails) {
		final String principalPaymentType=peSecurityDetails.getPrincipalPaymentType();
		if(principalPaymentType.equals(PEConstants.PRINCIPAL_PAYMENT_TYPE_SINKING_FUND)){
			LOGGER.info("Fetch redemption schedule for the security fisec id {}",peSecurityDetails.getFiSecId());
			final List<RedemptionSchedule> redemptionSchedules=redemptionScheduleService.getRedemptionScheduleByFiSecId(peSecurityDetails.getFiSecId());
		    if(!redemptionSchedules.isEmpty()){
		    	LOGGER.info("Setting redemption schedules in pesecurity details object");
		    	peSecurityDetails.setRedemptionSchedules(redemptionSchedules);
		}}
		return peSecurityDetails;
	}

	@Override
	public Map<String, Object> getPEStaticDataMappings() {
		LOGGER.info("In getPEStaticDataMappings()");
		Map<String, Object> mappings = new HashMap<String, Object>();
		LOGGER.info("Fetching from database ...");
		List<PEStaticDataMap> peStaticDataMap = getPEStaticDataMap();
		Map<Long, PEStaticDataMap> peStaticDataRawMap = new HashMap<Long, PEStaticDataMap>();
		for(PEStaticDataMap entity : peStaticDataMap){
			peStaticDataRawMap.put(entity.getId(), entity);
		}
		for(PEStaticDataMap entity : peStaticDataRawMap.values()){
			if(null==entity.getParentId()){
				if(mappings.containsKey(entity.getType())){
					//List<Map<String, Object>> valueList = (List<Map<String, Object>>) mappings.get(entity.getType());
					boolean hasChild = false;
					Map<String, Object> internalMap =  new HashMap<String, Object>();
					internalMap.put("name", entity.getValue());
					for(PEStaticDataMap entity1 : peStaticDataRawMap.values()){
						if(null != entity1.getParentId() && entity1.getParentId()==entity.getId()){
							hasChild = true;
							if(internalMap.containsKey(entity1.getType())){
								List<String> childList = (List<String>) internalMap.get(entity1.getType());
								childList.add(entity1.getValue());
							} else {
								List<String> childList = new ArrayList<String>();
								childList.add(entity1.getValue());
								internalMap.put(entity1.getType(),childList);
							}					
						}
					}
					
					if(hasChild){
						List<Map<String, Object>> valueList = (List<Map<String, Object>>) mappings.get(entity.getType());
						valueList.add(internalMap);
						mappings.put(entity.getType(), valueList);
					} else {
						List<String> valueList = (List<String>) mappings.get(entity.getType());
						valueList.add(entity.getValue());
						mappings.put(entity.getType(), valueList);
					}					
					
				} else {
					//List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>>();
					boolean hasChild = false;
					Map<String, Object> internalMap =  new HashMap<String, Object>();
					internalMap.put("name", entity.getValue());
					for(PEStaticDataMap entity1 : peStaticDataRawMap.values()){
						if(null != entity1.getParentId() && entity1.getParentId() == entity.getId()){
							hasChild = true;
							if(internalMap.containsKey(entity1.getType())){
								List<String> childList = (List<String>) internalMap.get(entity1.getType());
								childList.add(entity1.getValue());
							} else {
								List<String> childList = new ArrayList<String>();
								childList.add(entity1.getValue());
								internalMap.put(entity1.getType(),childList);
							}					
						}
					}
					if(hasChild){
						List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>>();
						valueList.add(internalMap);
						mappings.put(entity.getType(), valueList);
					} else {
						List<String> valueList = new ArrayList<String>();
						valueList.add(entity.getValue());
						mappings.put(entity.getType(), valueList);
					}									
				}
			}						
		}
		LOGGER.info("Returning getPEStaticDataMappings()");
		LOGGER.debug("With : "+mappings);
		return mappings;
	
	}

	private List<PEStaticDataMap> getPEStaticDataMap() {
     List<PEStaticDataMap> peStaticDataMap = (List<PEStaticDataMap>) peStaticDataMapRepository.findAll();
		return peStaticDataMap;
	}

	@Override
	public PESecurityDetails populateResetMargins(final PESecurityDetails peSecurityDetails) {
		LOGGER.info("Populating reset margins for fi sec id {}",peSecurityDetails.getFiSecId());
        if(peSecurityDetails instanceof FloatingSecurity){
        	LOGGER.info("Floating Security");
        	final FloatingSecurity floatingSecurity=(FloatingSecurity) peSecurityDetails;
        	final List<FloatingSecurityMargin> floatingSecurityMargins=floatingRateMarginService.getFloatingSecurityMarginByFiSecId(peSecurityDetails.getFiSecId());
        	  if(!CollectionUtils.isEmpty(floatingSecurityMargins)){
        		  LOGGER.info("Setting reset margins in pesecurity details object");
        		  floatingSecurity.setResetMargins(floatingSecurityMargins);
        	  }
        	
        	return floatingSecurity;
        }
		return peSecurityDetails;
	}

	@Override
	public String getTestMessage(String x) {
		// TODO Auto-generated method stub
		return x + "Hello";
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
		
	}


}
