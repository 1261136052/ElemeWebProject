package org.example.eleme.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.eleme.model.cart;
import org.example.eleme.model.user;


@Mapper
public interface cartMapper extends BaseMapper<cart> {
}
