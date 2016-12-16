package com.markit.pe.valuationengine.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.markit.pe.valuationengine.domain.PEValuationInfo;

@Repository
public interface PEValuationRepository extends MongoRepository<PEValuationInfo, Long>{

}
