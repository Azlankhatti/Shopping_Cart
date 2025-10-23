package com.ecom.repository;


import com.ecom.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CategoryRepository extends JpaRepository<Category,Integer> {

    public boolean existsByName(String name);
}
