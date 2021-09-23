package com.thodung.shopcaphe.repository;

import java.util.Optional;

import com.thodung.shopcaphe.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	Optional<User> findByEmail(String email);
	@Query(value = "select * from user where user.id=?1",nativeQuery = true)
	User getInfoById(long id);
    
}