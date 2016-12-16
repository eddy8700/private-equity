package com.markit.pe.valuationengine.domain;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PEExceptionProducer {

	@Output("peExceptionTopic")
	MessageChannel demo(); 
}
