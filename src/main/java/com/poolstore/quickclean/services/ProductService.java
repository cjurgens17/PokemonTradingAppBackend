package com.poolstore.quickclean.services;


import com.poolstore.quickclean.exceptions.NotFoundException;
import com.poolstore.quickclean.models.Product;
import com.poolstore.quickclean.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //Create custom methods once we get around to testing CRUD and find a need for actions outside of crudrepository.

    public Optional<Product> findById(int id){

        if(productRepository.findById((long) id).isEmpty()){
           throw new NotFoundException("Employee Not Found for ID value: "+ id);
        }
        return productRepository.findById((long) id);
    }

    public List<Product> findAllProducts(){
        List<Product> products = new ArrayList<>();

        productRepository.findAll().forEach(products::add);

        return products;
    }
}
