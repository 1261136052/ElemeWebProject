package org.example.eleme.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.eleme.mapper.*;
import org.example.eleme.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/shop")
@Controller
public class shopController {

    @Autowired
    private shopMapper shopMapper;

    @Autowired
    private menuMapper menuMapper;

    @RequestMapping("/listone")//列出一个需要修改的商品
    @ResponseBody
    public Object listone(int id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id);
        shop shop = shopMapper.selectOne(queryWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("shopdata",shop);
        return map;
    }

    @RequestMapping("/updateshop")//修改商品信息,图片暂时不支持修改，考虑中
    @ResponseBody
    public Object updateshop(int id,String name,String content,Float price,int type,int evaluate) {
        Map<String, Object> map = new HashMap<>();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id);
        shop shop = shopMapper.selectOne(queryWrapper);
        if (shop == null) {
            map.put("res", "500");
            return map;
        }

        shop.setName(name);
        shop.setContent(content);
        shop.setPrice(price);
        shop.setType(type);
        shop.setEvaluate(evaluate);
        shopMapper.updateById(shop);
        map.put("res", "400");
        return map;
    }

    @RequestMapping("/list")//列出全部商品
    @ResponseBody
    public Object list() {
        List<shop> shops = shopMapper.selectList(null);
        return shops;
    }


    @RequestMapping("/menu")//列出全部类型
    @ResponseBody
    public Object menu() {
        List<menu> menus = menuMapper.selectList(null);
        return menus;
    }



    @RequestMapping("/menulist")//根据type列出商品
    @ResponseBody
    public Object menulist(String type) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", type);
        menu menu = menuMapper.selectOne(queryWrapper);
        List<shop> shops = shopMapper.selectList(null);
        List<shop> typeshops = new ArrayList<>();
        for (shop shop:shops){
            if (menu.getId()==shop.getType()){
                typeshops.add(shop);
            }
        }
        return typeshops;
    }

    @RequestMapping("/menulistone")//根据type列出商品
    @ResponseBody
    public Object menulistone(int id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id);
        menu menu = menuMapper.selectOne(queryWrapper);
        return menu;
    }

    @RequestMapping("/addmenu")//添加menu
    @ResponseBody
    public Object addmenu(String type) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", type);
        menu menu = menuMapper.selectOne(queryWrapper);
        if (menu != null) {
            map.put("res", "500");
            return map;
        }
        menu menu1 = new menu();
        menu1.setName(type);
        menuMapper.insert(menu1);
        map.put("res", "400");
        return map;
    }

    @RequestMapping("/deletemenu")//删除menu
    @ResponseBody
    public Object deletmenu(int id) {
        Map<String, Object> map = new HashMap<>();
        menuMapper.deleteById(id);
        map.put("res", "400");
        return map;
    }

    @RequestMapping("/updatemenu")//添加menu
    @ResponseBody
    public Object updatemenu(int id,String type) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id);
        menu menu = menuMapper.selectOne(queryWrapper);
        if (menu == null) {
            map.put("res", "500");
            return map;
        }
        menu.setName(type);
        menuMapper.updateById(menu);
        map.put("res", "400");
        return map;
    }

    @RequestMapping("/addshop")//添加商品
    @ResponseBody
    public Object addshop(String name, MultipartFile file,String content,Float price,int type,int evaluate) throws Exception{
        Map<String, Object> map = new HashMap<>();
        String originalFilename = file.getOriginalFilename();
        String houzui = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
        String fileName = System.currentTimeMillis() + houzui;

        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.indexOf("win") != -1) {
            String url = "src/main/resources/static/uploads/" + fileName;
            File winFiledir = new File(url);
            FileUtil.writeBytes(file.getBytes(), winFiledir);
            System.out.println("保存文件:" + winFiledir.getAbsolutePath());
        }
        //编译环境的目录下
        String uploadFolderPath = ResourceUtils.getURL("classpath:").getPath() + "static/uploads/";
        File dest = new File(uploadFolderPath + fileName);
        FileUtil.writeBytes(file.getBytes(), dest);
        System.out.println(dest.getAbsolutePath());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", name);
        shop shop1 = shopMapper.selectOne(queryWrapper);
        if (shop1 != null) {
            map.put("res", "500");
            return map;
        }
        shop shop = new shop();
        shop.setName(name);
        shop.setImage("uploads/" + fileName);
        shop.setContent(content);
        shop.setPrice(price);
        shop.setType(type);
        shop.setEvaluate(evaluate);
        shopMapper.insert(shop);
        map.put("res", "400");
        return map;
    }

    @RequestMapping("/delete")//删除商品
    @ResponseBody
    public Object delete(int id) {
        Map<String, Object> map = new HashMap<>();
        shopMapper.deleteById(id);
        map.put("res", "400");
        return map;
    }


    @RequestMapping("/evaluate")//评价星级商品
    @ResponseBody
    public Object evaluate(int id,int evaluate) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id);
        shop shop = shopMapper.selectOne(queryWrapper);
        if (shop == null) {
            map.put("res", "500");
            return map;
        }
        shop.setEvaluate(evaluate);
        shopMapper.updateById(shop);
        map.put("res", "400");
        return map;
    }

    @RequestMapping("/check")//根据商品名字或者简介模糊查询
    @ResponseBody
    public Object check(String check) {

        Map<String, Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("name",check);
        queryWrapper.or();
        queryWrapper.like("content",check);
        List list = shopMapper.selectList(queryWrapper);
        if (list.size() == 0) {
            map.put("res", "500");
            return map;
        }
        map.put("res", "400");
        map.put("shoplist", list);
        return map;
    }

    @RequestMapping("/top")//置顶商品,已经置顶的话会取消置顶
    @ResponseBody
    public Object top(int id) {
        int top = 1;
        int down = 0;
        Map<String, Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id);
        shop shop = shopMapper.selectOne(queryWrapper);
        if (shop == null) {
            map.put("res", "500");
            return map;
        }
        if (shop.getTop()==top){
            map.put("res", "500");
            map.put("msg", "已经置顶");
            shop.setTop(down);
            shopMapper.updateById(shop);
            return map;
        }
        shop.setTop(top);
        shopMapper.updateById(shop);
        map.put("res", "400");
        return map;
    }



}
