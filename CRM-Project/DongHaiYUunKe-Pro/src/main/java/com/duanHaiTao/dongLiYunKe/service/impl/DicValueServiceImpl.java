package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.model.TDicValue;
import com.duanHaiTao.dongLiYunKe.model.TProduct;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.duanHaiTao.dongLiYunKe.repository.TDicValuemapper;
import com.duanHaiTao.dongLiYunKe.service.DicValueService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DicValueServiceImpl implements DicValueService {

    @Autowired
    private TDicValuemapper dicValueMapper;

    @Override
    public Map<String,List<TDicValue>> getDicValueByDicCode(List<String> typeCode) {
        List<TDicValue> tDicValues = dicValueMapper.selectByDicCode(typeCode);
        Map<String,List<TDicValue>> map = new HashMap<>();
        List<TDicValue> appellation=new ArrayList<>();
        List<TDicValue> needLoan=new ArrayList<>();
        List<TDicValue> intentionState=new ArrayList<>();
        List<TDicValue> state=new ArrayList<>();
        List<TDicValue> source=new ArrayList<>();
        for(TDicValue tDicValue:tDicValues){
            switch (tDicValue.getTypeCode()){
                case "appellation":appellation.add(tDicValue);break;
                case "needLoan":needLoan.add(tDicValue);break;
                case "intentionState":intentionState.add(tDicValue);break;
                case "clueState":state.add(tDicValue);break;
                case "source":source.add(tDicValue);break;
            }
        }
        map.put("appellation",appellation);
        map.put("needLoan",needLoan);
        map.put("intentionState",intentionState);
        map.put("state",state);
        map.put("source",source);
        return map;
    }

    @Override
    public List<TDicValue> getDicValueByDicCode(String typeCode) {
        return dicValueMapper.selectByDicValue(typeCode);
    }

    @Override
    public List<TDicValue> getDicValueList() {
        return dicValueMapper.selectAll();
    }

    @Override
    public PageInfo<Object> dicValuePage(Integer pageNum, String condition) throws JsonProcessingException {
        PageHelper.startPage(pageNum, 10);

        TDicValue tDicValue = new ObjectMapper().readValue(condition, TDicValue.class);

        List<TDicValue> list = dicValueMapper.selectPage(tDicValue);

        return new PageInfo<>(list);
    }

    @Override
    public int add(TDicValue dicValue) {
        return dicValueMapper.insertSelective(dicValue);
    }

    @Override
    public int delete(Integer id) {
        return dicValueMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteBath(String idStr) {
        List<String> list = Arrays.asList(idStr.split(","));

        List<Integer> ids = new ArrayList<>();

        list.forEach(id-> ids.add(Integer.parseInt(id)));

        return dicValueMapper.deleteBatch(ids);
    }

    @Override
    public TDicValue findOne(Integer id) {
        return dicValueMapper.selectByPrimaryKey(id);
    }

    @Override
    public int editDicValue(TDicValue dicValue) {
        return dicValueMapper.updateByPrimaryKeySelective(dicValue);
    }
}
