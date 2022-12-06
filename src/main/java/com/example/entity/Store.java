package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "foodmap")
public class Store {

	@Id
	@Column(name = "store_name")
	private String storename;

	
	@Column(name = "city")
	private String city;


	@Column(name = "evaluate")
	private double evaluate;

	public Store() {

	}

	public Store(String city, String store_name, int evaluate) {

		this.city = city;
		this.storename = store_name;
		this.evaluate = evaluate;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStorename() {
		return storename;
	}

	public void setStorename(String storename) {
		this.storename = storename;
	}

	public double getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(double evaluate) {
		this.evaluate = evaluate;
	}

    
	

}
