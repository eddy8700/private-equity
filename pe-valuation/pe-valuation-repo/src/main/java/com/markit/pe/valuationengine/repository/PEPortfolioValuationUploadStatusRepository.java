package com.markit.pe.valuationengine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.markit.pe.valuationengine.domain.PortfolioValuationUploadStatus;
import com.markit.pe.valuationengine.domain.PortfolioValuationUploadStatus.PortfolioValuationStatus;

@Repository
public interface PEPortfolioValuationUploadStatusRepository extends JpaRepository<PortfolioValuationUploadStatus, Long> {

	PortfolioValuationUploadStatus findByPePortfolioPortfolioName(String clientName);

	@Query(value="Select pvus from PortfolioValuationUploadStatus pvus where pvus.id=(Select MAX(pvus1.id) from PortfolioValuationUploadStatus pvus1 where pvus1.pePortfolio.id=?1)")
	PortfolioValuationUploadStatus findByPePortfolioIdAndMaxUploadStatusId(Long portfolioId);

	List<PortfolioValuationUploadStatus> findByPortfolioValuationStatus(PortfolioValuationStatus inProgress);

}
