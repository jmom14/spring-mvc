package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.IClientService;
import com.example.view.xml.ClientList;

@RestController
@RequestMapping(value = "/api/clients")
public class ClientRestController {

	@Autowired
	private IClientService clientService;

	@GetMapping(value =  "/list")
	public ClientList list() {
		return  new ClientList(clientService.findAll());
	}
	
}
