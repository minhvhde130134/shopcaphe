package com.thodung.shopcaphe.repository;

import java.util.Set;

import com.thodung.shopcaphe.model.ShopcartItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopcartItemRepository extends JpaRepository<ShopcartItem, Long> {
	Set<ShopcartItem> findByUserId(long id);
    
}