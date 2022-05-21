package com.naswork.starter.service;
/**
 * @Author: D7-Dj
 * @Date: 2020/5/12 10:46
 **/

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @program: station
 *
 * @description: mybatisplus Service 基类
 * @param <M> 基类 继承于BaseMapper<model类>
 * @param <T> 基类 model类
 * @author: D7E-Dj
 *
 * @create: 2020-05-12 10:46
 **/
public abstract class MapperBaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {
}
