package com.wei.epsop.dao;

import com.wei.epsop.pojo.Category;
import com.wei.epsop.pojo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDAO extends JpaRepository<Product,Integer>{
	Page<Product> findByCategory(Category category, Pageable pageable);
}
