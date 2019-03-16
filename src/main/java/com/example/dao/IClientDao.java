package com.example.dao;

import java.util.List;

import com.example.entity.Client;

public interface IClientDao {
	
	List<Client> findAll();
	
	void save(Client client);
	
	Client findOne(Long id);
	
	void remove(Long id);

}
