package com.crud_practice.api.crud_practice.repositories;

import com.crud_practice.api.crud_practice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepositories extends JpaRepository<Product, Integer> {

}
