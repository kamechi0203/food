package com.example.service.ifs;

import java.util.List;

import com.example.entity.Store;
import com.example.entity.Food;
import com.example.vo.FoodRes;

public interface FoodService {
	public Store addstore(String city, String store_name);
	public FoodRes changstore(String city, String store_name);

	public FoodRes addmenu(String store, String menu, int price, int menuEvaluate);
	public String changeMenuEvaluate(String storeName);

	public List<String> findStoreNameByCity(String city, Integer num);

	public List<String> findStoreEvaluate(double evaluate);
	
	public List<String> findStoreAndMenuEvaluate(double evaluate, int menuEvaluate);

}
