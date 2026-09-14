package com.albertolewis.pcpartsinventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RestockRequest {




    @NotNull
    @Positive
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


}
