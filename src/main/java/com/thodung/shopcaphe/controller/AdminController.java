package com.thodung.shopcaphe.controller;

import java.util.List;
import java.util.Set;

import com.thodung.shopcaphe.model.Orders;
import com.thodung.shopcaphe.model.Promotion;
import com.thodung.shopcaphe.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/getPendingOrder")
    public ResponseEntity<?> getPendingOrder() {
        try {
            return new ResponseEntity<Set<Orders>>(adminService.getOrderByStatus(0), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllOrder")
    public ResponseEntity<?> getAllOrder() {
        try {
            return new ResponseEntity<List<Orders>>(adminService.getAllOrder(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getOrderDetail")
    public ResponseEntity<?> getOrderDetail(@RequestParam long id) {
        try {
            return new ResponseEntity<Orders>(adminService.getOrderDetail(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getShippingOrder")
    public ResponseEntity<?> getShippingOrder() {
        try {
            return new ResponseEntity<Set<Orders>>(adminService.getOrderByStatus(1), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/rejectOrder")
    public ResponseEntity<?> rejectOrder(@RequestParam long id) {
        try {
            adminService.updateStatus(id, -1);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/acceptOrder")
    public ResponseEntity<?> acceptOrder(@RequestParam long id) {
        try {
            adminService.updateStatus(id, 1);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/successOrder")
    public ResponseEntity<?> successOrder(@RequestParam long id) {
        try {
            adminService.updateStatus(id, 2);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cancelOrder")
    public ResponseEntity<?> cancelOrder(@RequestParam long id) {
        try {
            adminService.updateStatus(id, -2);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllPromotion")
    public ResponseEntity<?> getAllPromotion() {
        try {
            return new ResponseEntity<List<Promotion>>(adminService.getAllPromotion(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/removePromotion")
    public ResponseEntity<?> getAllPromotion(@RequestParam long id) {
        try {
            adminService.removePromotion(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createPromotion")
    public ResponseEntity<?> createPromotion(@RequestBody Promotion p) {
        try {
            return new ResponseEntity<Promotion>(adminService.createPromotion(p), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}