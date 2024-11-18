package br.edu.unoesc.controller;

import java.util.HashMap;
import java.util.List;
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
import br.edu.unoesc.model.Product;
import br.edu.unoesc.service.CategoryService;

@Controller
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/consulta")
	public String consultarCategoria(@ModelAttribute("category") Category category) {
		return "/consultar/consultarCategoria";
	}
	
	@GetMapping("/cadastrar")
	public String cadastrarCategoria(@ModelAttribute("category") Category category) {
		return "/cadastrar/cadastrarCategoria";
	}
	
	@PostMapping("/salvar")
	public ResponseEntity<?> salvarProduto(@Validated @ModelAttribute Category category, BindingResult result) {
		try {
			if (result.hasErrors()) {
				Map<String, String> errors = new HashMap<>();
				result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(errors);
			}
			
			Category savedCategory = categoryService.salvarCategory(category);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
			
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, String> error = new HashMap<>();
			error.put("message", "Ocorreu um erro inesperado. Tente novamente mais tarde!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}
	
	@GetMapping("/editar/{id}")
	public String consultarCategoria(@PathVariable("id") Integer id, Model model) {
		Category category = categoryService.getCategoryById(id);
		model.addAttribute(category);
		return "/cadastro/cadastrarProduto";
	}
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<String> deletarCategoria(@PathVariable("id") Integer id) {
		try {
			boolean isRemoved = categoryService.deletarCategory(id);
			if (!isRemoved) {
				return new ResponseEntity<>("O registro não foi localizado!", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>("O registro foi deletado com sucesso!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Ocorreu um erro ao tentar deletar o registro", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
