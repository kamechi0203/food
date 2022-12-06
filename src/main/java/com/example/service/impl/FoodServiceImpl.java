package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.constants.FoodRtnCode;
import com.example.entity.Store;
import com.example.entity.Food;
import com.example.repository.StoreDao;
import com.example.repository.FoodDao;
import com.example.service.ifs.FoodService;
import com.example.vo.FoodRes;

@Service
public class FoodServiceImpl implements FoodService {

	@Autowired
	private StoreDao storeDao;
	@Autowired
	private FoodDao foodDao;

	@Override // 第一題:新增
	public Store addstore(String city, String storename) {

		//判斷輸入的商店名稱，是否存在於資料庫
		if (storeDao.existsById(storename)) {  
			return null;//已存在則不回傳。
		}
		
		//判斷city & storename 是否為空。
		if (!StringUtils.hasText(city) || !StringUtils.hasText(storename)) {
			
			//如果為空 ，回傳null
			return null;
		}
		
		//把city 跟storename設定進資料庫
		Store store = new Store();

		store.setCity(city);
		store.setStorename(storename);

		return storeDao.save(store);
	}

	@Override // 第一題:修改
	public FoodRes changstore(String city, String store_name) {

		FoodRes res = new FoodRes();
		
		//判斷資料庫中是否有輸入的店名
		if (!storeDao.existsById(store_name)) {
			res.setMessage(FoodRtnCode.STORE_INEXISTED.getMessage());
			return res;
		}	
		
		Optional<Store> stoOp = storeDao.findById(store_name);
		
		//if (stoOp.isPresent()) {//判斷49行Optional<Store>是否有(store_name)值，的商店資訊。
			Store sto1 = stoOp.get();
			
			//將要修改的city設定進去sto1
			sto1.setCity(city);
			
			//將sto1存回資料庫
			storeDao.save(sto1);
			
			//成功回傳正確訊息
			res.setMessage(FoodRtnCode.SUCCESSFUL.getMessage());
			res.setFood(sto1);
			return res;
	}

	@Override // 第二題:新增
	public FoodRes addmenu(String store, String menu, int price, int menuEvaluate) {
		FoodRes res = new FoodRes();
		
		//判斷輸入的store、menu是否存在於資料庫
		Optional<Food> sto = foodDao.findByStoreNameAndMenu(store, menu);
		Optional<Store> sto1 = storeDao.findById(store);
		if (!StringUtils.hasText(store) || !StringUtils.hasText(menu)) {
			res.setMessage(FoodRtnCode.NAME_REQUIRED.getMessage());
			return res;
		}
		
		//判斷店家是否存在，若不存在則無法新增這間商店的餐點
		else if(!sto1.isPresent()) {
			res.setMessage("店家不存在");
			return res;
			
		}

		//判斷餐點是否存在，若已存在則無法新增餐點
		else if (sto.isPresent()) {

			res.setMessage("餐點已存在");
			return res;
		}
		
		//判斷價格是否為0或是負數
		else if (price + "" == null || price < 0 ) {
			res.setMessage(FoodRtnCode.PRICE_REQUIRED.getMessage());
			return res;
		}

		//判斷餐點評價是否為1-5
		else if (menuEvaluate + "" == null || menuEvaluate < 1 || menuEvaluate > 5) {
			res.setMessage(FoodRtnCode.EVALUATE_REQUIRED.getMessage());
			return res;
		}

		//將要新增的餐點資訊設定進去store1
		Food store1 = new Food();
		store1.setStoreName(store);
		store1.setMenu(menu);
		store1.setPrice(price);
		store1.setMenu_evaluate(menuEvaluate);
		
		//將新增好的餐點存回資料庫
		foodDao.save(store1);
		
		//將新增好的餐點存到res，並回傳
		res.setStore(store1);
		res.setMessage(FoodRtnCode.SUCCESSFUL.getMessage());
		return res;
	}

