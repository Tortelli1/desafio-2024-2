package br.edu.unoesc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unoesc.model.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer>{

	List<Brand> findByActiveTrue();
}
