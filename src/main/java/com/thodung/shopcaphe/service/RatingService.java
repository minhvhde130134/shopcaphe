package com.thodung.shopcaphe.service;

import java.util.List;

import com.thodung.shopcaphe.model.Rating;
import com.thodung.shopcaphe.repository.ProductRepository;
import com.thodung.shopcaphe.repository.RatingRepository;
import com.thodung.shopcaphe.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Rating> getAllRatingOfProduct(long id, long userId) {
        List<Rating> rates = ratingRepository.findAllByProductIdOrderByCreateDateDesc(id);
        Rating rate = ratingRepository.findByProductIdAndUserId(id, userId);
        long rateId = rate == null ? 0 : rate.getId();
        rates.forEach(item -> {
            item.setFullName(item.getUser().getFullName());
            item.setAvatarLink(item.getUser().getAvatarLink());
            if (item.getId() == rateId) {
                item.setYourRate(true);
            }
        });
        return rates;
    }

    public void rating(long id, String content, int star, long userId) {
        Rating rated = ratingRepository.findByProductIdAndUserId(id, userId);
        boolean isRated =rated!=null;
        Rating r =isRated?rated: new Rating();
        r.setContent(content);
        r.setStar(star);
        if(!isRated){
            r.setProduct(productRepository.findById(id).get());
            r.setUser(userRepository.findById(userId).get());
        }
        ratingRepository.save(r);
	}
}