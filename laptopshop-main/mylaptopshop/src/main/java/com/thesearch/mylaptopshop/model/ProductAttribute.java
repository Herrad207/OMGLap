package com.thesearch.mylaptopshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "product_attribute")
@Entity
@IdClass(ProductAttributeId.class)
@Getter
@Setter
public class ProductAttribute {
    @Id
    private Long productId;

    @Id
    private Long attributeId;

    private String value;

    @ManyToOne
    @JoinColumn(name = "productId", insertable= false, updatable= false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "attributeId",insertable= false , updatable= false)
    private Attribute attribute;

}
