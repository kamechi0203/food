package com.example.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FoodId implements Serializable{
	private String storeName;
	private String menu;
	
	public FoodId() {
		
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String store) {
		this.storeName = store;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

}
