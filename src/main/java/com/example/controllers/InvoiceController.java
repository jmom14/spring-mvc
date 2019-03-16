package com.example.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Client;
import com.example.entity.Invoice;
import com.example.entity.InvoiceItem;
import com.example.entity.Product;
import com.example.service.IClientService;

@Controller
@RequestMapping("/invoice")
@SessionAttributes("invoice")
public class InvoiceController {

	@Autowired
	private IClientService clientService;

	@GetMapping(value = "/view/{id}")
	public String view(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Invoice invoice = clientService.fetchInvoiceById(id);
				//clientService.findInvoiceById(id);

		if (invoice == null) {
			flash.addFlashAttribute("error", "Invoice does not exist in the database");
			return "redirect: /list";
		}

		model.addAttribute("invoice", invoice);
		model.addAttribute("title", "Invoice: ".concat(invoice.getDescription()));
		return "invoice/view";
	}

	@GetMapping(value = "/form/{clientId}")
	public String create(@PathVariable(value = "clientId") Long id, Map<String, Object> model,
			RedirectAttributes flash) {

		Client client = clientService.findOne(id);

		if (client == null) {
			flash.addFlashAttribute("error", "Client does not exist in the database");
			return "redirect:/list";
		}

		Invoice invoice = new Invoice();
		invoice.setClient(client);
		model.put("invoice", invoice);
		model.put("title", "Create invoice");

		return "invoice/form";
	}

	@GetMapping(value = "/load-products/{term}", produces = { "application/json" })
	public @ResponseBody List<Product> loadProducts(@PathVariable String term) {
		return clientService.findByName(term);
	}

	@PostMapping(value = "/form")
	public String save(@Valid Invoice invoice, BindingResult result, Model model,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "quantity[]", required = false) Integer[] quantity, RedirectAttributes flash,
			SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("title", "Create invoice");
			return "invoice/form";
		}
		if (itemId == null || itemId.length == 0) {
			model.addAttribute("title", "Create invoice");
			model.addAttribute("error", "Error: La factura NO puede no tener líneas!");
			return "invoice/form";
		}
		for (int i = 0; i < itemId.length; i++) {

			Product product = clientService.findProductById(itemId[i]);
			InvoiceItem line = new InvoiceItem();
			line.setQuantity(quantity[i]);
			line.setProduct(product);
			invoice.addInvoiceItem(line);

		}
		clientService.saveInvoice(invoice);
		status.setComplete();
		flash.addFlashAttribute("success", "Invoice created successfully");

		return "redirect:/view/" + invoice.getClient().getId();
	}

	@GetMapping(value = "/delete/{id}")
	public String delete(@PathVariable(value = "id")Long id, RedirectAttributes flash) {
		
		Invoice invoice = clientService.findInvoiceById(id);
		
		if(invoice != null) {
			clientService.deleteInvoice(id);
			flash.addFlashAttribute("success","Invoice deleted successfully");
			return "redirect:/view/" + invoice.getClient().getId();
		}
		flash.addFlashAttribute("error","Invoice does not exist in the database");
		
		return "redirect:/list";

	}

}
