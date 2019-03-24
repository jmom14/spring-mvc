package com.example.view.csv;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.example.entity.Client;

@Component("list")
public class ClientCsvView extends AbstractView{

	
	
	public ClientCsvView() {
		setContentType("text/csv");
	}

	@Override
	protected boolean generatesDownloadContent() {
		// TODO Auto-generated method stub
		return super.generatesDownloadContent();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setHeader("Content-Disposition", "atachment; filename = \"clientes.csv\"");
		response.setContentType(getContentType());
		
		Page<Client> clients = (Page<Client>) model.get("clients");
	
		ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(),  CsvPreference.STANDARD_PREFERENCE);
		
		String[] header = {"id", "name", "lastName", "email", "createAt"};
		beanWriter.writeHeader(header);
		
		for(Client cliente: clients) {
			beanWriter.write(cliente, header);
		}
		
		beanWriter.close();
		
	}
	
	

}
