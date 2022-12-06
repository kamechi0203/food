package com.example.constants;

public enum FoodRtnCode {

	SUCCESSFUL("200", "Successful"), 
	STORE_INEXISTED("400","Store is inexisted"),
	CITY_REQUIRED("400", "City is required or City is null!!"), 
	MENU_REQUIRED("400", "Menu is required or Menu is null"),
	NAME_REQUIRED("400", "Name is required or Name is null!!"), 
	EVALUATE_REQUIRED("400", "Evaluate is required or Evaluate needs 1 ~ 5!!"), 
	PRICE_REQUIRED("400", "Price is required or Price must be over zero!!"),
	ACCOUNT_EXISTED("403", "Account exsitend"),
	FAILURE("500", "Account active fail"), 
	ADD_ROLE_FAILURE("500", "Add role fail"),
	ROLE_LIST_IS_EMPTY("400","Role list is empty");

	private String code;

	private String message;

	private  FoodRtnCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}



	public String getMessage() {
		return message;
	}

	

}

