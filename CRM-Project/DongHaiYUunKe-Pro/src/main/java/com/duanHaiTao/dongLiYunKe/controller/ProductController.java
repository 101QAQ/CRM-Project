package com.duanHaiTao.dongLiYunKe.controller;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TProduct;
import com.duanHaiTao.dongLiYunKe.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/getProductList")
    public R<Object> getProductList(){
        List<TProduct> all = productService.findAll();
        return R.Ok(Code.SUCCESS,all);
    }


    @GetMapping("/productPage")

    public R<Object> getProductPage(@RequestParam(value = "pageNum",required = false) Integer pageNum, @RequestParam("condition") String condition) throws JsonProcessingException {
        if (pageNum==null){
            pageNum=1;
        }
        PageInfo<Object> info=productService.productPage(pageNum,condition);

        return R.Ok(Code.SUCCESS,info);

    }

    @PostMapping("/add")
    public R<Object> add(@RequestBody TProduct product,@RequestHeader("Authorization") String token) throws JsonProcessingException {
        int count=productService.add(product,token);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/getProduct")
    public R<Object> getProduct(@RequestParam("id") int id){
        return R.Ok(Code.SUCCESS,productService.findOne(id));
    }

    @PutMapping("/edit")
    public R<Object> edit(@RequestBody TProduct product,@RequestHeader("Authorization") String token) throws JsonProcessingException {
        int count=productService.edit(product,token);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);

    }

    @DeleteMapping("/delete")
    public R<Object> delete(@RequestParam("id") int id){
        int count=productService.delete(id);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }


}
