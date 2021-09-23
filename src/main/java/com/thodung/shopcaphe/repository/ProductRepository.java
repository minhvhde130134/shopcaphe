package com.thodung.shopcaphe.repository;

import java.util.Set;

import com.thodung.shopcaphe.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT id FROM product join user_product_liked on product.id=user_product_liked.product_id where user_id=?1", nativeQuery = true)
	Set<Long> findIdByLikedUsersId(long userId);

	boolean existsByIdAndLikedUsersId(long id,long userId);

	Set<Product> findByLikedUsersId(long userId);
    
}