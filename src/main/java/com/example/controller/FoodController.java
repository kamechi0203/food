package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.constants.FoodRtnCode;

import com.example.entity.Store;
import com.example.entity.Food;
import com.example.repository.StoreDao;
import com.example.repository.FoodDao;
import com.example.service.ifs.FoodService;
import com.example.vo.FoodReq;
import com.example.vo.FoodRes;

@RestController
public class FoodController {

	@Autowired
	private FoodService foodService;
	FoodRes res = new FoodRes();

	@Autowired
	private StoreDao storeDao;

	@Autowired
	private FoodDao foodDao;

	@PostMapping(value = "/api/addstore") // 第一題:新增
	public FoodRes addstore(@RequestBody FoodReq req) {

		Store store = foodService.addstore(req.getCity(), req.getStore_name());

		//如果為空，會回傳一個名稱為空的訊息
		if (store == null) {
			res.setMessage(FoodRtnCode.NAME_REQUIRED.getMessage());
			return res;
		}
		
		//回傳儲存好的商店資訊
		return new FoodRes(store);
	}

	@PostMapping(value = "/api/changestore") // 第一題:修改  更新商店資訊
	public FoodRes changestore(@RequestBody FoodReq req) {

		//判斷商店名稱是否為空值NULL
		if(!StringUtils.hasText(req.getStore_name())) {
			res.setMessage(FoodRtnCode.NAME_REQUIRED.getMessage());
			return res;
		}

		//判斷city是否為空值或NULL
		if (!StringUtils.hasText(req.getCity())) {
			res.setMessage(FoodRtnCode.CITY_REQUIRED.getMessage());
			return res;
		}

		//使用impl的方法並回傳res
		return foodService.changstore(req.getCity(), req.getStore_name());
	}

	@PostMapping(value = "/api/addmenu")//第二題 新增餐點
	public FoodRes addmenu(@RequestBody FoodReq req) {
		
		FoodRes store = foodService.addmenu(req.getStore(), req.getMenu(), req.getPrice(), req.getMenuEvaluate());
		
		//如果store為空,回傳一個空訊息。
		if (store == null) {
			res.setMessage(FoodRtnCode.MENU_REQUIRED.getMessage());
			return res;
		}
		
		//如果store有值的話,回傳store
		return store;
	}

	@PostMapping(value = "/api/changeMenuEvaluate")//第二題 修改餐點評價，並修改店家評價
	public FoodRes changeMenuEvaluate(@RequestBody FoodReq req) {
		
		String name = req.getStore_name();
		
		//如果name為null或空回傳一個name不能為空的錯誤訊息
		if (name == null || !StringUtils.hasText(name)) {
			res.setMessage(FoodRtnCode.NAME_REQUIRED.getMessage());
			return res;
		}

		//如果取得的menu為null或空回傳一個menu不能為空的錯誤訊息
		if (req.getMenu() == null || !StringUtils.hasText(req.getMenu())) {
			res.setMessage(FoodRtnCode.MENU_REQUIRED.getMessage());
			return res;
		}

		//如果取得的price為null或小於0回傳一個價格錯誤的訊息
		if (req.getPrice() == null || req.getPrice() < 0) {
			res.setMessage(FoodRtnCode.PRICE_REQUIRED.getMessage());
			return res;
		}

		//如果取得的price為null或小於0或大於5，回傳一個評價錯誤的訊息
		if (req.getMenuEvaluate() == null || req.getMenuEvaluate() < 1 || req.getMenuEvaluate() > 5) {
			res.setMessage(FoodRtnCode.EVALUATE_REQUIRED.getMessage());
			return res;
		}

		//如果內容符合，將取得的name,menu,price,menuEvaluate放進result，並將儲存進foodDao
		Food result = new Food(name, req.getMenu(), req.getPrice(), req.getMenuEvaluate());
		foodDao.save(result);
		
		//使用impl的方法，如果店名為null，回傳一個找不到店名的錯誤訊息
		String str = foodService.changeMenuEvaluate(name);
		if (str == null) {
			res.setMessage("找不到店名，請重新輸入");
			return res;
		}
		
		//如果都符合，回傳result
		return new FoodRes(result);
	}

	@PostMapping(value = "/api/findStore")//第三題 搜尋特定城市找出所有的店家
	public FoodRes findStore(@RequestBody FoodReq req) {

		String city = req.getCity();
		int num = req.getNum();
		
		//創建一個 名為strList的陣列用來存放限制筆數的city
		List<String> strList = new ArrayList<>();
		strList = foodService.findStoreNameByCity(city, num);
		
		//如果strList為null，回傳一個城市名稱錯誤訊息
		if (strList == null) {
			res.setMessage(FoodRtnCode.CITY_REQUIRED.getMessage());
			res.setStoreInfoList(null);
			return res;
			
			//成功則回傳成功的訊息
		} else {
			res.setMessage(FoodRtnCode.SUCCESSFUL.getMessage());
			res.setStoreInfoList(strList);
			return res;
		}

	}

	@PostMapping(value = "/api/findStoreEvaluate")//第四題 搜尋店家評價在幾等以上的店家
	public FoodRes findStoreEvaluate(@RequestBody FoodReq req) {

		//如果取得的店家評價為null或是大於5、小於1，回傳一個評價錯誤的訊息
		if (req.getEvaluate() == null || req.getEvaluate() > 5 || req.getEvaluate() < 1) {
			res.setMessage(FoodRtnCode.EVALUATE_REQUIRED.getMessage());
			res.setStoreInfoList(null);
			return res;
		
			//成功則回傳成功訊息及店家資訊
		} else {
			res.setMessage(FoodRtnCode.SUCCESSFUL.getMessage());
			res.setStoreInfoList(foodService.findStoreEvaluate(req.getEvaluate()));
			return res;
		}

	}

	@PostMapping(value = "/api/ findStoreAndMenuEvaluate")// 第五題 搜尋店家評價在幾等以上的店家後，搜尋其餐點評價在幾等以上的餐點
	public FoodRes findStoreAndMenuEvaluate(@RequestBody FoodReq req) {

		//如果店家及餐點評價為null或是大於5、小於1，會回傳一個評價錯誤的訊息
		if (req.getEvaluate() == null || req.getEvaluate() > 5 || req.getEvaluate() < 1 
		   || req.getMenuEvaluate() == null|| req.getMenuEvaluate() > 5 || req.getMenuEvaluate() < 1) {
			res.setMessage(FoodRtnCode.EVALUATE_REQUIRED.getMessage());
			res.setStoreInfoList(null);
			return res;
		
		//如果符合則回傳成功訊息及店家資訊
		} else { 
			res.setMessage(FoodRtnCode.SUCCESSFUL.getMessage());
			res.setStoreInfoList(foodService.findStoreAndMenuEvaluate(req.getEvaluate(), req.getMenuEvaluate()));
			return res;
		}
	}
}
