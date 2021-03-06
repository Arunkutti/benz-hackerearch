package com.hacker.benz.service;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hacker.benz.resources.CONSTANTS;
import com.hacker.benz.resources.ChargerArrayRes;
import com.hacker.benz.resources.ChargersRespRes;
import com.hacker.benz.resources.RestaurantRes;
import com.hacker.benz.resources.RestaurantsRespRes;

@Service
public class DistanceCalculationService {

	Logger logger = LoggerFactory.getLogger(DistanceCalculationService.class);

	@Autowired
	private RestAPIUtil restUtil;

	@Value("${base.calculation.url}")
	private String baseURL;

	@Value("${chargers.endpoint}")
	private String chargersEndpoint;

	@Value("${restaurants.endpoint}")
	private String restaurantsEndpoint;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseEntity calculateDistance(Integer fuelLevel) {
		String calcReqURL = baseURL + chargersEndpoint;
		logger.info("Calling external endpoint to get charges response ", calcReqURL);
		ResponseEntity<ChargersRespRes> apiResponse = (ResponseEntity<ChargersRespRes>) restUtil
				.getForEntity(calcReqURL, ChargersRespRes.class);
		if (apiResponse.getStatusCode().equals(HttpStatus.OK)) {
			ChargersRespRes responseObj = apiResponse.getBody();
			List<Integer> array = null;
			for (ChargerArrayRes chargerArray : responseObj.getChargers()) {
				if (Objects.nonNull(chargerArray.getArray()) && chargerArray.getArray().size() >= fuelLevel
						&& chargerArray.getArray().get(fuelLevel - 1) > 0) {
					array = chargerArray.getArray();
					break;
				}
			}
			if (Objects.nonNull(array)) {
				logger.info("Response containg the charging capacity", fuelLevel, array.get(fuelLevel - 1));
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				logger.info("Response not containg the size of the", fuelLevel);
				return findTop10RestaurantResp();
			}
		} else {
			return findTop10RestaurantResp();
		}
	}

	/**
	 * Method to return restaurant response
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ResponseEntity findTop10RestaurantResp() {
		String calcReqURL = baseURL + restaurantsEndpoint;
		logger.info("Calling external endpoint to get restaurant response", calcReqURL);
		ResponseEntity<RestaurantsRespRes> apiResponse = (ResponseEntity<RestaurantsRespRes>) restUtil
				.getForEntity(calcReqURL, RestaurantsRespRes.class);
		if (apiResponse.getStatusCode().equals(HttpStatus.OK)) {
			RestaurantsRespRes response = apiResponse.getBody();
			response.setRestaurants(sortBasedOnRating(response.getRestaurants(), CONSTANTS.NUMBER_OF_RESTUARANTS));
		}
		return new ResponseEntity(apiResponse.getBody(), HttpStatus.BAD_REQUEST);
	}

	public List<RestaurantRes> sortBasedOnRating(List<RestaurantRes> restaurants, Integer noOfRecords) {
		for (int i = 0; i < restaurants.size(); i++) {
			for (int j = restaurants.size() - 1; j > i; j--) {
				if (restaurants.get(i).getRating() < restaurants.get(j).getRating()) {
					RestaurantRes tmp = restaurants.get(i);
					restaurants.set(i, restaurants.get(j));
					restaurants.set(j, tmp);
				}
			}
		}
		if (restaurants.size() > noOfRecords) {
			restaurants = restaurants.subList(0, noOfRecords);
		}
		return restaurants;
	}

}