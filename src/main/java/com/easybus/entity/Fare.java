package com.easybus.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Fare.java
@Entity
@Table(name = "fare")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fare {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private Double price;
 private String currency;

 @ManyToOne
 @JoinColumn(name = "bus_id")
 private Bus bus;
}
