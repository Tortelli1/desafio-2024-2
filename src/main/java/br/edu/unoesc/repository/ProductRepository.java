package br.edu.unoesc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unoesc.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

}
