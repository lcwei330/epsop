package com.wei.epsop.service;


import com.wei.epsop.dao.ItemImageDAO;
import com.wei.epsop.pojo.Item;
import com.wei.epsop.pojo.ItemImage;
import com.wei.epsop.service.CategoryService;
import com.wei.epsop.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames="itemImages")
public class ItemImageService   {

    public static final String type_single = "single";
    public static final String type_detail = "detail";

    @Autowired ItemImageDAO itemImageDAO;
    @Autowired ItemService itemService;
    @Autowired CategoryService categoryService;

    @Cacheable(key="'itemImages-one-'+ #i0")
    public ItemImage get(int id) {
        return itemImageDAO.findOne(id);
    }

    public void setFirstProdutImage(Item item) {
        ItemImageService itemImageService = SpringContextUtil.getBean(ItemImageService.class);
        List<ItemImage> singleImages = itemImageService.listSingleItemImages(item);
        if(!singleImages.isEmpty())
            item.setFirstItemImage(singleImages.get(0));
        else
            item.setFirstItemImage(new ItemImage()); //这样做是考虑到产品还没有来得及设置图片，但是在订单后台管理里查看订单项的对应产品图片。

    }
    @CacheEvict(allEntries=true)
    public void add(ItemImage bean) {
        itemImageDAO.save(bean);

    }
    @CacheEvict(allEntries=true)
    public void delete(int id) {
        itemImageDAO.delete(id);
    }

    @Cacheable(key="'itemImages-single-pid-'+ #i0.id")
    public List<ItemImage> listSingleItemImages(Item item) {
        return itemImageDAO.findByItemAndTypeOrderByIdDesc(item, type_single);
    }
    @Cacheable(key="'itemImages-detail-pid-'+ #i0.id")
    public List<ItemImage> listDetailItemImages(Item item) {
        return itemImageDAO.findByItemAndTypeOrderByIdDesc(item, type_detail);
    }

    public void setFirstItemImages(List<Item> items) {
        for (Item item : items)
            setFirstProdutImage(item);
    }

    public void setFirstItemImagesOnOrderItems(List<OrderItem> ois) {
        for (OrderItem orderItem : ois) {
            setFirstProdutImage(orderItem.getItem());
        }
    }
}

