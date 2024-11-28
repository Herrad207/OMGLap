package com.thesearch.mylaptopshop.service.product;

import java.math.BigDecimal;
import java.util.List;

import com.thesearch.mylaptopshop.dto.ProductDto;
import com.thesearch.mylaptopshop.model.Product;
import com.thesearch.mylaptopshop.model.ProductAttribute;
import com.thesearch.mylaptopshop.request.AddProductRequest;
import com.thesearch.mylaptopshop.request.ProductUpdateRequest;

public interface IProductService {
    Product addProduct(AddProductRequest request);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest request, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductByCategory(String category);
    List<Product> getProductByBrand(String brand);
    List<Product> getProductByName(String name);
    List<Product> getProductByBrandAndCategory(String brand, String category);
    List<Product> getProductByBrandAndName(String brand, String name);
    List<Product> filterProducts(String category,BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> filterProductsB(String category, String brand,BigDecimal minPrice, BigDecimal maxPrice);
    Long countProductByBrandAndName(String brand, String name);
    List<ProductDto> getConvertedProducts(List<Product> products);
    ProductDto convertToDto(Product product);
    ProductAttribute createProductAttribute(Long productId, Long attributeId, String value);
    List<ProductAttribute> getAttributesByProductId(Long productId);
    List<Product> getProductsByPage(int page, int size);
    List<Product> getProductsByCategoryAndPage(String category, int page, int size);
    
}
