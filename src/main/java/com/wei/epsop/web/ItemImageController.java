package com.wei.epsop.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wei.epsop.pojo.Item;
import com.wei.epsop.pojo.ItemImage;
import com.wei.epsop.service.CategoryService;
import com.wei.epsop.service.ItemImageService;
import com.wei.epsop.service.ItemService;
import com.wei.epsop.util.ImageUtil;

@RestController
public class ItemImageController {
    @Autowired ItemService productService;
    @Autowired ItemImageService productImageService;
    @Autowired CategoryService categoryService;

    @GetMapping("/products/{pid}/productImages")
    public List<ItemImage> list(@RequestParam("type") String type, @PathVariable("pid") int pid) throws Exception {
        Item product = productService.get(pid);

        if(ItemImageService.type_single.equals(type)) {
            List<ItemImage> singles =  productImageService.listSingleItemImages(product);
            return singles;
        }
        else if(ItemImageService.type_detail.equals(type)) {
            List<ItemImage> details =  productImageService.listDetailItemImages(product);
            return details;
        }
        else {
            return new ArrayList<>();
        }
    }

    @PostMapping("/productImages")
    public Object add(@RequestParam("pid") int pid, @RequestParam("type") String type, MultipartFile image, HttpServletRequest request) throws Exception {
        ItemImage bean = new ItemImage();
        Item product = productService.get(pid);
        bean.setItem(product);
        bean.setType(type);

        productImageService.add(bean);
        String folder = "img/";
        if(ItemImageService.type_single.equals(bean.getType())){
            folder +="productSingle";
        }
        else{
            folder +="productDetail";
        }
        File  imageFolder= new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder,bean.getId()+".jpg");
        String fileName = file.getName();
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(ItemImageService.type_single.equals(bean.getType())){
            String imageFolder_small= request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle= request.getServletContext().getRealPath("img/productSingle_middle");
            File f_small = new File(imageFolder_small, fileName);
            File f_middle = new File(imageFolder_middle, fileName);
            f_small.getParentFile().mkdirs();
            f_middle.getParentFile().mkdirs();
            ImageUtil.resizeImage(file, 56, 56, f_small);
            ImageUtil.resizeImage(file, 217, 190, f_middle);
        }

        return bean;
    }

    @DeleteMapping("/productImages/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request)  throws Exception {
        ItemImage bean = productImageService.get(id);
        productImageService.delete(id);

        String folder = "img/";
        if(ItemImageService.type_single.equals(bean.getType()))
            folder +="productSingle";
        else
            folder +="productDetail";

        File  imageFolder= new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder,bean.getId()+".jpg");
        String fileName = file.getName();
        file.delete();
        if(ItemImageService.type_single.equals(bean.getType())){
            String imageFolder_small= request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle= request.getServletContext().getRealPath("img/productSingle_middle");
            File f_small = new File(imageFolder_small, fileName);
            File f_middle = new File(imageFolder_middle, fileName);
            f_small.delete();
            f_middle.delete();
        }

        return null;
    }


}

