package br.edu.unoesc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.unoesc.model.Brand;
import br.edu.unoesc.model.Category;
import br.edu.unoesc.service.BrandService;

@Controller
@RequestMapping("/brand")
public class BrandController {

	@Autowired
	private BrandService brandService;
	
	@GetMapping("/consulta")
	public String consultarMarca(@ModelAttribute("brand") Brand brand) {
		return "/consultar/consultarMarca";
	}
	
	@GetMapping("/cadastrar")
	public String cadastrarMarca(@ModelAttribute("brand") Brand brand) {
		return "/cadastrar/cadastrarMarca";
	}
	
	@PostMapping("/salvar")
	public ResponseEntity<?> salvarMarca(@Validated @ModelAttribute Brand brand, BindingResult result) {
		try {
			if (result.hasErrors()) {
				Map<String, String> errors = new HashMap<>();
				result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(errors);
			}
			
			Brand savedBrand = brandService.salvarBrand(brand);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedBrand);
			
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, String> error = new HashMap<>();
			error.put("message", "Ocorreu um erro inesperado. Tente novamente mais tarde!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}
	
	@GetMapping("/editar/{id}")
	public String consultarMarca(@PathVariable("id") Integer id, Model model) {
		Brand brand = brandService.getBrandById(id);
		model.addAttribute(brand);
		return "/cadastro/cadastrarMarca";
	}
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<String> deletarMarca(@PathVariable("id") Integer id) {
		try {
			boolean isRemoved = brandService.deletarBrand(id);
			if (!isRemoved) {
				return new ResponseEntity<>("O registro não foi localizado!", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>("O registro foi deletado com sucesso!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Ocorreu um erro ao tentar deletar o registro", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
