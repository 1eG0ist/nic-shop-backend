package com.nic.nic_shop_task.services;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
