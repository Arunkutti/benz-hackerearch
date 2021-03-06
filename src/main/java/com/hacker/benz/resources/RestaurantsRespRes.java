package com.hacker.benz.resources;

import java.util.List;

public class RestaurantsRespRes {

	private List<RestaurantRes> restaurants;

	/**
	 * @return the restaurants
	 */
	public List<RestaurantRes> getRestaurants() {
		return restaurants;
	}

	/**
	 * @param restaurants the restaurants to set
	 */
	public void setRestaurants(List<RestaurantRes> restaurants) {
		this.restaurants = restaurants;
	}

}
