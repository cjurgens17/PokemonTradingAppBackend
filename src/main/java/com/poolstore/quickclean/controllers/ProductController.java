package com.poolstore.quickclean.controllers;


import com.poolstore.quickclean.models.Product;
import com.poolstore.quickclean.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping({"product"})
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Product> getProductById(@PathVariable("id") int id){

        Optional<Product> optionalProduct = productService.findById(id);

        return ResponseEntity.ok(optionalProduct.get());
    }


}
