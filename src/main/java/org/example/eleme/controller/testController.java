package org.example.eleme.controller;

import org.example.eleme.mapper.*;
import org.example.eleme.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/test")
@Controller
public class testController {
    @Autowired
    private userMapper userMapper;

    @Autowired
    private cartMapper cartMapper;

    @Autowired
    private menuMapper menuMapper;

    @Autowired
    private orderoneMapper orderoneMapper;

    @Autowired
    private ordersummarylistingMapper ordersummarylistingMapper;

    @Autowired
    private shopMapper shopMapper;

    @RequestMapping("/test")
    @ResponseBody
    public Map<String, Object> publicComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 400);
        return map;
    }

    @RequestMapping("/user")
    @ResponseBody
    public Object user() {
        List<user> users = userMapper.selectList(null);
        return users;
    }

    @RequestMapping("/menu")
    @ResponseBody
    public Object menu() {
        List<menu> menus = menuMapper.selectList(null);
        return menus;
    }

    @RequestMapping("/order")
    @ResponseBody
    public Object order() {
        List<orderone> orders = orderoneMapper.selectList(null);
        return orders;
    }

    @RequestMapping("/ordersummarylisting")
    @ResponseBody
    public Object ordersummarylisting() {
        List<ordersummarylisting> ordersummarylistings = ordersummarylistingMapper.selectList(null);
        return ordersummarylistings;
    }

    @RequestMapping("/shop")
    @ResponseBody
    public Object shop() {
        List<shop> shops = shopMapper.selectList(null);
        return shops;
    }

    @RequestMapping("/cart")
    @ResponseBody
    public Object cart() {
        List<cart> carts = cartMapper.selectList(null);
        return carts;
    }

}
