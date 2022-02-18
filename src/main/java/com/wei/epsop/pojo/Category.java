package com.wei.epsop.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
//因为是做前后端分离，而前后端数据交互用的是 json 格式。 那么 Category 对象就会被转换为 json 数据。
//        而本项目使用 jpa 来做实体类的持久化，jpa 默认会使用 hibernate, 在 jpa 工作过程中，
//        就会创造代理类来继承 Category ，并添加 handler 和 hibernateLazyInitializer
//        这两个无须 json 化的属性，所以这里需要用 JsonIgnoreProperties 把这两个属性忽略掉。
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    String name;

    @Transient
    List<Item> items;
    @Transient
    List<List<Item>> itemsByRow;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = Category.this.items;
    }

    public List<List<Item>> getItemsByRow() {
        return itemsByRow;
    }
    public void setItemsByRow(List<List<Item>> itemsByRow) {
        this.itemsByRow = itemsByRow;
    }
    @Override
    public String toString() {
        return "Category [id=" + id + ", name=" + name + "]";
    }
}

