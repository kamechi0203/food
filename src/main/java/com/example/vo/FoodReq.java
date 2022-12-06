package com.example.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FoodReq {

	@JsonProperty("city")
	private String city;

	@JsonProperty("store_name")
	private String store_name;

	@JsonProperty("evaluate")
	private int evaluate;

	@JsonProperty("store")
	private String store;

	@JsonProperty("menu")
	private String menu;

	@JsonProperty("price")
	private Integer price;
	
	@JsonProperty("menu_evaluate")
	private Integer menuEvaluate;
	
	private Integer num;

	public FoodReq() {

	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public Integer getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(Integer evaluate) {
		this.evaluate = evaluate;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getMenuEvaluate() {
		return menuEvaluate;
	}

	public void setMenuEvaluate(Integer menuEvaluate) {
		this.menuEvaluate = menuEvaluate;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}	

}
