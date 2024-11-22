package br.edu.unoesc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.unoesc.model.Category;
import br.edu.unoesc.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	 
	public Category salvarCategory(Category category) {
		return categoryRepository.save(category);
	}
	
	public List<Category> getAllCategorys(){
		return categoryRepository.findAll();
	}
	
	public List<Category> getAllActiveCategories(){
		return categoryRepository.findByActiveTrue();
	}
	
	public Category getCategoryById(Integer id) {
		return categoryRepository.findById(id).orElse(null);
	}
	
	public Boolean deletarCategory(Integer id) {
		if (categoryRepository.existsById(id)) {
			categoryRepository.deleteById(id);
			return true;
		} 
		return false;
	} 
}
