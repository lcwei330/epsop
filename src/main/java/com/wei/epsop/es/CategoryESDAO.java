package com.wei.epsop.es;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.wei.epsop.pojo.Category;

public interface CategoryESDAO extends ElasticsearchRepository<Category,Integer>{

}

