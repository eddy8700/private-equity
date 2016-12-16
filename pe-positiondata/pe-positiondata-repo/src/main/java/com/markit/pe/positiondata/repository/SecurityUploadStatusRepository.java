package com.markit.pe.positiondata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.markit.pe.positiondata.domain.SecurityUploadStatus;
import com.markit.pe.positiondata.domain.SecurityUploadStatus.SecurityLoadStatus;

@Repository
public interface SecurityUploadStatusRepository extends JpaRepository<SecurityUploadStatus, Long> {

	List<SecurityUploadStatus> findByPePortfolioId(Long id);

	SecurityUploadStatus findByPePortfolioIdAndFileName(Long portfolioId,String fileName);

	List<SecurityUploadStatus> findByLoadStatusIn(List<SecurityLoadStatus> list);

	
	
}
