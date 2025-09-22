package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.model.TProduct;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.duanHaiTao.dongLiYunKe.repository.TProductmapper;
import com.duanHaiTao.dongLiYunKe.service.ProductService;
import com.duanHaiTao.dongLiYunKe.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private TProductmapper productMapper;

    @Override
    public List<TProduct> findAll() {
        return productMapper.selectAll();
    }
    @Override
    public PageInfo<Object> productPage(Integer pageNum, String condition) throws JsonProcessingException {
        PageHelper.startPage(pageNum, 10);

        TProduct tProduct = new ObjectMapper().readValue(condition, TProduct.class);

        List<TProduct> list = productMapper.selectPage(BaseQuery.builder().build(),tProduct);

        return new PageInfo<>(list);
    }

    @PreAuthorize("hasAuthority('product:add')")
    @Override
    public int add(TProduct product, String token) throws JsonProcessingException {
        TUser tUser = JwtUtil.parseToken(token);

        product.setCreateBy(tUser.getId());
        product.setCreateTime(new Date());
        return productMapper.insertSelective(product);

    }

    @Override
    public TProduct findOne(Integer id) {
        return productMapper.selectByPrimaryKey(id);
    }

    @PreAuthorize("hasAuthority('product:edit')")
    @Override
    public int edit(TProduct product, String token) throws JsonProcessingException {
        TUser tUser = JwtUtil.parseToken(token);
        product.setEditBy(tUser.getId());
        product.setEditTime(new Date());
        return productMapper.updateByPrimaryKeySelective(product);
    }

    @PreAuthorize("hasAuthority('product:delete')")
    @Override
    public int delete(Integer id) {
        return productMapper.deleteByPrimaryKey(id);
    }
}
