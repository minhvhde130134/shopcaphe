package com.thodung.shopcaphe.service;

import java.util.List;
import java.util.Set;

import com.thodung.shopcaphe.model.Product;
import com.thodung.shopcaphe.model.ShopcartItem;
import com.thodung.shopcaphe.model.User;
import com.thodung.shopcaphe.repository.ProductRepository;
import com.thodung.shopcaphe.repository.ShopcartItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    // @Autowired
    // private UserRepository userRepository;
    @Autowired
    private ShopcartItemRepository shopcartItemRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll(Sort.by("numSelled"));
    }

    public Set<Product> getLiked(long userId) {
        return productRepository.findByLikedUsersId(userId);
    }

    public void addToCart(long id, User u, String type, int quantity) {
        Set<ShopcartItem> si = shopcartItemRepository.findByUserId(u.getId());
        boolean exist = false;
        for (ShopcartItem item : si) {
            if (item.getProduct().getId() == id && item.getType().equals(type)) {
                int q = item.getQuantity();
                item.setQuantity(q + quantity);
                exist = true;
            }
        }
        if (!exist) {
            Product p = productRepository.findById(id).get();
            ShopcartItem i = new ShopcartItem(u, p, quantity, type);
            si.add(i);
        }
        shopcartItemRepository.saveAll(si);
    }

    public void asyncCart(Set<ShopcartItem> shopcart, User u) {
        for (ShopcartItem item : shopcart) {
            item.setUser(u);
        }
        shopcartItemRepository.saveAll(shopcart);
    }

    public void updateCart(Set<ShopcartItem> shopcart, long userId) {
        Set<ShopcartItem> si = shopcartItemRepository.findByUserId(userId);
        for (ShopcartItem item : si) {
            for (ShopcartItem i : shopcart) {
                if (item.getProduct().getId() == i.getProduct().getId() && item.getType().equals(i.getType())) {
                    item.setQuantity(i.getQuantity());
                }
            }
        }
        shopcartItemRepository.saveAll(si);
    }

    private long getCartItemToRemove(long id, String type, User u) {
        Set<ShopcartItem> shopcart = shopcartItemRepository.findByUserId(u.getId());
        for (ShopcartItem item : shopcart) {
            if (item.getProduct().getId() == id && item.getType().equals(type)) {
                return item.getId();
            }
        }
        return -1;
    }

    public void removeCartItem(long id, String type, User u) {
        long itemId = getCartItemToRemove(id, type, u);
        if (itemId != -1) {
            shopcartItemRepository.deleteById(itemId);
        }
    }

    public boolean checkLiked(long id, User u) {
        return productRepository.existsByIdAndLikedUsersId(id, u.getId());
    }
    
    public void updateLike(long id, boolean status, User u) {
        Product p = productRepository.findById(id).get();
        Set<User> su = p.getLikedUsers();
        if (status) {
            su.add(u);
        } else {
            su.removeIf(user -> user.getId() == u.getId());
        }
        p.setLikedUsers(su);
        productRepository.save(p);
    }
}