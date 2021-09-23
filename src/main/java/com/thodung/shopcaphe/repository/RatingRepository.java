package com.thodung.shopcaphe.repository;

import java.util.List;

import javax.persistence.OrderBy;

import com.thodung.shopcaphe.model.Rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long>{
	@OrderBy("createDate")
	List<Rating> findAllByProductId(long id);

	List<Rating> findAllByProductIdOrderByCreateDateDesc(long id);

	Rating findByProductIdAndUserId(long id, long userId);
    
}