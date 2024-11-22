package br.edu.unoesc.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
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

	        product = productService.salvarProduct(product);
	        
	        Integer generatedId = product.getId();
	        
	        ProductAPI apiProduct = productAPIService.fetchProductFromAPI(generatedId);
	        
	        if (apiProduct != null) {
	        	product.setDescription(apiProduct.getDescription());
		        product.setPrice(apiProduct.getPrice());
		        product.setRating(apiProduct.getRating());
		        product.setStock(apiProduct.getStock());
		        product.setSku(apiProduct.getSku());
		        product.setWeight(apiProduct.getWeight());
		        
		        productService.salvarProduct(product);
	        } else {
	        	throw new Exception("Produto não encontrado na API");
	        }
	        
	        return "redirect:/product/cadastrar";
	    } catch (Exception e) {
	    	e.printStackTrace();
	        attr.addFlashAttribute("error", "Erro ao salvar o produto: " + e.getMessage());
	        return "redirect:/product/cadastrar";
	    }
	}
	
	@GetMapping("/product/editar/{id}")
	public String editarProduto(@PathVariable("id") Integer id, Model model) {
	    Product product = productService.getProductById(id);
	    List<Category> categories = categoryService.getAllActiveCategories();
	    List<Brand> brands = brandService.getAllActiveBrands();
	    
	    model.addAttribute("product", product);
	    model.addAttribute("categories", categories);
	    model.addAttribute("brands", brands);
	    
	    return "/editar/editarProduto";
	}

	@PostMapping("/product/editar/{id}")
	public String salvarEdicaoProduto(@PathVariable("id") Integer id, 
	                                   @ModelAttribute Product product, 
	                                   @RequestParam Integer categoryId, 
	                                   @RequestParam Integer brandId, 
	                                   Model model) {
	    Category category = categoryService.getCategoryById(categoryId);
	    Brand brand = brandService.getBrandById(brandId);

	    if (category == null || brand == null || !category.getActive() || !brand.getActive()) {
	        model.addAttribute("errorMessage", "Categoria ou Marca inválida ou inativa.");
	        return "editar/editarProduto";
	    }

	    product.setCategory(category);
	    product.setBrand(brand);

	    try {
	        productService.updateProduct(id, product);
	    } catch (Exception e) {
	        model.addAttribute("errorMessage", e.getMessage()); 
	        return "editar/editarProduto";
	    }

	    return "redirect:/product/consultar";
	}
	
	@DeleteMapping("/deletar/{id}")
	public String deletarProduto(@PathVariable("id") Integer id, RedirectAttributes attr) {
		try {
	        boolean isRemoved = productService.deletarProduct(id);
	        if (!isRemoved) {
	            attr.addFlashAttribute("error", "O registro não foi localizado!");
	        } else {
	            attr.addFlashAttribute("success", "O registro foi deletado com sucesso!");
	        }
	    } catch (Exception e) {
	        attr.addFlashAttribute("error", "Ocorreu um erro ao tentar deletar o registro!");
	    }
	    return "redirect:/product/consultar";
	}
}
