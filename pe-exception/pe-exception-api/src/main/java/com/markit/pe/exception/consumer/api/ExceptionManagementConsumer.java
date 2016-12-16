package com.markit.pe.exception.consumer.api;

import org.springframework.messaging.Message;

import com.markit.pe.exception.domain.PEException;

public interface ExceptionManagementConsumer {
	
	public void consumeExceptionMessage(Message<PEException> exceptionManagement);

}
