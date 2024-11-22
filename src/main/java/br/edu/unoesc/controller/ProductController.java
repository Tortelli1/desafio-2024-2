package br.edu.unoesc.controller;

import java.util.List;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.unoesc.dto.ProductAPI;
import br.edu.unoesc.model.Brand;
import br.edu.unoesc.model.Category;
import br.edu.unoesc.model.Product;
import br.edu.unoesc.service.BrandService;
import br.edu.unoesc.service.CategoryService;
import br.edu.unoesc.service.ProductAPIService;
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
	
	@Autowired
	private ProductAPIService productAPIService;
	
	@GetMapping("/consultar")
	public String consultarProduto(@ModelAttribute("product") Product product, Model model) {
		List<Product> products = productService.getAllProducts();
		model.addAttribute("products", products);
		return "/consultar/consultarProduto";
	}
	
	@GetMapping("/cadastrar")
	public String cadastrarProduto(@ModelAttribute("product") Product product, ModelMap model) {
		if (product.getId() == null) {
	        product = new Product();
	    }
		
		List<Category> categories = categoryService.getAllActiveCategories();
	    List<Brand> brands = brandService.getAllActiveBrands();
	    
	    model.addAttribute("categories", categories);
	    model.addAttribute("brands", brands);
	    model.addAttribute("product", new Product());

	    return "/cadastrar/cadastrarProduto";
	}
	
	@PostMapping("/salvar")
	public String salvarProduto(@Validated @ModelAttribute Product product, 
			BindingResult result, 
			RedirectAttributes attr) {
		try {
			
			if (result.hasErrors()) {
				attr.addFlashAttribute("error", "Preencha todos os campos obrigatórios!");
				return "redirect:/product/cadastrar";
			}
			
			Integer generatedId = product.getId();
			
			ProductAPI apiProduct = productAPIService.fetchProductFromAPI(generatedId);
            
			if (apiProduct != null) {
                product.setDescription(apiProduct.getDescription());
                product.setPrice(apiProduct.getPrice());
                product.setRating(apiProduct.getRating());
                product.setStock(apiProduct.getStock());
                product.setSku(apiProduct.getSku());
                product.setWeight(apiProduct.getWeight());
            } else {
            	attr.addFlashAttribute("error", "Produto não localizado com o seguinte ID: " + generatedId);
            	return "redirect:/product/cadastrar";
            }
			
			productService.salvarProduct(product);
			return "redirect:/product/cadastrar";
			
		} catch (Exception e) {
			attr.addFlashAttribute("error", "Erro ao salvar o produto.");
			return "redirect:/product/cadastrar";
		}
	}
	
	@GetMapping("/editar/{id}")
	public String consultarProduto(@PathVariable("id") Integer id, Model model) {
		try {
			Product product = productService.getProductById(id);
			if (product == null) {
				return "redirect:/product/consultar";
			}
			List<Category> categories = categoryService.getAllActiveCategories();
			List<Brand> brands = brandService.getAllActiveBrands();
			
			model.addAttribute("product",product);
			model.addAttribute("categories", categories);
			model.addAttribute("brands", brands);
			
			return "/editar/editarProduto";
		} catch (Exception e) {
			return "redirect:/product/consultar";
		}
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
