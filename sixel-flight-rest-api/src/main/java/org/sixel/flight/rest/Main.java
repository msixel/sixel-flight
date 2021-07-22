package org.sixel.flight.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author marcos
 *
 */
@SpringBootApplication
@ComponentScan("org.sixel.flight.rest")
@ConfigurationPropertiesScan("org.sixel.flight.rest")
@EnableAutoConfiguration(exclude = {
		WebSocketServletAutoConfiguration.class,
		FreeMarkerAutoConfiguration.class,
		JtaAutoConfiguration.class,
		MultipartAutoConfiguration.class,
		TransactionAutoConfiguration.class})
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}