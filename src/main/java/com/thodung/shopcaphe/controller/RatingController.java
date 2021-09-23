package com.thodung.shopcaphe.controller;

import java.util.List;

import com.thodung.shopcaphe.model.Rating;
import com.thodung.shopcaphe.model.User;
import com.thodung.shopcaphe.security.UserPrincipal;
import com.thodung.shopcaphe.service.RatingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/rate")
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @GetMapping("/getRate")
    public ResponseEntity<?> getAllRatingOfProduct(@RequestParam long id, Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            return new ResponseEntity<List<Rating>>(ratingService.getAllRatingOfProduct(id, u.getId()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/rating")
    public ResponseEntity<?> ratingProduct(@RequestParam long id, @RequestParam String content, @RequestParam int star,
            Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            ratingService.rating(id, content, star, u.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}