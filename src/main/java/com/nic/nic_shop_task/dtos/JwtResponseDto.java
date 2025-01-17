package com.nic.nic_shop_task.dtos;

import com.nic.nic_shop_task.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponseDto {
    private String accessToken;
    private String refreshToken;
    private User user;
}

