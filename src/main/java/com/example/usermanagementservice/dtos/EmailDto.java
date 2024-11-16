package com.example.usermanagementservice.dtos;

import lombok.Data;

@Data
public class EmailDto {
    private String fromEmail;
    private String toEmail;
    private String subject;
    private String body;
}
