package com.example.SpringBatchTutorial;

import io.prometheus.client.exporter.PushGateway;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringBatchTutorialApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringBatchTutorialApplication.class, args);
	}

	@Bean
	public PushGateway pushGateway(
			@Value("${prometheus.pushgateway.url:localhost:9091}") String url
	) {
		return new PushGateway(url);
	}
}
