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

@RequestMapping("/cart")
@Controller
public class cartController {

    @Autowired
    private cartMapper cartMapper;
    @Autowired
    private shopMapper shopMapper;

    @RequestMapping("/list")//根据userid来分类每个人的购物车
    @ResponseBody
    public Object cart(int userid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid",userid);
        List<cart> carts = cartMapper.selectList(queryWrapper);
        List<shopcart> list = new ArrayList<>();
        for (cart cart:carts){
            shopcart shopcart = new shopcart();
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("id",cart.getShopid());
            shop shop = shopMapper.selectOne(queryWrapper1);
            shopcart.setId(cart.getId());
            shopcart.setName(shop.getName());
            shopcart.setContent(shop.getContent());
            shopcart.setEvaluate(shop.getEvaluate());
            shopcart.setNum(cart.getNum());
            shopcart.setPrice(shop.getPrice());
            shopcart.setImage(shop.getImage());
            list.add(shopcart);
        }
        return list;
    }

    @RequestMapping("/jionincart")//加入购物车初始数量为1,如果该用户购物车中以拥有该商品，商品数量加1
    @ResponseBody
    public Object jionincart(int userid,int shopid) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.eq("userid",userid);
        queryWrapper.eq("shopid",shopid);
        cart cart1 = cartMapper.selectOne(queryWrapper);
        if (cart1!=null){
            cart1.setNum(cart1.getNum()+1);
            cartMapper.updateById(cart1);
            map.put("res","500");
            map.put("msg","商品已经存在购物车数量加1");
            return map;
        }

        cart cart = new cart();
        cart.setShopid(shopid);
        cart.setUserid(userid);
        cart.setNum(1);
        cartMapper.insert(cart);
        map.put("res","400");
        return map;
    }


    @RequestMapping("/changemun")//加减购物车的数量，当数量低于0删除
    @ResponseBody
    public Object changemun(int id,int change) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id",id);
        cart cart = cartMapper.selectOne(queryWrapper);

        cart.setNum(cart.getNum()+change);
        cartMapper.updateById(cart);
        if (cart.getNum()<=0){
            map.put("res","400");
            map.put("msg","已从购物车删除记录");
            cartMapper.deleteById(id);
            return map;
        }
        map.put("res","400");
        return map;
    }

    @RequestMapping("/clearall")//加减购物车的数量，当数量低于0删除
    @ResponseBody
    public Object clearall(int userid) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid",userid);
        cartMapper.delete(queryWrapper);
        map.put("res","400");
        return map;
    }

    @RequestMapping("/delete")//加减购物车的数量，当数量低于0删除
    @ResponseBody
    public Object delete(int id) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id",id);
        cartMapper.deleteById(id);
        map.put("res","400");
        return map;
    }
}
