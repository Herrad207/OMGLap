package com.thesearch.mylaptopshop.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thesearch.mylaptopshop.model.Product;


public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findByCategoryName(String category);
    List<Product> findByBrand(String brand);
    List<Product> findByBrandAndCategoryName(String brand,String category);
    List<Product> findByName(String name);
    List<Product> findByBrandAndName(String brand,String name);
    Long countByBrandAndName(String brand,String name);
    boolean existsByNameAndBrand(String name,String brand);

    @Query("SELECT p FROM Product p Where "
            +"(:category IS NULL OR p.category.name = :category) AND "
            +"(:minPrice IS NULL OR p.price >= :minPrice) AND "
            +"(:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> findByFilter(
        @Param("category") String category,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice
    );

    @Query("SELECT p FROM Product p Where "
            +"(:brand IS NULL OR p.brand = :brand) AND "
            +"(:minPrice IS NULL OR p.price >= :minPrice) AND "
            +"(:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> findByFilterB(
        @Param("brand") String brand,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice
    );
}
