package com.markit.pe.positiondata.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.markit.pe.positiondata.domain.PESecurityDetails;

@NoRepositoryBean
@Transactional
public interface BasePESecurityRepository<T extends PESecurityDetails> extends JpaRepository<T, Long>{

}
