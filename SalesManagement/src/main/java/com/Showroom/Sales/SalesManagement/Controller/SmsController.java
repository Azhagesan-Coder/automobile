package com.Showroom.Sales.SalesManagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Showroom.Sales.SalesManagement.Service.SmsService;

@RestController
@RequestMapping("/api/sms")
public class SmsController {
	
	@Autowired
	private SmsService smsService;
	
	@PostMapping("/send")
	public ResponseEntity<String> sendSms(@RequestParam String to, @RequestParam String msg){
		String result = smsService.sendSms(to, msg);
		return ResponseEntity.ok(result);
	}

}
