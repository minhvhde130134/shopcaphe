package com.thodung.shopcaphe.model;


import java.util.Random;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "nvarchar(255)")
    private String name;
    private int price;    
    private int numSelled= new Random().nextInt(100);
    private boolean coffee; 
    private boolean type;
    private String mainImgLink;
    private boolean stocking =true;
    @Column(columnDefinition = "TEXT")
    private String description;
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    @Exclude
    @OrderBy
    private Set<Image> listImage;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @Exclude
    @JsonIgnore
    private Set<Rating> rating; 
    @ManyToMany(fetch = FetchType.LAZY)
    @Exclude
    @JsonIgnore
    @JoinTable(name = "user_product_liked", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likedUsers;
    
}