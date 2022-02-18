package com.wei.epsop.comparator;


import com.wei.epsop.pojo.Item;

import java.util.Comparator;

public class ItemReviewComparator implements Comparator<Item> {

    @Override
    public int compare(Item i1, Item i2) {
        return i2.getReviewCount()-i1.getReviewCount();
    }

}

