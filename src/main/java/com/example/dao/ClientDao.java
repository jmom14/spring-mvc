package com.example.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Client;


@Repository
public class ClientDao implements IClientDao{
	
	@PersistenceContext
	private EntityManager em;

	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Client> findAll() {
	
		return em.createQuery("from Client").getResultList();
	}

	@Override
	@Transactional
	public void save(Client client) {
		if(client.getId() != null && client.getId() > 0) {
			em.merge(client);
		}else{
			em.persist(client);
		}
		
	}
	@Transactional(readOnly = true)
	@Override
	public Client findOne(Long id) {		
		return em.find(Client.class, id);
	}
	
	@Transactional
	@Override
	public void remove(Long id) {
		em.remove(findOne(id));	
	}

}
