package com.thodung.shopcaphe.service;

import java.util.Date;
import java.util.Set;

import com.thodung.shopcaphe.model.OrderItem;
import com.thodung.shopcaphe.model.Orders;
import com.thodung.shopcaphe.model.Promotion;
import com.thodung.shopcaphe.model.ShopcartItem;
import com.thodung.shopcaphe.model.User;
import com.thodung.shopcaphe.repository.OrdersRepository;
import com.thodung.shopcaphe.repository.PromotionRepository;
import com.thodung.shopcaphe.repository.ShopcartItemRepository;
import com.thodung.shopcaphe.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    OrdersRepository ordersRepository;
    @Autowired
    ShopcartItemRepository shopcartItemRepository;
    @Autowired
    PromotionRepository promotionRepository;
    @Autowired
    UserRepository userRepository;

    public Promotion checkPromotion(String code) throws Exception {
        Promotion p = promotionRepository.findByCode(code);
        if (new Date().after(p.getEndDate()) || (p.getMax() <= p.getUsed() && p.getMax()!=0)) {
            throw new Exception("Mã hết hạn hoặc hết lượt sử dụng");
        }
        return p;
    }

    public void createOrder(Orders order, User u) {
        if (u != null) {
            order.setUser(u);
            if (!order.isBuyNow()) {
                Set<ShopcartItem> si = shopcartItemRepository.findByUserId(u.getId());
                shopcartItemRepository.deleteAll(si);
            }
        }
        for (OrderItem item : order.getOrderItem()) {
            item.setPrice(item.getProduct().getPrice());
            item.setOrder(order);
        }
        ordersRepository.save(order);
    }

    public Set<Orders> getMyOrder(User u) {
        return ordersRepository.findByUserId(u.getId());
    }

}