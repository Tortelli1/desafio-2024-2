package br.edu.unoesc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.unoesc.model.Product;
import br.edu.unoesc.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	public Product salvarProduct(Product product) {
		return productRepository.save(product);
	}
	
	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}
	
	public Product getProductById(Integer id) {
		return productRepository.findById(id).orElse(null);
	}
	
	public Boolean deletarProduct(Integer id) {
		if (productRepository.existsById(id)) {
			productRepository.deleteById(id);
			return true;
		} 
		return false;
	}
	
}
