package com.example.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.entity.User;

public interface IUserRepository extends CrudRepository<User, Long>{
	
	User findByUsername(String username);

}
