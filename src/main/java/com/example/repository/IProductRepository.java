package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.entity.Product;

public interface IProductRepository extends CrudRepository<Product, Long>{

	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
	List<Product> findByName(String term);
	
	List<Product> findByNameLikeIgnoreCase(String name);
	
}
