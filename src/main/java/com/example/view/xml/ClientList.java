package com.example.view.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.example.entity.Client;

@XmlRootElement(name = "clients")
public class ClientList {

	@XmlElement(name = "client")
	private List<Client> clients;

	public ClientList() {}

	public ClientList(List<Client> clients) {
		this.clients = clients;
	}

	public List<Client> getClients() {
		return clients;
	}
	
	
	
	
	
	
	
}
