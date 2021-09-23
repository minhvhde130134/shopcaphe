package com.thodung.shopcaphe.model;

import java.sql.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;

@Entity
@Data
public class Orders {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Date orderDate= new Date(System.currentTimeMillis());
	private int orderStatus;
	private String fullName;
	private String address;
	private String phone;
	private String note;
	private int shippingFee;
	@Transient
	private boolean buyNow;
	@ManyToOne
	@JoinColumn(name="promotion_id")
	private Promotion promotionCode;
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
    private User user;
	@OneToMany(mappedBy = "order",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@Exclude
	private Set<OrderItem>orderItem;
}