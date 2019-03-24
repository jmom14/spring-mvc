package com.example.view.xml;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.example.entity.Client;

@Component("list.xml")
public class ClientListXmlView extends MarshallingView{

	
	@Autowired
	public ClientListXmlView(Jaxb2Marshaller marshaller) {
		super(marshaller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		model.remove("title");
		model.remove("page");
		
		Page<Client> clients = (Page<Client>) model.get("clients");
		model.remove("clients");
		
		model.put("clientsList", new ClientList(clients.getContent()));
		
		super.renderMergedOutputModel(model, request, response);
		
	}

	
}