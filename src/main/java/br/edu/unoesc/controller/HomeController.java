package br.edu.unoesc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

	public String paginaPrincipal() {
		return "home";
	}
}
