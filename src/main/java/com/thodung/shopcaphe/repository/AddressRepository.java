package com.thodung.shopcaphe.repository;

import java.util.Set;

import com.thodung.shopcaphe.model.Address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

	Set<Address> findByUserId(long id);
    
}