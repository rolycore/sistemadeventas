package com.shalomsolutiontech.sistemadeventas;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import javax.validation.Valid;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping("")
	
	public String mostrarHome() {

	    return"index";
	}
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		
		return "signup_form";
	}
	@PostMapping("/process_register")
	public String processRegister(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		userRepo.save(user);
		
		return "register_success";
	}
	@GetMapping("/users")
	public String listUsers(Model model) {
		List<User> listUsers = userRepo.findAll();
		model.addAttribute("listUsers", listUsers);
		
		return "users";
	}
	   @PostMapping(value = "/eliminar")
	    public String eliminarUsuario(@ModelAttribute User user, RedirectAttributes redirectAttrs) {
	        redirectAttrs
	                .addFlashAttribute("mensaje", "Usuario Eliminado correctamente")
	                .addFlashAttribute("clase", "warning");
	        userRepo.deleteById(user.getId());
	        return "redirect:/users";
	    }
	   
	   @PostMapping(value = "/editar/{id}")
	    public String actualizarUsuario(@ModelAttribute @Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
	        if (bindingResult.hasErrors()) {
	            if (user.getId() != null) {
	                return "/editar_user";
	            }
	            return "redirect:/users";
	        }
	        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
	        User posibleUserExistente = userRepo.findFirstById(user.getId());

	        if (posibleUserExistente != null && !posibleUserExistente.getId().equals(user.getId())) {
	            redirectAttrs
	                    .addFlashAttribute("mensaje", "Ya existe un usuario con ese email")
	                    .addFlashAttribute("clase", "warning");
	            return "redirect:/signup_form";
	        }
	        userRepo.save(user);
	        redirectAttrs
	                .addFlashAttribute("mensaje", "Usuario Editado correctamente")
	                .addFlashAttribute("clase", "success");
	        return "redirect:/users";
	    }

	    @GetMapping(value = "/editar/{id}")
	    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
	        model.addAttribute("user", userRepo.findById((long) id).orElse(null));
	        
	        return "/editar_user";
	    }

@GetMapping("/login")
	
	public String mostrarLogin() {
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
	return"login";
}
	    return"index";
	}
	

}


