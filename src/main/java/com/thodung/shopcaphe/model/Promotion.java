package com.thodung.shopcaphe.model;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private Date endDate;
    private int discount;
    private int max;
    private int used;
    @OneToMany(mappedBy = "promotionCode", fetch = FetchType.LAZY)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
	private Set<Orders>orders;
}