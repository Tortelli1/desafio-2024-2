package br.edu.unoesc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.unoesc.model.Brand;
import br.edu.unoesc.repository.BrandRepository;

@Service
public class BrandService {

	@Autowired
	private BrandRepository brandRepository;
	
	public Brand salvarBrand(Brand brand) {
		return brandRepository.save(brand);
	}
	
	public List<Brand> getAllBrands() {
		return brandRepository.findAll();
	}
	
	public Brand getBrandById(Integer id) {
		return brandRepository.findById(id).orElse(null);
	}
	
	public Boolean deletarBrand(Integer id) {
		if (brandRepository.existsById(id)) {
			brandRepository.deleteById(id);
			return true;
		}
		return false;
	}
	
}
