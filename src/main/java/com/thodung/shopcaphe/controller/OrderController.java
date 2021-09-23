package com.thodung.shopcaphe.controller;

import java.util.Set;

import com.thodung.shopcaphe.model.Orders;
import com.thodung.shopcaphe.model.Promotion;
import com.thodung.shopcaphe.model.User;
import com.thodung.shopcaphe.security.UserPrincipal;
import com.thodung.shopcaphe.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/checkPromotion")
    public ResponseEntity<?> checkPromotion(@RequestParam String code) {
        try {
            return new ResponseEntity<Promotion>(orderService.checkPromotion(code), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody Orders order, Authentication authentication) {
        User u;
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            u= up.getUser();
        } catch (Exception e) {
            u = null;
        }
        orderService.createOrder(order, u);
        return new ResponseEntity<Promotion>(HttpStatus.OK);
    }

    @GetMapping("/getMyOrder")
    public ResponseEntity<?> getMyOrder(Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            return new ResponseEntity<Set<Orders>>(orderService.getMyOrder(u), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}