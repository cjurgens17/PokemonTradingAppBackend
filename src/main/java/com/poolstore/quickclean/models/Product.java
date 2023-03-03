package com.poolstore.quickclean.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="products")
public class Product extends BaseEntity {

    @Column(name="name")
    private String name;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="stock")
    private Boolean stock;

    @Column(name="price")
    private Double price;
}
