package com.example.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.entity.Client;

public interface IClientRepository extends PagingAndSortingRepository<Client, Long>{

	@Query("select c from Client c join fetch c.invoices l where c.id=?1")
	public Client fetchByIdWithInvoice(Long id);
	
}
