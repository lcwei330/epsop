package com.wei.epsop.service;

import com.wei.epsop.dao.ItemDAO;
import com.wei.epsop.es.ItemESDAO;
import com.wei.epsop.pojo.Category;
import com.wei.epsop.pojo.Item;
import com.wei.epsop.util.Page4Navigator;
import com.wei.epsop.util.SpringContextUtil;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames="items")
public class ItemService  {

    @Autowired ItemDAO itemDAO;
    @Autowired
    ItemESDAO itemESDAO;
    @Autowired ItemImageService itemImageService;
    @Autowired CategoryService categoryService;

    @Autowired ReviewService reviewService;

    @CacheEvict(allEntries=true)
    public void add(Item bean) {
        itemDAO.save(bean);
        itemESDAO.save(bean);
    }

    @CacheEvict(allEntries=true)
    public void delete(int id) {
        itemDAO.delete(id);
        itemESDAO.delete(id);
    }

    @Cacheable(key="'items-one-'+ #p0")
    public Item get(int id) {
        return itemDAO.findOne(id);
    }

    @CacheEvict(allEntries=true)
    public void update(Item bean) {
        itemDAO.save(bean);
        itemESDAO.save(bean);
    }

    @Cacheable(key="'items-cid-'+#p0+'-page-'+#p1 + '-' + #p2 ")
    public Page4Navigator<Item> list(int cid, int start, int size,int navigatePages) {
        Category category = categoryService.get(cid);
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Item> pageFromJPA =itemDAO.findByCategory(category,pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    public void fill(List<Category> categorys) {
        for (Category category : categorys) {
            fill(category);
        }
    }


    @Cacheable(key="'items-cid-'+ #p0.id")
    public List<Item> listByCategory(Category category){
        return itemDAO.findByCategoryOrderById(category);
    }

    public void fill(Category category) {
        ItemService itemService = SpringContextUtil.getBean(ItemService.class);
        List<Item> items = itemService.listByCategory(category);
        itemImageService.setFirstProdutImages(items);
        category.setItems(items);
    }


    public void fillByRow(List<Category> categorys) {
        int itemNumberEachRow = 8;
        for (Category category : categorys) {
            List<Item> items =  category.getItems();
            List<List<Item>> itemsByRow =  new ArrayList<>();
            for (int i = 0; i < items.size(); i+=itemNumberEachRow) {
                int size = i+itemNumberEachRow;
                size= size>items.size()?items.size():size;
                List<Item> itemsOfEachRow =items.subList(i, size);
                itemsByRow.add(itemsOfEachRow);
            }
            category.setItemsByRow(itemsByRow);
        }
    }


    public void setSaleAndReviewNumber(Item item) {
        int saleCount = orderItemService.getSaleCount(item);
        item.setSaleCount(saleCount);


        int reviewCount = reviewService.getCount(item);
        item.setReviewCount(reviewCount);

    }


    public void setSaleAndReviewNumber(List<Item> items) {
        for (Item item : items)
            setSaleAndReviewNumber(item);
    }

    public List<Item> search(String keyword, int start, int size) {
        initDatabase2ES();
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
                .add(QueryBuilders.matchPhraseQuery("name", keyword),
                        ScoreFunctionBuilders.weightFactorFunction(100))
                .scoreMode("sum")
                .setMinScore(10);
        Sort sort  = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start, size,sort);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(functionScoreQueryBuilder).build();

        Page<Item> page = itemESDAO.search(searchQuery);
        return page.getContent();
    }

    private void initDatabase2ES() {
        Pageable pageable = new PageRequest(0, 5);
        Page<Item> page =itemESDAO.findAll(pageable);
        if(page.getContent().isEmpty()) {
            List<Item> items= itemDAO.findAll();
            for (Item item : items) {
                itemESDAO.save(item);
            }
        }
    }

}
