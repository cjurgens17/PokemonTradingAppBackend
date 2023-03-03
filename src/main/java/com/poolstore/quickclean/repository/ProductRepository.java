package com.poolstore.quickclean.repository;

import com.poolstore.quickclean.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
