package br.edu.unoesc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unoesc.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

}
