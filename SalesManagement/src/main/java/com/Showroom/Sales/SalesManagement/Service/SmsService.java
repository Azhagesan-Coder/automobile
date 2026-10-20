package com.Showroom.Sales.SalesManagement.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsService {
    @org.springframework.beans.factory.annotation.Autowired
    private com.Showroom.Sales.SalesManagement.Resilience.DownstreamCircuits circuits = new com.Showroom.Sales.SalesManagement.Resilience.DownstreamCircuits();

	@Value("${twilio.phone.number}")
	private String fromPhNum;

	public String sendSms(String to, String message) {
		// Protect the provider call without retrying potentially delivered messages.
		try {
			Message mess = circuits.call("sms", () -> Message.creator(new PhoneNumber(to), new PhoneNumber(fromPhNum), message).create());
			return "Message sent with Sid:" + mess.getSid(); 
		}catch(org.springframework.web.server.ResponseStatusException e) { throw e;
        }catch(Exception e) {
		  throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_GATEWAY, "SMS provider request failed", e);	
		}
	}
	
}
