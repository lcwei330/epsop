package com.wei.epsop.es;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.wei.epsop.pojo.Item;

public interface ItemESDAO extends ElasticsearchRepository<Item,Integer>{

}

