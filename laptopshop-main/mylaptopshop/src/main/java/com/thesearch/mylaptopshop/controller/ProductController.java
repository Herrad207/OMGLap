package com.thesearch.mylaptopshop.controller;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.thesearch.mylaptopshop.dto.ProductDto;
import com.thesearch.mylaptopshop.exception.AlreadyExistException;
import com.thesearch.mylaptopshop.exception.ResourceNotFoundException;
import com.thesearch.mylaptopshop.model.Product;
import com.thesearch.mylaptopshop.model.ProductAttribute;
import com.thesearch.mylaptopshop.repository.ProductAttributeRepository;
import com.thesearch.mylaptopshop.request.AddProductRequest;
import com.thesearch.mylaptopshop.request.ProductUpdateRequest;
import com.thesearch.mylaptopshop.response.ApiResponse;
import com.thesearch.mylaptopshop.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
@CrossOrigin(origins ="http://127.0.0.1:5500/")

public class ProductController {

    private final IProductService productService;
    private final ProductAttributeRepository productAttributeRepository;

    @GetMapping("/all")
    public List<ProductDto> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return convertedProducts;
    }

    @GetMapping("{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId){
        try {
            Product product = productService.getProductById(productId);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("success",productDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product){
        try{
            Product theProduct = productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Add Product Success!", theProduct));
        }catch(AlreadyExistException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request, @PathVariable Long productId){
        try {
            Product theProduct = productService.updateProduct(request, productId);
            return ResponseEntity.ok(new ApiResponse("Update Product Success!",theProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null)); 
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct (@PathVariable Long productId){
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Delete Product Success!",productId));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName){
        try{
            List<Product> products = productService.getProductByBrandAndName(brandName, productName);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Product Found!",null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success!", convertedProducts));
        } catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/by/brand-and-category")
    public ResponseEntity<ApiResponse> getProductByBrandAndCategory(@RequestParam String brand, @RequestParam String category){
        try{
            List<Product> products = productService.getProductByBrandAndCategory(brand, category);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Product Found!",null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success!", convertedProducts));
        } catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error!",null));
        }
    }

    @GetMapping("/{name}/products")
    public List<ProductDto> getProductByName(@PathVariable String name){
            List<Product> products = productService.getProductByName(name);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return convertedProducts;
    }
    // public ResponseEntity<List<ProductDto>> getProductByName(@PathVariable String name) {
    //     List<Product> products = productService.getProductByName(name);
    //     List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
    //     return ResponseEntity.ok(convertedProducts);
    // }


    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> findProductByBrand(@RequestParam String brand){
        try {
            List<Product> products = productService.getProductByBrand(brand);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product Not Found!",null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success!",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> findProductByCategory(@PathVariable String category){
        try {
            List<Product> products = productService.getProductByCategory(category);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product Not Found!",null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductByBrandAndName(@RequestParam String brand, @RequestParam String name){
        try{
            var productCount = productService.countProductByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Product Count!",productCount));
        }catch(Exception e){
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }
    @PostMapping("/{productId}/attributes/add")
    public ResponseEntity<ApiResponse> addProductAttribute(@PathVariable Long productId,@RequestParam Long attributeId, @RequestParam String value){
        ProductAttribute productAttribute = productService.createProductAttribute(productId, attributeId, value);
        productAttributeRepository.save(productAttribute);
        return ResponseEntity.ok(new ApiResponse("Success",productAttribute));
    }
    @GetMapping("/{productId}/attributes/all")
    public ResponseEntity<ApiResponse> getAttributesByProductId(@PathVariable Long productId){
        return ResponseEntity.ok(new ApiResponse("Success",productService.getAttributesByProductId(productId)));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse> getFilterProducts(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice
    ) {
        try {
            List<Product> products = productService.filterProducts(category, minPrice, maxPrice);
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success",productDtos));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/filter/brand")
    public ResponseEntity<ApiResponse> getFilterProductsB(
        @RequestParam(required = false) String brand,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice
    ) {
        try {
            List<Product> products = productService.filterProductsB(brand, minPrice, maxPrice);
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success",productDtos));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/products")
    public List<ProductDto> getProductsByPage(@RequestParam int page, @RequestParam int size) {
        List<Product> products = productService.getProductsByPage(page, size);
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return convertedProducts;
    }
    
}
