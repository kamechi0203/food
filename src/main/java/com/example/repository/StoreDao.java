package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Store;

@Repository
public interface StoreDao extends JpaRepository<Store, String>{
	List<Store>findAllByCity(String city);
//	List<Food> findByCity(String city);
	List<Store>findByEvaluateGreaterThanEqualOrderByEvaluateDesc(double evaluate);

}
