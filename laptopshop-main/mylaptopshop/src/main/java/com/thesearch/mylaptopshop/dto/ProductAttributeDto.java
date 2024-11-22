package com.thesearch.mylaptopshop.dto;

import lombok.Data;

@Data
public class ProductAttributeDto {
    private Long attributeId;
    private String attributeName;
    private String value;
}
