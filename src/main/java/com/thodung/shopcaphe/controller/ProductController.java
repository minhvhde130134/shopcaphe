package com.thodung.shopcaphe.controller;

import java.util.List;
import java.util.Set;

import com.thodung.shopcaphe.model.Product;
import com.thodung.shopcaphe.model.ShopcartItem;
import com.thodung.shopcaphe.model.User;
import com.thodung.shopcaphe.security.UserPrincipal;
import com.thodung.shopcaphe.service.ProductService;

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
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        try {
            return new ResponseEntity<List<Product>>(productService.getAllProducts(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getRating")
    public ResponseEntity<?> getRating(@RequestParam long id) {
        try {
            return new ResponseEntity<List<Product>>(productService.getAllProducts(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/checkLiked")
    public ResponseEntity<?> checkLiked(@RequestParam long id, Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            return new ResponseEntity<>(productService.checkLiked(id, u), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/updateLike")
    public ResponseEntity<?> updateLike(@RequestParam long id, @RequestParam boolean status,
            Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            productService.updateLike(id, status, u);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getLiked")
    public ResponseEntity<?> getRating(Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            return new ResponseEntity<Set<Product>>(productService.getLiked(u.getId()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/addToCart", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> addToCart(@RequestParam long id, @RequestParam String type, @RequestParam int quantity,
            Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            productService.addToCart(id, u, type, quantity);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/removeCartItem")
    public ResponseEntity<?> removeCartItem(@RequestParam long id, @RequestParam String type,
            Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            productService.removeCartItem(id, type, u);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/asyncCart")
    public ResponseEntity<?> asyncCart(@RequestBody Set<ShopcartItem> shopcart, Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            productService.asyncCart(shopcart, u);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/updateCart")
    public ResponseEntity<?> updateCart(@RequestBody Set<ShopcartItem> shopcart, Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            productService.updateCart(shopcart, u.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}