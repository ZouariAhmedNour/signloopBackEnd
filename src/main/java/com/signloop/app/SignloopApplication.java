package com.signloop.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableTransactionManagement
public class SignloopApplication {

	public static void main(String[] args) {
		SpringApplication.run(SignloopApplication.class, args);
	}


	@EventListener(ApplicationReadyEvent.class)
	public void logSwaggerUrl() throws UnknownHostException {
		String ip = InetAddress.getLocalHost().getHostAddress();
		System.out.println("\n\n✅ Swagger UI: http://" + ip + ":8080/swagger-ui/index.html\n");
	}
}



