package com.thodung.shopcaphe.service;

import java.util.List;
import java.util.Set;

import com.thodung.shopcaphe.model.Orders;
import com.thodung.shopcaphe.model.Promotion;
import com.thodung.shopcaphe.repository.OrdersRepository;
import com.thodung.shopcaphe.repository.PromotionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
	@Autowired
	private OrdersRepository ordersRepository;
	@Autowired
	private PromotionRepository promotionRepository;

	public Set<Orders> getOrderByStatus(int status) {
		return ordersRepository.findByOrderStatusOrderById(status);
	}
	public List<Orders> getAllOrder() {
		return ordersRepository.findAll();
	}

	public Orders getOrderDetail(long id) {
		return ordersRepository.findById(id).get();
	}

	public void updateStatus(long id,int status) {
		Orders o = ordersRepository.findById(id).get();
		o.setOrderStatus(status);
		ordersRepository.save(o);
	}
	public List<Promotion> getAllPromotion() {
		return promotionRepository.findAll();
	}
	public Promotion createPromotion(Promotion p) {
		return promotionRepository.save(p);
	}
	public void removePromotion(long id) {
		promotionRepository.deleteById(id);
	}

}