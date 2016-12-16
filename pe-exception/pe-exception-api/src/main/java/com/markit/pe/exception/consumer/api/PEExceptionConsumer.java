package com.markit.pe.exception.consumer.api;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface PEExceptionConsumer {
	@Input("peExceptionTopic")
	SubscribableChannel demo();
}
