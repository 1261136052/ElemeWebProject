package org.example.eleme.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.eleme.mapper.*;
import org.example.eleme.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/order")
@Controller
public class orderController {
    @Autowired
    private orderoneMapper orderoneMapper;

    @Autowired
    private ordersummarylistingMapper ordersummarylistingMapper;

    @Autowired
    private cartMapper cartMapper;


    @Autowired
    private shopMapper shopMapper;

    @Autowired
    private userMapper userMapper;

    @RequestMapping("/orderlist")//通过orderid，查看订单详细信息
    @ResponseBody
    public Object orderlist(int orderid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("orderid", orderid);
        List<orderone> orders = orderoneMapper.selectList(queryWrapper);
        List<shopcart> list = new ArrayList<>();
        for (orderone orderone :orders){
            shopcart shopcart = new shopcart();
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("id",orderone.getShopid());
            shop shop = shopMapper.selectOne(queryWrapper1);
            shopcart.setId(orderone.getId());
            shopcart.setName(shop.getName());
            shopcart.setContent(shop.getContent());
            shopcart.setEvaluate(shop.getEvaluate());
            shopcart.setNum(orderone.getShopnum());
            shopcart.setPrice(orderone.getPrice());
            shopcart.setImage(shop.getImage());
            list.add(shopcart);
        }
        return list;
    }

    @RequestMapping("/ordersummarylistinglist")//通过userid，查看用户订单
    @ResponseBody
    public Object ordersummarylisting(int userid) {
        Map<Object, Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid", userid);
        List<ordersummarylisting> ordersummarylistings = ordersummarylistingMapper.selectList(queryWrapper);

        return ordersummarylistings;
    }


    @RequestMapping("/neworderone")//生成订单
    @ResponseBody
    public Object neworderone(int userid) {
        long l = System.currentTimeMillis() % 100000;
        int orderid = (int) l;
        Float totalmoney=new Float(0);
        Map<String, Object> map = new HashMap<>();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid", userid);
        List<cart> list = cartMapper.selectList(queryWrapper);
        if (list.size()<=0){
            map.put("res", "500");
            map.put("msg", "购物车为空");
            return map;
        }
        for (cart cart : list) {
            QueryWrapper queryWrapper0 = new QueryWrapper();
            queryWrapper0.eq("id", cart.getShopid());
            shop shop = shopMapper.selectOne(queryWrapper0);
            orderone orderone = new orderone();
            orderone.setOrderid(orderid);
            orderone.setShopid(cart.getShopid());
            orderone.setShopnum(cart.getNum());
            orderone.setPrice(cart.getNum() * shop.getPrice());
            totalmoney +=  cart.getNum() * shop.getPrice();
            orderoneMapper.insert(orderone);
        }
        QueryWrapper objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("id",userid);
        user user = userMapper.selectOne(objectQueryWrapper);
        if (user==null){
            map.put("res","500");
            map.put("msg","账号不存在");
            return map;
        }else if (user.getMoney()<=totalmoney){
            map.put("res","500");
            map.put("msg","余额不足");
            return map;
        }
        ordersummarylisting ordersummarylisting = new ordersummarylisting();
        ordersummarylisting.setId(orderid);
        ordersummarylisting.setUserid(userid);
        ordersummarylisting.setTotalmoney(totalmoney);
        ordersummarylistingMapper.insert(ordersummarylisting);

        cartMapper.delete(queryWrapper);

        user.setMoney(user.getMoney()-totalmoney);
        userMapper.updateById(user);
        map.put("res", "400");
        return map;
    }


    @RequestMapping("/delete")//通过orderid，输入详细信息
    @ResponseBody
    public Object delete(int orderid) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("orderid", orderid);
        orderoneMapper.delete(queryWrapper);
        ordersummarylistingMapper.deleteById(orderid);
        map.put("res", "400");
        return map;
    }


}
