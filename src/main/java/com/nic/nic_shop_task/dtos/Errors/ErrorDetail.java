package com.nic.nic_shop_task.dtos.Errors;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ErrorDetail {
    private String title;
    private int status;
    private String detail;
    private List<String> errors;
}
