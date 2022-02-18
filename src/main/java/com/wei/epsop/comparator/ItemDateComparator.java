package com.wei.epsop.comparator;


import com.wei.epsop.pojo.Item;

import java.util.Comparator;


public class ItemDateComparator implements Comparator<Item>{

    @Override
    public int compare(Item i1, Item i2) {
        return i1.getCreateDate().compareTo(i2.getCreateDate());
    }

}

