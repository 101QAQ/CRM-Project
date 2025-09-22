package com.duanHaiTao.dongLiYunKe.service;

import com.duanHaiTao.dongLiYunKe.model.TDicValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface DicValueService {

    Map<String,List<TDicValue>> getDicValueByDicCode(List<String> typeCode);

    List<TDicValue> getDicValueByDicCode(String typeCode);

    List<TDicValue> getDicValueList();


    PageInfo<Object> dicValuePage(Integer pageNum, String condition) throws JsonProcessingException;

    int add(TDicValue dicValue);

    int delete(Integer id);

    int deleteBath(String idStr);

    TDicValue  findOne(Integer id);

    int editDicValue(TDicValue dicValue);


}
