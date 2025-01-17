package com.nic.nic_shop_task.models;

import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "product_comments")
public class ProductComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdDate;
    @OneToOne(mappedBy = "userId", orphanRemoval = true)
    private User user;
    @OneToOne(mappedBy = "productId", orphanRemoval = true)
    private Product product;
    private Integer rating;
    @Column(length = 4055)
    private String comment;
    private String imagePath;
}
