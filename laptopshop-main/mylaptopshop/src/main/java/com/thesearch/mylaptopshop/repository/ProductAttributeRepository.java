package com.thesearch.mylaptopshop.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thesearch.mylaptopshop.model.ProductAttribute;
import com.thesearch.mylaptopshop.model.ProductAttributeId;


public interface  ProductAttributeRepository extends JpaRepository<ProductAttribute, ProductAttributeId>{
    Set<ProductAttribute> findByProductId(Long productId);

    boolean existsByProductIdAndAttributeId(long productId, long attributeId);
}
