package com.nic.nic_shop_task.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductCommentDto {
    private Long productId;
    private Integer rating;
    private String comment;
    private String imagePath;
}
