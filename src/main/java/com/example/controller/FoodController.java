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

	@PostMapping(value = "/api/addstore") // �Ĥ@�D:�s�W
	public FoodRes addstore(@RequestBody FoodReq req) {

		Store store = foodService.addstore(req.getCity(), req.getStore_name());

		//�p�G���šA�|�^�Ǥ@�ӦW�٬��Ū��T��
		if (store == null) {
			res.setMessage(FoodRtnCode.NAME_REQUIRED.getMessage());
			return res;
		}
		
		//�^���x�s�n���ө���T
		return new FoodRes(store);
	}

	@PostMapping(value = "/api/changestore") // �Ĥ@�D:�ק�  ��s�ө���T
	public FoodRes changestore(@RequestBody FoodReq req) {

		//�P�_�ө��W�٬O�_���ŭ�NULL
		if(!StringUtils.hasText(req.getStore_name())) {
			res.setMessage(FoodRtnCode.NAME_REQUIRED.getMessage());
			return res;
		}

		//�P�_city�O�_���ŭȩ�NULL
		if (!StringUtils.hasText(req.getCity())) {
			res.setMessage(FoodRtnCode.CITY_REQUIRED.getMessage());
			return res;
		}

		//�ϥ�impl����k�æ^��res
		return foodService.changstore(req.getCity(), req.getStore_name());
	}

	@PostMapping(value = "/api/addmenu")//�ĤG�D �s�W�\�I
	public FoodRes addmenu(@RequestBody FoodReq req) {
		
		FoodRes store = foodService.addmenu(req.getStore(), req.getMenu(), req.getPrice(), req.getMenuEvaluate());
		
		//�p�Gstore����,�^�Ǥ@�ӪŰT���C
		if (store == null) {
			res.setMessage(FoodRtnCode.MENU_REQUIRED.getMessage());
			return res;
		}
		
		//�p�Gstore���Ȫ���,�^��store
		return store;
	}

	@PostMapping(value = "/api/changeMenuEvaluate")//�ĤG�D �ק��\�I�����A�íק况�a����
	public FoodRes changeMenuEvaluate(@RequestBody FoodReq req) {
		
		String name = req.getStore_name();
		
		//�p�Gname��null�ΪŦ^�Ǥ@��name���ର�Ū����~�T��
		if (name == null || !StringUtils.hasText(name)) {
			res.setMessage(FoodRtnCode.NAME_REQUIRED.getMessage());
			return res;
		}

		//�p�G���o��menu��null�ΪŦ^�Ǥ@��menu���ର�Ū����~�T��
		if (req.getMenu() == null || !StringUtils.hasText(req.getMenu())) {
			res.setMessage(FoodRtnCode.MENU_REQUIRED.getMessage());
			return res;
		}

		//�p�G���o��price��null�Τp��0�^�Ǥ@�ӻ�����~���T��
		if (req.getPrice() == null || req.getPrice() < 0) {
			res.setMessage(FoodRtnCode.PRICE_REQUIRED.getMessage());
			return res;
		}

		//�p�G���o��price��null�Τp��0�Τj��5�A�^�Ǥ@�ӵ������~���T��
		if (req.getMenuEvaluate() == null || req.getMenuEvaluate() < 1 || req.getMenuEvaluate() > 5) {
			res.setMessage(FoodRtnCode.EVALUATE_REQUIRED.getMessage());
			return res;
		}

		//�p�G���e�ŦX�A�N���o��name,menu,price,menuEvaluate��iresult�A�ñN�x�s�ifoodDao
		Food result = new Food(name, req.getMenu(), req.getPrice(), req.getMenuEvaluate());
		foodDao.save(result);
		
		//�ϥ�impl����k�A�p�G���W��null�A�^�Ǥ@�ӧ䤣�쩱�W�����~�T��
		String str = foodService.changeMenuEvaluate(name);
		if (str == null) {
			res.setMessage("�䤣�쩱�W�A�Э��s��J");
			return res;
		}
		
		//�p�G���ŦX�A�^��result
		return new FoodRes(result);
	}

	@PostMapping(value = "/api/findStore")//�ĤT�D �j�M�S�w������X�Ҧ������a
	public FoodRes findStore(@RequestBody FoodReq req) {

		String city = req.getCity();
		int num = req.getNum();
		
		//�Ыؤ@�� �W��strList���}�C�ΨӦs�񭭨�ƪ�city
		List<String> strList = new ArrayList<>();
		strList = foodService.findStoreNameByCity(city, num);
		
		//�p�GstrList��null�A�^�Ǥ@�ӫ����W�ٿ��~�T��
		if (strList == null) {
			res.setMessage(FoodRtnCode.CITY_REQUIRED.getMessage());
			res.setStoreInfoList(null);
			return res;
			
			//���\�h�^�Ǧ��\���T��
		} else {
			res.setMessage(FoodRtnCode.SUCCESSFUL.getMessage());
			res.setStoreInfoList(strList);
			return res;
		}

	}

	@PostMapping(value = "/api/findStoreEvaluate")//�ĥ|�D �j�M���a�����b�X���H�W�����a
	public FoodRes findStoreEvaluate(@RequestBody FoodReq req) {

		//�p�G���o�����a������null�άO�j��5�B�p��1�A�^�Ǥ@�ӵ������~���T��
		if (req.getEvaluate() == null || req.getEvaluate() > 5 || req.getEvaluate() < 1) {
			res.setMessage(FoodRtnCode.EVALUATE_REQUIRED.getMessage());
			res.setStoreInfoList(null);
			return res;
		
			//���\�h�^�Ǧ��\�T���Ω��a��T
		} else {
			res.setMessage(FoodRtnCode.SUCCESSFUL.getMessage());
			res.setStoreInfoList(foodService.findStoreEvaluate(req.getEvaluate()));
			return res;
		}

	}

	@PostMapping(value = "/api/ findStoreAndMenuEvaluate")// �Ĥ��D �j�M���a�����b�X���H�W�����a��A�j�M���\�I�����b�X���H�W���\�I
	public FoodRes findStoreAndMenuEvaluate(@RequestBody FoodReq req) {

		//�p�G���a���\�I������null�άO�j��5�B�p��1�A�|�^�Ǥ@�ӵ������~���T��
		if (req.getEvaluate() == null || req.getEvaluate() > 5 || req.getEvaluate() < 1 
		   || req.getMenuEvaluate() == null|| req.getMenuEvaluate() > 5 || req.getMenuEvaluate() < 1) {
			res.setMessage(FoodRtnCode.EVALUATE_REQUIRED.getMessage());
			res.setStoreInfoList(null);
			return res;
		
		//�p�G�ŦX�h�^�Ǧ��\�T���Ω��a��T
		} else { 
			res.setMessage(FoodRtnCode.SUCCESSFUL.getMessage());
			res.setStoreInfoList(foodService.findStoreAndMenuEvaluate(req.getEvaluate(), req.getMenuEvaluate()));
			return res;
		}
	}
}