	@Override
	public String changeMenuEvaluate(String storeName) { // 第二題 修改
		
		//判斷資料庫是否存在此商店資訊
		Optional<Store> foodOp = storeDao.findById(storeName);
		if (!foodOp.isPresent()) {
			return null;
		}
		Store food = foodOp.get();
		
		//將這間店的所有餐點平價計算平均，當成店家評價
		double value = 0;
		List<Food> list = foodDao.findListByStoreName(storeName);
		for (Food store : list) {
			value += store.getMenu_evaluate();
		}
		double result = value / list.size();
		food.setEvaluate(Math.floor(result * 10.0) / 10.0);
		
		//將店家評價儲存進資料庫
		storeDao.save(food);
		return "Success";

	}

	@Override//第三題 城市找店家
	public List<String> findStoreNameByCity(String city, Integer num) {
		List<String> lis = new ArrayList<>();
		
		//判斷city是否為空或是null
		if (!StringUtils.hasText(city)) {
			return null;
		}
		
		//判斷資料庫裡是否有此城市的商店
		List<Store> list = storeDao.findAllByCity(city);
		if (list.isEmpty()) {
			return null;
		}
		for (Store food : list) {
			List<Food> list2 = foodDao.findAllByStoreName(food.getStorename());
			for (Food sto : list2) {
				lis.add("店名: " + sto.getStoreName() + " ,店家評價: " + food.getEvaluate() + " ,餐點名稱: " + sto.getMenu() + " ,價格: "
						+ sto.getPrice() + " ,餐點評價: " + sto.getMenu_evaluate());
			}

		}
		
		//設定要顯示的筆數
		if (num > lis.size()) {
			return lis;
		}
		if (num == null || num == 0) {
			return lis;
		}

		return lis.subList(0, num);

	}
	
	@Override//第四題 店家評價找店家
	public List<String> findStoreEvaluate(double evaluate) {

		List<String> list = new ArrayList<>();

		//尋找資料庫中，高於我們設定的店家評價的商店資訊，並用店家評價遞減排序
		List<Store> list1 = storeDao.findByEvaluateGreaterThanEqualOrderByEvaluateDesc(evaluate);
		
		//把每一間商店的餐點資訊，存成字串後，加進去list中
		for (Store store : list1) {
			List<Food> list2 = foodDao.findAllByStoreName(store.getStorename());
			for (Food food : list2) {
				list.add("城市: "+store.getCity() +" ,店名: " + food.getStoreName() + " ,店家評價: " + store.getEvaluate() + " ,餐點名稱: " + food.getMenu() + " ,價格: "
						+ food.getPrice() + " ,餐點評價: " + food.getMenu_evaluate());

			}
		}
		return list;

	}

	@Override//第五題 店家評價找餐廳後，與餐點評價找餐點
	public List<String> findStoreAndMenuEvaluate(double evaluate, int menuEvaluate) {

		List<String> list = new ArrayList<>();
		
		//找出餐點評價大於我們設定的所有餐點，並用餐點評價遞減排序
		List<Food> list2 = foodDao.findByMenuEvaluateGreaterThanEqualOrderByMenuEvaluateDesc(menuEvaluate);
		
		//找出店家評價大於我們設定的所有店家，並用店家評價遞減排序
		List<Store> list1 = storeDao.findByEvaluateGreaterThanEqualOrderByEvaluateDesc(evaluate);
		
		//把每一間商店的餐點資訊，存成字串後，加進去list中
		for (Store store : list1) {

			for (Food food : list2) {
				if (store.getStorename().equals(food.getStoreName())) {
					list.add("城市: "+store.getCity()+" ,店名: " + store.getStorename() + " ,店家評價: " + store.getEvaluate() + " ,餐點名稱: " + food.getMenu()
							+ " ,價格: " + food.getPrice() + " ,餐點評價: " + food.getMenu_evaluate());
				}
			}

		}
		return list;
	}
}