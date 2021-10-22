package org.example.eleme.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.eleme.mapper.*;
import org.example.eleme.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/user")
@Controller
public class userController {
    @Autowired
    private userMapper userMapper;

    @RequestMapping("/list")//列出所有用户
    @ResponseBody
    public Object user() {
        List<user> users = userMapper.selectList(null);
        return users;
    }

    @RequestMapping("/listone")//列出用户
    @ResponseBody
    public Object listone(int id) {
        QueryWrapper objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("id",id);
        user user = userMapper.selectOne(objectQueryWrapper);
        return user;
    }

    @RequestMapping("/login")//登录
    @ResponseBody
    public Object login(String account,String pwd) {
        Map<Object,Object> map = new HashMap<>();
        QueryWrapper objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("account",account);
        objectQueryWrapper.eq("pwd",pwd);
        user user = userMapper.selectOne(objectQueryWrapper);
        if (user==null){
            map.put("res","500");
            return map;
        }
        map.put("res","400");
        map.put("userdata",user);
        return map;
    }
    @RequestMapping("/register")//注册
    @ResponseBody
    public Object register(String nickname,String account,String pwd1,String pwd2) {
        Map<Object,Object> map = new HashMap<>();
        if (!pwd1.equals(pwd2)){
            map.put("res","500");
            map.put("msg","密码不一致");
            return map;
        }

        QueryWrapper objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("account",account);
        user user = userMapper.selectOne(objectQueryWrapper);
        if (user!=null){
            map.put("res","500");
            map.put("msg","账号已存在");
            return map;
        }
        user newuser = new user();
        newuser.setAccount(account);
        newuser.setNickname(nickname);
        newuser.setPwd(pwd1);
        userMapper.insert(newuser);
        map.put("res","400");
        return map;
    }

    @RequestMapping("/delete")//删除账户
    @ResponseBody
    public Object delete(int id) {
        Map<Object,Object> map = new HashMap<>();
        userMapper.deleteById(id);
        map.put("res","400");
        return map;
    }

    @RequestMapping("/updatepassword")//修改密码e
    @ResponseBody
    public Object updatepassword(String account,String pwd1,String pwd2) {
        Map<Object,Object> map = new HashMap<>();
        if (!pwd1.equals(pwd2)){
            map.put("res","500");
            map.put("msg","密码不一致");
            return map;
        }
        QueryWrapper objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("account",account);
        user user = userMapper.selectOne(objectQueryWrapper);
        if (user==null){
            map.put("res","500");
            map.put("msg","账号不存在");
            return map;
        }
        user.setPwd(pwd1);
        userMapper.updateById(user);
        map.put("res","400");
        return map;
    }


    @RequestMapping("/updateuser")//修改密码e
    @ResponseBody
    public Object updateuser(int id,String account,String nickname,String pwd1,String pwd2,Float money) {
        Map<Object,Object> map = new HashMap<>();
        if (!pwd1.equals(pwd2)){
            map.put("res","500");
            map.put("msg","密码不一致");
            return map;
        }
        QueryWrapper objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("id",id);
        user user = userMapper.selectOne(objectQueryWrapper);
        if (user==null){
            map.put("res","500");
            map.put("msg","账号不存在");
            return map;
        }
        user.setAccount(account);
        user.setNickname(nickname);
        user.setMoney(money);
        user.setPwd(pwd1);
        userMapper.updateById(user);
        map.put("res","400");
        return map;
    }

    @RequestMapping("/upmoney")//充值
    @ResponseBody
    public Object upmoney(String id,Float money) {
        Map<Object,Object> map = new HashMap<>();
        QueryWrapper objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("id",id);
        user user = userMapper.selectOne(objectQueryWrapper);
        if (user==null){
            map.put("res","500");
            map.put("msg","账号不存在");
            return map;
        }
        user.setMoney(user.getMoney()+money);
        userMapper.updateById(user);
        map.put("res","400");
        return map;
    }

    @RequestMapping("/tomony")//扣钱
    @ResponseBody
    public Object tomony(int id,Float mony) {
        Map<Object,Object> map = new HashMap<>();
        QueryWrapper objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("id",id);
        user user = userMapper.selectOne(objectQueryWrapper);
        if (user==null){
            map.put("res","500");
            map.put("msg","账号不存在");
            return map;
        }
        user.setMoney(user.getMoney()-mony);
        userMapper.updateById(user);
        map.put("res","400");
        return map;
    }
}
