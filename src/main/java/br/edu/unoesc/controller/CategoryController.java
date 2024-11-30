package br.edu.unoesc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.unoesc.model.Category;
import br.edu.unoesc.service.CategoryService;

@Controller
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/consultar")
	public String consultarCategoria(@ModelAttribute("category") Category category, Model model) {
		List<Category> categories = categoryService.getAllCategorys();
		model.addAttribute("categories", categories);
		return "/consultar/consultarCategoria";
	}
	
	@GetMapping("/cadastrar")
	public String cadastrarCategoria(@ModelAttribute("category") Category category) {
		return "/cadastrar/cadastrarCategoria";
	}
	
	@PostMapping("/salvar")
	public String salvarProduto(@Validated @ModelAttribute Category category, BindingResult result, RedirectAttributes attr) {
		try {
			if (result.hasErrors()) {
				attr.addFlashAttribute("error", "Favor preencher os campos obrigatórios!");
				return "redirect:/category/cadastrar";
			}
		
			categoryService.salvarCategory(category);
			return "redirect:/category/cadastrar";
			
		} catch (Exception e) {
			attr.addFlashAttribute("error", "Ops! Um erro inesperado ocorreu.");
			return "redirect:/category/cadastrar";
		}
	}
	
	@GetMapping("/editar/{id}")
	public String consultarCategoria(@PathVariable("id") Integer id, Model model) {
		Category category = categoryService.getCategoryById(id);
		model.addAttribute(category);
		return "/cadastro/cadastrarProduto";
	}
	
	@PostMapping("/deletar/{id}")
	public String deletarCategoria(@PathVariable("id") Integer id, RedirectAttributes attr) {
		try {
			boolean isRemoved = categoryService.deletarCategory(id);
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
