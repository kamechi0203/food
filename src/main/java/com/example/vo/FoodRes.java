package com.example.vo;

import java.util.List;

import com.example.entity.Store;
import com.example.entity.Food;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodRes {

	@JsonProperty("food_info")
	private Store food;
	@JsonProperty("store_info")
	private Food store;

	private String message;
	
	private List<String> storeInfoList;

	public FoodRes() {

	}

	public FoodRes(Store food) {
		this.food = food;
	}

	public FoodRes(Food store) {
		this.store = store;
	}

	public Store getFood() {
		return food;
	}

	public void setFood(Store food) {
		this.food = food;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Food getStore() {
		return store;
	}

	public void setStore(Food store) {
		this.store = store;
	}

	public List<String> getStoreInfoList() {
		return storeInfoList;
	}

	public void setStoreInfoList(List<String> storeInfoList) {
		this.storeInfoList = storeInfoList;
	}
	
	

}
