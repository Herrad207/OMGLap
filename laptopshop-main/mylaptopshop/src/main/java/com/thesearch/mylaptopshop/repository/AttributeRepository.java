package com.thesearch.mylaptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thesearch.mylaptopshop.model.Attribute;

public interface AttributeRepository  extends JpaRepository<Attribute, Long>{
    boolean existsByName(String name);
}
