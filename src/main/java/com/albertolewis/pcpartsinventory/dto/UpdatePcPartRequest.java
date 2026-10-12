package com.albertolewis.pcpartsinventory.dto;

import com.albertolewis.pcpartsinventory.model.PartsCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class UpdatePcPartRequest {

    @NotBlank
    private String name;

    @NotNull
    private PartsCategory category;

    @NotBlank
    private String manufacturer;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    public UpdatePcPartRequest() {
    }

    public UpdatePcPartRequest(String name, PartsCategory category, String manufacturer, BigDecimal price, Integer quantity) {
        this.name = name;
        this.category = category;
        this.manufacturer = manufacturer;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PartsCategory getCategory() {
        return category;
    }

    public void setCategory(PartsCategory category) {
        this.category = category;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
