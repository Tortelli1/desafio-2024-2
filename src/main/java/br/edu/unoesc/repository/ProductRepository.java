package br.edu.unoesc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.unoesc.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

}
