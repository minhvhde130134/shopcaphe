package com.thodung.shopcaphe.payload;

import java.util.Set;

import com.thodung.shopcaphe.model.Address;
import com.thodung.shopcaphe.model.ShopcartItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
public class LoginResponse {
	private String accessToken;
    private String fullName;
    private String phone;
    private String avatarLink;
    private boolean emailVerify;
    private String role;
    @EqualsAndHashCode.Exclude
    private Set<ShopcartItem> shopcart;
    @EqualsAndHashCode.Exclude
    private Set<Address> address;
    
    
}