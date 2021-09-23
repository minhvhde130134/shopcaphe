package com.thodung.shopcaphe.model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode.Exclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@JsonIgnore
	private String password;
	@Column(columnDefinition = "nvarchar(255)")
	private String fullName;
	private String avatarLink="https://live.staticflickr.com/65535/51297189262_36f1d24ba4_q.jpg";
	private Date dob;
	private String phone;
	private String email;
	private boolean emailVerified;
	private String role = "ROLE_USER";

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@Exclude
	@JsonIgnore
	private Set<Address> addresses = new HashSet<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@Exclude
	@JsonIgnore
	private Set<Orders> orders;

	@Exclude
	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Set<Rating> rating;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@Exclude
	@JsonIgnore
	private Set<ShopcartItem> shopcart = new HashSet<>();

	@ManyToMany(mappedBy = "likedUsers")
	@Exclude
	@JsonIgnore
	private Set<Product> likedProducts;

	public User(String password, String fullName, String email) {
		this.password = password;
		this.fullName = fullName;
		this.email = email;
	}

	
}