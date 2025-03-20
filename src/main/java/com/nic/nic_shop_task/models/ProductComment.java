package com.nic.nic_shop_task.models;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "product_comments")
public class ProductComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer rating;

    @Column(length = 4055)
    private String comment;

    private String imagePath;
}
