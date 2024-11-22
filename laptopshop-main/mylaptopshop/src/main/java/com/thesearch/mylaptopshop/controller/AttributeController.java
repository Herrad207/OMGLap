package com.thesearch.mylaptopshop.controller;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thesearch.mylaptopshop.exception.AlreadyExistException;
import com.thesearch.mylaptopshop.exception.ResourceNotFoundException;
import com.thesearch.mylaptopshop.model.Attribute;
import com.thesearch.mylaptopshop.response.ApiResponse;
import com.thesearch.mylaptopshop.service.attribute.IAttributeService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/attributes")
public class AttributeController {

    private final IAttributeService attributeService;
    
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllAttributes(){
        try {
            List<Attribute> attributes = attributeService.getAllAttributes();
            return ResponseEntity.ok(new ApiResponse("Success",attributes));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addAttribute(@RequestBody Attribute name){
        try {
            Attribute attribute = attributeService.addAttribute(name);
            return ResponseEntity.ok(new ApiResponse("Success",attribute));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @DeleteMapping("/attribute/{id}/delete")
    public ResponseEntity<ApiResponse> deleteAttribute(@PathVariable Long id){
        try {
            attributeService.deleteAttribute(id);
            return ResponseEntity.ok(new ApiResponse("Delete Success!",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PutMapping("/attribute/{id}/update")
    public ResponseEntity<ApiResponse> updateAttribute(@PathVariable Long id,@RequestBody Attribute attribute){
        try {
            Attribute updateAttribute = attributeService.updateAttribute(attribute, id);
            return ResponseEntity.ok(new ApiResponse("Update Success!",updateAttribute));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
