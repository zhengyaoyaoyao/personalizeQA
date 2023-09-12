package com.personalize.personalizeqa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    public String name;
    public String sku;
    public int price;
}
