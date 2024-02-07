package com.example.springbootawssqs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SpringBootApplication
public class AwsSQSDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsSQSDemoApplication.class, args);
	}


	@Autowired
	SQSService sqsService;

	@Bean
	public Supplier<String> postStandard() {
		return ()->sqsService.postMessage("Sample message for standard queue. "+new Date(),
				MESSAGE_QUEUE_TYPE.STANDARD);
	}

	@Bean
	public Supplier<String> postFIFO() {
		return ()->sqsService.postMessage("Sample message for FIFO queue. "+new Date(),
				MESSAGE_QUEUE_TYPE.FIFO);
	}

	@Bean
	public Supplier<List<Map<String,Object>>> consumeStandard() {
		return ()->sqsService.consumeMessages(MESSAGE_QUEUE_TYPE.STANDARD);
	}

	@Bean
	public Supplier<List<Map<String,Object>>> consumeFIFO() {
		return ()->sqsService.consumeMessages(MESSAGE_QUEUE_TYPE.FIFO);
	}

}
