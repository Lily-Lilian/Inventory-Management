package com.stockmanagement.stockmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SellItemRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Quantity to sell is required")
    private Integer quantityToSell;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantityToSell() {
        return quantityToSell;
    }

    public void setQuantityToSell(Integer quantityToSell) {
        this.quantityToSell = quantityToSell;
    }
}

