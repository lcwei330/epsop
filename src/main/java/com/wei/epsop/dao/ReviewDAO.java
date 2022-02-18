package com.wei.epsop.dao;


import com.wei.epsop.pojo.Item;
import com.wei.epsop.pojo.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewDAO extends JpaRepository<Review,Integer>{

    List<Review> findByItemOrderByIdDesc(Item item);
    int countByItem(Item item);

}

