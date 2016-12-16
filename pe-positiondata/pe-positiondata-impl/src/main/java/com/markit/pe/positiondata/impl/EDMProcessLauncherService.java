package com.markit.pe.positiondata.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Holder;

import org.datacontract.schemas._2004._07.cadis.CADISComponentsComponentKey;
import org.datacontract.schemas._2004._07.cadis.EMessagePriority;
import org.datacontract.schemas._2004._07.cadis_webservice.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cadisedm.schemas.IProcessLauncher1;
import com.cadisedm.schemas.ProcessLauncher;
import com.markit.pe.positiondata.api.ProcessLauncherService;
import com.markit.pe.positiondata.constant.BulkUploadConstants;
import com.markit.pe.positiondata.domain.PEClient;
import com.markit.pe.positiondata.domain.PEPortfolio;
import com.markit.pe.positiondata.domain.SecurityUploadStatus;
import com.markit.pe.positiondata.domain.SecurityUploadStatus.SecurityLoadStatus;
import com.markit.pe.positiondata.repository.PEPortfolioRepository;
import com.markit.pe.positiondata.repository.SecurityUploadStatusRepository;
import com.markit.pe.positiondata.util.CacheManager;
import com.markit.pe.positiondata.util.RetryTemplate;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfKeyValueOfstringstring;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfKeyValueOfstringstring.KeyValueOfstringstring;

@Component
public class EDMProcessLauncherService implements ProcessLauncherService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EDMProcessLauncherService.class);

	@Value("${edm.domain.username}")
	private String domainUsername;

	@Value("${edm.security.bulk.upload.queue.parameter.name}")
	private String queueName;

	@Value("${edm.security.bulk.upload.process.name}")
	private String processName;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private SecurityUploadStatusRepository securityUploadRepo;


	@Autowired
	PEPortfolioRepository pePortfolioRepository;

	@Autowired
	private RetryTemplate retryTemplate;
	
	@Value("${edm.url}")
    private String edmUrl;

	//final ProcessLauncher launcher = new ProcessLauncher();
	//final IProcessLauncher1 process = launcher.getCADISWebService();
	//final String securityToken = buildSecurityToken(process);

	@Override
	public Map<String,Object> launchEDMProcess(File fileToPass, final PEClient client) throws MalformedURLException {
		final PEPortfolio pePortfolio = pePortfolioRepository.findByPortfolioName(client.getClientAbbrName());
		final ProcessLauncher launcher = new ProcessLauncher(edmUrl);
		final IProcessLauncher1 process = launcher.getCADISWebService();
		final String securityToken = buildSecurityToken(process);
		ArrayOfKeyValueOfstringstring configurableParameters = buildConfigurableParameters(client, fileToPass);
		long startTime = System.currentTimeMillis();
		 SecurityUploadStatus securityUploadStatus=initiateUpload(pePortfolio,fileToPass);
		final com.cadisedm.schemas.Error errorResponse = process.sendProcessLaunchMessageWithParameters(securityToken, queueName,
				CADISComponentsComponentKey.SOLUTION, processName, EMessagePriority.NORMAL, configurableParameters);
		long endTime = System.currentTimeMillis();
		LOGGER.info("Security Token is " + securityToken);

		LOGGER.info("Process " +processName+ " launcher returned code "+errorResponse.getResult());
		LOGGER.info("Took " + (long) (endTime - startTime));
		Map<String, Object> map = new HashMap<>();
		if (errorResponse.getResult().equals(ErrorCode.ERROR)){
			securityUploadStatus = updateUploadStatus(pePortfolio,fileToPass,securityUploadStatus);
			map.put("uploadStatus", securityUploadStatus.getLoadStatus());
			map.put("uploadMessage", BulkUploadConstants.BULK_UPLOAD_FAILED);
			return map;
		}
		if (errorResponse.getResult().equals(ErrorCode.OK)){
		    map=retryTemplate.retryBulkUpload(pePortfolio.getId(),fileToPass.getName());
		    cacheManager.populateCache(map,pePortfolio.getId(),fileToPass.getName());
			 return map;
		}
		return null;

	}


/*	private String buildFailureMesage(SecurityLoadStatus loadStatus) {
	     if(loadStatus.equals(SecurityLoadStatus.LOAD_FAILED))
	    	 return BulkUploadConstants.BULK_UPLOAD_FAILED;
		return null;
	}*/


	private SecurityUploadStatus updateUploadStatus(PEPortfolio pePortfolio, File fileToPass, SecurityUploadStatus securityUploadStatus2) {
		securityUploadStatus2.setLoadStatus(SecurityLoadStatus.LOAD_FAILED);
		final SecurityUploadStatus savedSecurityUpload=securityUploadRepo.save(securityUploadStatus2);
		return savedSecurityUpload;
	}

	private ArrayOfKeyValueOfstringstring buildConfigurableParameters(final PEClient client, File fileToPass) {
		final ArrayOfKeyValueOfstringstring configurableParameters = new ArrayOfKeyValueOfstringstring();
		List<KeyValueOfstringstring> keyValueOfstringstring = new ArrayList<>();
		KeyValueOfstringstring e1 = new KeyValueOfstringstring();
		KeyValueOfstringstring e2 = new KeyValueOfstringstring();
		KeyValueOfstringstring e3 = new KeyValueOfstringstring();
		KeyValueOfstringstring e4 = new KeyValueOfstringstring();
		e1.setKey("User");
		e1.setValue(domainUsername);
		if (client != null) {
			e2.setKey("ClientId");
			e2.setValue(client.getId().toString());
			e3.setKey("ClientName");
			e3.setValue(client.getClientName());
		}
		e4.setKey("FileName");
		e4.setValue(fileToPass.getName());

		keyValueOfstringstring.add(e1);
		keyValueOfstringstring.add(e2);
		keyValueOfstringstring.add(e3);
		keyValueOfstringstring.add(e4);
		configurableParameters.setKeyValueOfstringstring(keyValueOfstringstring);
		return configurableParameters;
	}

	private String buildSecurityToken(final IProcessLauncher1 process) {
		final Holder<String> connectResult = new Holder<>();
		process.connect(domainUsername, connectResult, null);
		return connectResult.value;
	}

	private SecurityUploadStatus initiateUpload(PEPortfolio pePortfolio, File fileToRead) {
		if(pePortfolio != null){
			final SecurityUploadStatus securityUploadStatus= new SecurityUploadStatus(pePortfolio,SecurityLoadStatus.NEW ,fileToRead.getName());
		  final SecurityUploadStatus savedSecurityUpload=securityUploadRepo.save(securityUploadStatus);
		  return savedSecurityUpload;
		}
		return null;

	}
}
