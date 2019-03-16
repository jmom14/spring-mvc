package com.example.service;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dao.IClientDao;
import com.example.entity.Client;
import com.example.entity.Invoice;
import com.example.entity.Product;
import com.example.repository.IClientRepository;
import com.example.repository.IInvoiceRepository;
import com.example.repository.IProductRepository;

@Service
public class ClientService implements IClientService{
	
	@Autowired
	private IClientDao clientDao;
	
	@Autowired
	private IProductRepository productRepository;
	
	@Autowired
	private IClientRepository clienteRepository;
	
	@Autowired
	private IInvoiceRepository invoiceRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Client> findAll() {
		return clientDao.findAll();
	}

	@Override
	@Transactional
	public void save(Client client) {
		clientDao.save(client);		
	}

	@Override
	@Transactional(readOnly = true)
	public Client findOne(Long id) {
		return clientDao.findOne(id);
	}

	@Override
	@Transactional
	public void remove(Long id) {
		clientDao.remove(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Client> findAll(Pageable pageable) {
		return clienteRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Product> findByName(String term) {
		
		return productRepository.findByNameLikeIgnoreCase("%"+term+"%");
	}

	@Override
	@Transactional
	public void saveInvoice(Invoice invoice) {
		invoiceRepository.save(invoice);
	}

	@Override
	@Transactional(readOnly = true)
	public Product findProductById(Long id) {		
		return productRepository.findById(id).get();
	}

	@Override
	@Transactional(readOnly = true)
	public Invoice findInvoiceById(Long id) {
		return invoiceRepository.findById(id).get();
	}

	@Override
	@Transactional
	public void deleteInvoice(Long id) {
		invoiceRepository.deleteById(id);
	}

	@Override
	@Transactional
	public Invoice fetchInvoiceById(Long id) {
		
		return invoiceRepository.fetchById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Client fetchByIdWithInvoices(Long id) {
		
		return clienteRepository.fetchByIdWithInvoice(id);
	}

}
