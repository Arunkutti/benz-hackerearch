package com.hacker.benz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hacker.benz.service.DistanceCalculationService;

@RestController
public class DistanceCalculationRestController {

	Logger logger = LoggerFactory.getLogger(DistanceCalculationRestController.class);

	@Autowired
	DistanceCalculationService distanceCalcService;

	@SuppressWarnings("rawtypes")
	@GetMapping(value = "distance/calculate/{fuelLevel}")
	public ResponseEntity calculateDistance(@PathVariable(name = "fuelLevel", required = true) Integer fuelLevel) {
		logger.info("Request received for calculating distance for chargers ", fuelLevel);
		return distanceCalcService.calculateDistance(fuelLevel);
	}

}
