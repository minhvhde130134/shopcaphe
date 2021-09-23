package com.thodung.shopcaphe.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class OrderItem {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="order_id")
	private Orders order;
	@OneToOne
	@JoinColumn(name="product_id")
	private Product product;
	private int quantity;
	private int price;
	@Column(columnDefinition = "nvarchar(255)")
	private String type;
}