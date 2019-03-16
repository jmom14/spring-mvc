package com.example.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Client;
import com.example.service.IClientService;
import com.example.service.IUploadFileService;
import com.example.util.paginator.PageRender;

@Controller
@SessionAttributes("client")
public class ClientController {

	@Autowired
	private IClientService clientService;

	@Autowired
	private IUploadFileService uploadFileService;

	protected final Log logger = LogFactory.getLog(this.getClass());

	@GetMapping(value = { "/list", "/" })
	public String list(@RequestParam(value = "page", defaultValue = "0") int page, Model model, Authentication auth) {

		Authentication auth1 = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			logger.info("Hi, ".concat(auth.getName()).concat("!"));
		}
		
		if(hasRole("ROLE_ADMIN")) {
			logger.info("You " + auth.getName().concat(" have access!"));
		}else {
			logger.info("You " + auth1.getName().concat(" dont have access!"));
		}

		// Pageable pageable = new PageRequest(page, 5);
		Pageable pageable = PageRequest.of(page, 4);
		Page<Client> clients = clientService.findAll(pageable);

		PageRender<Client> pageRender = new PageRender<>("/list", clients);

		model.addAttribute("title", "Clients List");
		model.addAttribute("clients", clients);
		model.addAttribute("page", pageRender);
		return "list";
	}

	@GetMapping(value = "/form")
	public String create(Map<String, Object> map) {
		Client client = new Client();
		map.put("client", client);
		map.put("title", "Client form");
		return "form";
	}

	@PostMapping(value = "/form")
	public String save(@Valid Client client, @RequestParam(value = "file") MultipartFile photo, BindingResult result,
			Model model, RedirectAttributes flash, SessionStatus status) {

		model.addAttribute("title", "Clients List");
		if (result.hasErrors()) {
			return "form";
		}

		if (!photo.isEmpty()) {
			if (client.getId() != null && client.getId() > 0 && client.getPhoto() != null
					&& client.getPhoto().length() > 0) {
				uploadFileService.delete(client.getPhoto());
			}
			String name = null;
			try {
				name = uploadFileService.copy(photo);
			} catch (IOException e) {
				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Photo " + photo.getOriginalFilename() + " uploaded succesfully!");
			client.setPhoto(name);
		}

		String msjFlash = (client.getId() != null) ? "Cliente editado con éxito" : "Cliente guardado con exito";

		clientService.save(client);
		status.setComplete();
		flash.addFlashAttribute("success", msjFlash);
		return "redirect:list";
	}

	@GetMapping(value = "/form/{id}")
	public String edit(@PathVariable(value = "id") Long id, Map<String, Object> map, RedirectAttributes flash) {
		Client client = null;

		if (id > 0) {
			client = clientService.fetchByIdWithInvoices(id);
			if (client == null) {
				flash.addFlashAttribute("error", "El id no existe en la base de datos");
				return "redirect:list";
			}
		} else {
			flash.addFlashAttribute("error", "El id tiene que ser mayor a cero");
			return "redirect:list";
		}

		map.put("client", client);
		map.put("title", "Client edit");
		return "form";
	}

	@GetMapping(value = "/view/{id}")
	public String view(@PathVariable(value = "id") Long id, Map<String, Object> map, RedirectAttributes flash) {
		Client client = clientService.findOne(id);

		if (client == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/list";
		}
		map.put("client", client);
		map.put("title", "Client detail");

		return "view";
	}

	@GetMapping(value = "/uploads/{file:.*}")
	public ResponseEntity<Resource> viewPhoto(@PathVariable String file) {

		Resource resource = null;

		try {
			resource = uploadFileService.load(file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "atachment; file=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@GetMapping(value = "/delete/{id}")
	public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			Client client = clientService.findOne(id);
			clientService.remove(id);
			flash.addFlashAttribute("success", "Cliente eliminado con exito");

			if (uploadFileService.delete(client.getPhoto())) {
				flash.addFlashAttribute("info", "Foto eliminada con exito");
			}
		}
		return "redirect:/list";
	}

	private boolean hasRole(String role) {
		SecurityContext context = SecurityContextHolder.getContext();

		if (context == null) {
			return false;
		}

		Authentication auth = context.getAuthentication();

		if (auth == null) {
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		for (GrantedAuthority a : authorities) {
			if (role.equals(a.getAuthority())) {
				logger.info("Tu rol es: ".concat(a.getAuthority()));
				return true;
			}

		}
		return false;

	}
}
