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

	@Override // �Ĥ@�D:�s�W
	public Store addstore(String city, String storename) {

		//�P�_��J���ө��W�١A�O�_�s�b���Ʈw
		if (storeDao.existsById(storename)) {  
			return null;//�w�s�b�h���^�ǡC
		}
		
		//�P�_city & storename �O�_���šC
		if (!StringUtils.hasText(city) || !StringUtils.hasText(storename)) {
			
			//�p�G���� �A�^��null
			return null;
		}
		
		//��city ��storename�]�w�i��Ʈw
		Store store = new Store();

		store.setCity(city);
		store.setStorename(storename);

		return storeDao.save(store);
	}

	@Override // �Ĥ@�D:�ק�
	public FoodRes changstore(String city, String store_name) {

		FoodRes res = new FoodRes();
		
		//�P�_��Ʈw���O�_����J�����W
		if (!storeDao.existsById(store_name)) {
			res.setMessage(FoodRtnCode.STORE_INEXISTED.getMessage());
			return res;
		}	
		
		Optional<Store> stoOp = storeDao.findById(store_name);
		
		//if (stoOp.isPresent()) {//�P�_49��Optional<Store>�O�_��(store_name)�ȡA���ө���T�C
			Store sto1 = stoOp.get();
			
			//�N�n�ק諸city�]�w�i�hsto1
			sto1.setCity(city);
			
			//�Nsto1�s�^��Ʈw
			storeDao.save(sto1);
			
			//���\�^�ǥ��T�T��
			res.setMessage(FoodRtnCode.SUCCESSFUL.getMessage());
			res.setFood(sto1);
			return res;
	}

	@Override // �ĤG�D:�s�W
	public FoodRes addmenu(String store, String menu, int price, int menuEvaluate) {
		FoodRes res = new FoodRes();
		
		//�P�_��J��store�Bmenu�O�_�s�b���Ʈw
		Optional<Food> sto = foodDao.findByStoreNameAndMenu(store, menu);
		Optional<Store> sto1 = storeDao.findById(store);
		if (!StringUtils.hasText(store) || !StringUtils.hasText(menu)) {
			res.setMessage(FoodRtnCode.NAME_REQUIRED.getMessage());
			return res;
		}
		
		//�P�_���a�O�_�s�b�A�Y���s�b�h�L�k�s�W�o���ө����\�I
		else if(!sto1.isPresent()) {
			res.setMessage("���a���s�b");
			return res;
			
		}

		//�P�_�\�I�O�_�s�b�A�Y�w�s�b�h�L�k�s�W�\�I
		else if (sto.isPresent()) {

			res.setMessage("�\�I�w�s�b");
			return res;
		}
		
		//�P�_����O�_��0�άO�t��
		else if (price + "" == null || price < 0 ) {
			res.setMessage(FoodRtnCode.PRICE_REQUIRED.getMessage());
			return res;
		}

		//�P�_�\�I�����O�_��1-5
		else if (menuEvaluate + "" == null || menuEvaluate < 1 || menuEvaluate > 5) {
			res.setMessage(FoodRtnCode.EVALUATE_REQUIRED.getMessage());
			return res;
		}

		//�N�n�s�W���\�I��T�]�w�i�hstore1
		Food store1 = new Food();
		store1.setStoreName(store);
		store1.setMenu(menu);
		store1.setPrice(price);
		store1.setMenu_evaluate(menuEvaluate);
		
		//�N�s�W�n���\�I�s�^��Ʈw
		foodDao.save(store1);
		
		//�N�s�W�n���\�I�s��res�A�æ^��
		res.setStore(store1);
		res.setMessage(FoodRtnCode.SUCCESSFUL.getMessage());
		return res;
	}

	@Override
	public String changeMenuEvaluate(String storeName) { // �ĤG�D �ק�
		
		//�P�_��Ʈw�O�_�s�b���ө���T
		Optional<Store> foodOp = storeDao.findById(storeName);
		if (!foodOp.isPresent()) {
			return null;
		}
		Store food = foodOp.get();
		
		//�N�o�������Ҧ��\�I�����p�⥭���A�����a����
		double value = 0;
		List<Food> list = foodDao.findListByStoreName(storeName);
		for (Food store : list) {
			value += store.getMenu_evaluate();
		}
		double result = value / list.size();
		food.setEvaluate(Math.floor(result * 10.0) / 10.0);
		
		//�N���a�����x�s�i��Ʈw
		storeDao.save(food);
		return "Success";

	}

	@Override//�ĤT�D �����䩱�a
	public List<String> findStoreNameByCity(String city, Integer num) {
		List<String> lis = new ArrayList<>();
		
		//�P�_city�O�_���ũάOnull
		if (!StringUtils.hasText(city)) {
			return null;
		}
		
		//�P�_��Ʈw�̬O�_�����������ө�
		List<Store> list = storeDao.findAllByCity(city);
		if (list.isEmpty()) {
			return null;
		}
		for (Store food : list) {
			List<Food> list2 = foodDao.findAllByStoreName(food.getStorename());
			for (Food sto : list2) {
				lis.add("���W: " + sto.getStoreName() + " ,���a����: " + food.getEvaluate() + " ,�\�I�W��: " + sto.getMenu() + " ,����: "
						+ sto.getPrice() + " ,�\�I����: " + sto.getMenu_evaluate());
			}

		}
		
		//�]�w�n��ܪ�����
		if (num > lis.size()) {
			return lis;
		}
		if (num == null || num == 0) {
			return lis;
		}

		return lis.subList(0, num);

	}
	
	@Override//�ĥ|�D ���a�����䩱�a
	public List<String> findStoreEvaluate(double evaluate) {

		List<String> list = new ArrayList<>();

		//�M���Ʈw���A����ڭ̳]�w�����a�������ө���T�A�åΩ��a��������Ƨ�
		List<Store> list1 = storeDao.findByEvaluateGreaterThanEqualOrderByEvaluateDesc(evaluate);
		
		//��C�@���ө����\�I��T�A�s���r���A�[�i�hlist��
		for (Store store : list1) {
			List<Food> list2 = foodDao.findAllByStoreName(store.getStorename());
			for (Food food : list2) {
				list.add("����: "+store.getCity() +" ,���W: " + food.getStoreName() + " ,���a����: " + store.getEvaluate() + " ,�\�I�W��: " + food.getMenu() + " ,����: "
						+ food.getPrice() + " ,�\�I����: " + food.getMenu_evaluate());

			}
		}
		return list;

	}

	@Override//�Ĥ��D ���a�������\�U��A�P�\�I�������\�I
	public List<String> findStoreAndMenuEvaluate(double evaluate, int menuEvaluate) {

		List<String> list = new ArrayList<>();
		
		//��X�\�I�����j��ڭ̳]�w���Ҧ��\�I�A�å��\�I��������Ƨ�
		List<Food> list2 = foodDao.findByMenuEvaluateGreaterThanEqualOrderByMenuEvaluateDesc(menuEvaluate);
		
		//��X���a�����j��ڭ̳]�w���Ҧ����a�A�åΩ��a��������Ƨ�
		List<Store> list1 = storeDao.findByEvaluateGreaterThanEqualOrderByEvaluateDesc(evaluate);
		
		//��C�@���ө����\�I��T�A�s���r���A�[�i�hlist��
		for (Store store : list1) {

			for (Food food : list2) {
				if (store.getStorename().equals(food.getStoreName())) {
					list.add("����: "+store.getCity()+" ,���W: " + store.getStorename() + " ,���a����: " + store.getEvaluate() + " ,�\�I�W��: " + food.getMenu()
							+ " ,����: " + food.getPrice() + " ,�\�I����: " + food.getMenu_evaluate());
				}
			}

		}
		return list;
	}
}