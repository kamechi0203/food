package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.example.entity.Food;
import com.example.entity.FoodId;

@Repository
public interface FoodDao extends JpaRepository<Food, FoodId>{
	public Optional<Food> findByStoreNameAndMenu(String storeName,String Menu);
	public List<Food> findListByStoreName(String storeName);
	public List<Food> findListByMenu(String menu);
	public List<Food> findAllByStoreName(String store);
	public List<Food> findByMenuEvaluateGreaterThanEqualOrderByMenuEvaluateDesc(int menuEvaluate);
	

}
