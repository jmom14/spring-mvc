package com.example.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.entity.Client;
import com.example.entity.Invoice;
import com.example.entity.Product;

public interface IClientService {
	
	List<Client> findAll();
	
	Page<Client> findAll(Pageable pageable);
	
	void save(Client client);
	
	Client findOne(Long id);
	
	Client fetchByIdWithInvoices(Long id);
	
	void remove(Long id);
	
	List<Product> findByName(String term);
	
	void saveInvoice(Invoice invoice);
	
	Product findProductById(Long id);
	
	Invoice findInvoiceById(Long id);
	
	void deleteInvoice(Long id);
	
	Invoice fetchInvoiceById(Long id);
	
	

}
