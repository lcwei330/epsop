package com.wei.epsop.service;

import com.wei.epsop.dao.ReviewDAO;
import com.wei.epsop.pojo.Item;
import com.wei.epsop.pojo.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames="reviews")
public class ReviewService {

	@Autowired ReviewDAO reviewDAO;
	@Autowired ItemService itemService;

	@CacheEvict(allEntries=true)
    public void add(Review review) {
    	reviewDAO.save(review);
    }

	@Cacheable(key="'reviews-pid-'+ #p0.id")
    public List<Review> list(Item item){
        List<Review> result =  reviewDAO.findByItemOrderByIdDesc(item);
        return result;
    }

	@Cacheable(key="'reviews-count-pid-'+ #p0.id")
    public int getCount(Item item) {
    	return reviewDAO.countByItem(item);
    }
	
}

