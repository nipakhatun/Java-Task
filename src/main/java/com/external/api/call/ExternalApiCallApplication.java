package com.external.api.call;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;
import com.external.api.call.controller.AutomationExecutor;

@SpringBootApplication
public class ExternalApiCallApplication {
	private final static Logger logger = LoggerFactory.getLogger(ExternalApiCallApplication.class);
	@Autowired
	private AutomationExecutor process;

	public static void main(String[] args) {
		SpringApplication.run(ExternalApiCallApplication.class, args);
	}
	@Scheduled(fixedRate = 1296000000) //6hours
	public void scheduleTaskWithFixedRate() {
		logger.debug("Task start .......");
		process.doStart();
	}
}
