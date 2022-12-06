package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "store")
@IdClass(value = FoodId.class)
public class Food {

	@Id
	@Column(name = "store")
	private String storeName;
	@Id
	@Column(name = "menu")
	private String menu;
	
	@Column(name = "price")
	private int price;
	
	@Column(name = "menu_evaluate")
	private int menuEvaluate;
	
	
	
	
	public Food() {
		
	}
	public Food(String store,String menu,int price,int menuEvaluate) {
		this.storeName=store;
		this.menu=menu;
		this.price=price;
		this.menuEvaluate=menuEvaluate;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getMenu_evaluate() {
		return menuEvaluate;
	}

	public void setMenu_evaluate(int menu_evaluate) {
		this.menuEvaluate = menu_evaluate;
	}
	
	
}
