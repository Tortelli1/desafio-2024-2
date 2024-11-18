package br.edu.unoesc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
import br.edu.unoesc.service.BrandService;
import br.edu.unoesc.service.CategoryService;
import br.edu.unoesc.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private BrandService brandService;
	
	@GetMapping("/cadastrar")
	public String cadastrarProduto(@ModelAttribute("product") Product product, ModelMap model) {
		List<Category> category = categoryService.getAllCategorys();
		List<Brand> brand = brandService.getAllBrands();
		model.addAttribute("category", category);
		model.addAttribute("brand", brand);
		return "/cadastrar/cadastrarProduto";
	}
	
	@GetMapping("/consultar")
	public String consultarProduto(@ModelAttribute("produtos") Product product) {
		return "/consultar/consultarProduto";
	}
	
	
	@PostMapping("/salvar")
	public ResponseEntity<?> salvarProduto(@Validated @ModelAttribute Product product, BindingResult result) {
		try {
			if (result.hasErrors()) {
				Map<String, String> errors = new HashMap<>();
				result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(errors);
			}
			
			Product savedProduct = productService.salvarProduct(product);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
			
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, String> error = new HashMap<>();
			error.put("message", "Ocorreu um erro inesperado. Tente novamente mais tarde!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}
	
	@GetMapping("/editar/{id}")
	public String consultaProduto(@PathVariable("id") Integer id, Model model) {
		Product product = productService.getProductById(id);
		List<Category> category = categoryService.getAllCategorys();
		List<Brand> brand = brandService.getAllBrands();
		model.addAttribute(product);
		model.addAttribute(category);
		model.addAttribute(brand);
		return "/cadastro/cadastrarProduto";
	}
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<String> deletarProduto(@PathVariable("id") Integer id) {
		try {
			boolean isRemoved = productService.deletarProduct(id);
			if (!isRemoved) {
				return new ResponseEntity<>("O registro não foi localizado!", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>("O registro foi deletado com sucesso!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Ocorreu um erro ao tentar deletar o registro", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
