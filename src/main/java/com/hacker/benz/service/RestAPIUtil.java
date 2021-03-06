package com.hacker.benz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestAPIUtil {

	@Autowired
	private RestTemplate restTemplate;

	public ResponseEntity<?> getForEntity(String REQUEST_URI, Class<?> respone) {
		ResponseEntity<?> entity = restTemplate.getForEntity(REQUEST_URI, respone);
		return entity;
	}

}
