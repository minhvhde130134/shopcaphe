package com.thodung.shopcaphe.payload;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
    private String fullName;
}