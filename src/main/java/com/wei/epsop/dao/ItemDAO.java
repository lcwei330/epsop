package com.wei.epsop.dao;

import com.wei.epsop.pojo.Category;
import com.wei.epsop.pojo.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemDAO extends JpaRepository<Item,Integer>{
    Page<Item> findByCategory(Category category, Pageable pageable);
    List<Item> findByCategoryOrderById(Category category);

    List<Item> findByNameLike(String keyword, Pageable pageable);


}

