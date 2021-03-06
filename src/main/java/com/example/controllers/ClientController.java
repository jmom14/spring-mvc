package com.example.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Client;
import com.example.service.IClientService;
import com.example.service.IUploadFileService;
import com.example.util.paginator.PageRender;
import com.example.view.xml.ClientList;

@Controller
@SessionAttributes("client")
public class ClientController {

	@Autowired
	private IClientService clientService;

	@Autowired
	private IUploadFileService uploadFileService;
	
	@Autowired
	private MessageSource message;

	protected final Log logger = LogFactory.getLog(this.getClass());

	/*
	 * ClientList is a wrapper class to contain the object and be able to format to xml
	 * */
	@GetMapping(value =  "/list-rest")
	public @ResponseBody ClientList listRest() {
		
		return  new ClientList(clientService.findAll());
	}

	@GetMapping(value = { "/list", "/" })
	public String list(@RequestParam(value = "page", defaultValue = "0") int page, Model model, Authentication auth,
			HttpServletRequest request, Locale locale) {

		Authentication auth1 = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			logger.info("Hi, user authenticated ".concat(auth.getName()).concat(" !"));
		}
		
		if(hasRole("ROLE_ADMIN")) {
			logger.info("You " + auth1.getName().concat(" have access!"));
		}else {
			logger.info("You " + auth1.getName().concat(" dont have access!"));
		}
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		
		if(securityContext.isUserInRole("ADMIN")) {
			logger.info("SecurityContextHolderAwareRequestWrapper: " + auth1.getName().concat(" have access!"));
		}else {
			logger.info("SecurityContextHolderAwareRequestWrapper: " + auth1.getName().concat(" dont have access!"));
		}
		
		if(request.isUserInRole("ROLE_ADMIN")) {
			logger.info("HttpServletRequest: " + auth1.getName().concat(" have access!"));
		}else {
			logger.info("HttpServletRequest: " + auth1.getName().concat(" dont have access!"));
		}
		
		
		// Pageable pageable = new PageRequest(page, 5);
		Pageable pageable = PageRequest.of(page, 4);
		Page<Client> clients = clientService.findAll(pageable);

		PageRender<Client> pageRender = new PageRender<>("/list", clients);

		
		model.addAttribute("title", message.getMessage("text.cliente.listar.titulo", null, locale));
		model.addAttribute("clients", clients);
		model.addAttribute("page", pageRender);
		return "list";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/form")
	public String create(Map<String, Object> map) {
		Client client = new Client();
		map.put("client", client);
		map.put("title", "Client form");
		return "form";
	}

	@Secured("ROLE_ADMIN")
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

	//@Secured("ROLE_ADMIN")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
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
	
	//@Secured("ROLE_USER")
	@PreAuthorize("hasRole('ROLE_USER')")
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

	@Secured({"ROLE_USER", "ROLE_ADMIN"})
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

	@Secured("ROLE_ADMIN")
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
		
		return authorities.contains(new SimpleGrantedAuthority(role));
//		for (GrantedAuthority a : authorities) {
//			if (role.equals(a.getAuthority())) {
//				logger.info("Tu rol es: ".concat(a.getAuthority()));
//				return true;
//			}
//
//		}
//		return false;

	}
}
