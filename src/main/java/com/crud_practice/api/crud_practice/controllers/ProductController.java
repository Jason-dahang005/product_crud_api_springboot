package com.crud_practice.api.crud_practice.controllers;

import com.crud_practice.api.crud_practice.models.Product;
import com.crud_practice.api.crud_practice.models.ProductDto;
import com.crud_practice.api.crud_practice.repositories.ProductsRepositories;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductsRepositories repo;

    @GetMapping
    public List<Product> getProducts(){
        return repo.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getProduct(@PathVariable int id){
        Product product = repo.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(
            @Valid @RequestBody ProductDto productDto,
            BindingResult result
            ) {

        double price = 0;

        try {
            price = Double.parseDouble(productDto.getPrice());
        } catch (Exception e) {
            result.addError(new FieldError("productDto", "price", "The price should be a number"));
        }

        if (result.hasErrors()) {
            var errorLst = result.getAllErrors();
            var errorMap = new HashMap<String, String>();

            for(int i = 0; i < errorLst.size(); i++) {
                var error = (FieldError) errorLst.get(i);
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body((errorMap));
        }

        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());
        product.setPrice(price);
        product.setCreatedAt(new Date());

        repo.save(product);

        return ResponseEntity.ok(product);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateProduct(
            @PathVariable int id,
            @Valid @RequestBody ProductDto productDto,
            BindingResult result
    ) {
        Product product = repo.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        double price = 0;
        try {
            price = Double.parseDouble(productDto.getPrice());
        } catch (Exception ex){
            result.addError(new FieldError("productDto", "price", "the price should be a number"));
        }

        if (result.hasErrors()) {
            var errorLst = result.getAllErrors();
            var errorMap = new HashMap<String, String>();

            for(int i = 0; i < errorLst.size(); i++) {
                var error = (FieldError) errorLst.get(i);
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body((errorMap));
        }

        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(price);
        product.setDescription(productDto.getDescription());

        repo.save(product);

     return ResponseEntity.ok(product);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable int id){
        Product product = repo.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        repo.delete(product);

        return ResponseEntity.ok().build();
    }
}
