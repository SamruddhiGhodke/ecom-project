package com.example.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private UserDetailsEntity userDetailsEntity;

    @ManyToOne
    private ProductEntity productEntity;
    private Integer quantity;

    @Transient
    private Double totalPrice;

}
