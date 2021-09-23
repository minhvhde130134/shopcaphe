package com.thodung.shopcaphe.repository;

import java.util.Set;

import com.thodung.shopcaphe.model.Orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Set<Orders> findByUserId(long id);
	Set<Orders> findByOrderStatusOrderById(int i);

}