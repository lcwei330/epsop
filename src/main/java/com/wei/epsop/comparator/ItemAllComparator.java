package com.wei.epsop.comparator;


import com.wei.epsop.pojo.Item;

import java.util.Comparator;



public class ItemAllComparator implements Comparator<Item>{

    @Override
    public int compare(Item i1, Item i2) {
        return i2.getReviewCount()*i2.getSaleCount()-i1.getReviewCount()*i1.getSaleCount();
    }

}

