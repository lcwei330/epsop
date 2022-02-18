package com.wei.epsop.dao;


import com.wei.epsop.pojo.Item;
import com.wei.epsop.pojo.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImageDAO extends JpaRepository<ItemImage,Integer>{
    public List<ItemImage> findByItemAndTypeOrderByIdDesc(Item item, String type);

}

