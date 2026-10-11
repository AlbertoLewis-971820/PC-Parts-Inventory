package com.albertolewis.pcpartsinventory.dto;

import com.albertolewis.pcpartsinventory.model.PartsCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class PcPartResponse {


    private final String name;

    private final PartsCategory category;

    private final String manufacturer;

    private final BigDecimal price;

    private final Integer quantity;

    private final Long id;

    public PcPartResponse(Long id, String name, PartsCategory category, String manufacturer, BigDecimal price, Integer quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.manufacturer = manufacturer;
        this.price = price;
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public PartsCategory getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }


}
