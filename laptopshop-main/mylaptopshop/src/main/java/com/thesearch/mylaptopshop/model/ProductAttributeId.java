package com.thesearch.mylaptopshop.model;

import java.io.Serializable;
import java.util.Objects;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductAttributeId implements Serializable{
    private Long productId;
    private Long attributeId;

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof ProductAttributeId)) return false;
        ProductAttributeId that = (ProductAttributeId) o;
        return Objects.equals(productId, that.productId)&&
            Objects.equals(attributeId, that.attributeId);
    }
    @Override
    public int hashCode(){
        return Objects.hash(productId,attributeId);
    }
}
