package com.example.conductordemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private String status;
}
