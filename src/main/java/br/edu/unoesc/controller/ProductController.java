package br.edu.unoesc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
		return "/category/cadastro";
	}
	
}
