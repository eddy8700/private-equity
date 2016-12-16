package com.markit.pe.valuationengine.async;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.markit.pe.positiondata.domain.PEPortfolio;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;
import com.markit.pe.valuationengine.api.PortfolioValuationDao;
import com.markit.pe.valuationengine.api.ValuationEngineService;
import com.markit.pe.valuationengine.domain.PortfolioValuationUploadStatus.PortfolioValuationStatus;
import com.markit.pe.valuationengine.interservice.api.PEInterServiceTemplate;
import com.markit.pe.valuationengine.request.PEPortfolioValuationRequest;

@Component
public class PortfolioAsyncTaskExecutor implements IPortfolioAsyncTaskExecutor {

	private static final Logger logger = LoggerFactory.getLogger(PortfolioAsyncTaskExecutor.class);
	
	@Value("${portfolio.valuation.thread.pool.size}")
	private int threadPoolSize;
	
   @Autowired
   private PortfolioValuationDao portfolioValuationDao;
	
   @Autowired
   private PEInterServiceTemplate peInterserviceTemplate;
	
	@Async
	@Override
	public void performPortfolioValuation(final List<PESecurityInfoDTO> peSecurityInfoDTOs,
			final PEPortfolioValuationRequest pePortfolioValuationRequest,
			final ValuationEngineService valuationEngineService,final PEPortfolio pePortfolio) {
		long beginTime = System.currentTimeMillis();
		logger.info("Creating the executor with the thread pool {}",threadPoolSize);
		ExecutorService executors = Executors.newFixedThreadPool(threadPoolSize);
		for (PESecurityInfoDTO peSecurityInfoDTO : peSecurityInfoDTOs) {
			executors.submit(new PortfolioValuationTask(peSecurityInfoDTO, pePortfolioValuationRequest.getValDate(), pePortfolioValuationRequest.getPortfolioValuationId(),
					valuationEngineService));
			
		}
		logger.info("Shutting down the executor");
		executors.shutdown();
		while (!executors.isTerminated()) {
			try {
				Thread.sleep(2000);
				//logger.info("waiting for tasks....");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//logger.info("Main thread waiting ... ");
		}
		if(executors.isTerminated()){
			logger.info("Executor shutdown successfully");
			final Boolean isExceptionPresent=peInterserviceTemplate.performInterServiceCallForCheckPortfolioValuationException(pePortfolio.getId(), pePortfolioValuationRequest.getPortfolioValuationId());
			if(isExceptionPresent){
				logger.info("Some exception occured for portfolioValuationId{}",pePortfolioValuationRequest.getPortfolioValuationId());
				portfolioValuationDao.updatePortfolioStatus(pePortfolioValuationRequest.getPortfolioValuationId(), PortfolioValuationStatus.DONE_WITH_EXCEPTIONS);
			}
			else{
				logger.info("Persisting in the database");
				portfolioValuationDao.updatePortfolioStatus(pePortfolioValuationRequest.getPortfolioValuationId(), PortfolioValuationStatus.DONE);
			}
		}
		long endTime = System.currentTimeMillis();
		long timeTaken = endTime - beginTime;
		

	}
}