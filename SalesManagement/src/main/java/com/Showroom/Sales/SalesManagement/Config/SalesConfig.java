package com.Showroom.Sales.SalesManagement.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.twilio.Twilio;

import jakarta.annotation.PostConstruct;

@Configuration
public class SalesConfig {

	@Value("${twilio.account.sid}")
	private String accountSid;
	
	@Value("${twilio.auth.token}")
	private String authToken;
	
	@PostConstruct
	public void initTwilio() {
		if (!accountSid.isBlank() && !authToken.isBlank()) Twilio.init(accountSid, authToken);
	}
}
