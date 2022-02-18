package com.wei.epsop.pojo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Item")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
@Document(indexName = "epsop",type = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @ManyToOne
    @JoinColumn(name="cid")
    private Category category;

    //如果既没有指明 关联到哪个Column,又没有明确要用@Transient忽略，那么就会自动关联到表对应的同名字段
    private String name;
    private String subTitle;
    private float originalPrice;
    private float promotePrice;
    private int stock;
    private Date createDate;

    @Transient
    private ItemImage firstItemImage;
    @Transient
    private List<ItemImage> ItemSingleImages;
    @Transient
    private List<ItemImage> ItemDetailImages;
    @Transient
    private int reviewCount;
    @Transient
    private int saleCount;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSubTitle() {
        return subTitle;
    }
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
    public float getOriginalPrice() {
        return originalPrice;
    }
    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }
    public float getPromotePrice() {
        return promotePrice;
    }
    public void setPromotePrice(float promotePrice) {
        this.promotePrice = promotePrice;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public ItemImage getFirstItemImage() {
        return firstItemImage;
    }
    public void setFirstItemImage(ItemImage firstItemImage) {
        this.firstItemImage = firstItemImage;
    }
    public List<ItemImage> getItemSingleImages() {
        return ItemSingleImages;
    }
    public void setItemSingleImages(List<ItemImage> ItemSingleImages) {
        this.ItemSingleImages = ItemSingleImages;
    }
    public List<ItemImage> getItemDetailImages() {
        return ItemDetailImages;
    }
    public void setItemDetailImages(List<ItemImage> ItemDetailImages) {
        this.ItemDetailImages = ItemDetailImages;
    }
    public int getReviewCount() {
        return reviewCount;
    }
    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
    public int getSaleCount() {
        return saleCount;
    }
    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }
    @Override
    public String toString() {
        return "Item [id=" + id + ", category=" + category + ", name=" + name + ", subTitle=" + subTitle
                + ", originalPrice=" + originalPrice + ", promotePrice=" + promotePrice + ", stock=" + stock
                + ", createDate=" + createDate + ", firstItemImage=" + firstItemImage + ", reviewCount="
                + reviewCount + ", saleCount=" + saleCount + "]";
    }



}

