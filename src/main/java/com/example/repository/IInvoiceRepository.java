package com.example.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.entity.Invoice;

public interface IInvoiceRepository extends CrudRepository <Invoice, Long> {
	
	@Query("select i from Invoice i join fetch i.client c join fetch i.items l join fetch l.product where i.id = ?1")
	Invoice fetchById(Long id);

}
