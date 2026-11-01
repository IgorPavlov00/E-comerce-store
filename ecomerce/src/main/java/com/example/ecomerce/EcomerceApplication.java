package com.example.ecomerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EcomerceApplication {

	public static void main(String[] args) {
		// The Stripe client uses HttpURLConnection. Its keep-alive timer closes idle TLS
		// connections while holding the connection-cache lock, which stalled the next Stripe
		// call for over a minute. Stripe calls are infrequent, so reuse isn't worth that.
		System.setProperty("http.keepAlive", "false");
		SpringApplication.run(EcomerceApplication.class, args);
	}
}
