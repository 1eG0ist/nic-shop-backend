package com.nic.nic_shop_task.models;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_cells")
public class OrderCell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "product_id")
    private Long productId;

    private int count;
}
