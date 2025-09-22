package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.model.TDicValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TDicValuemapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TDicValue record);

    int insertSelective(TDicValue record);

    TDicValue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TDicValue record);

    int updateByPrimaryKey(TDicValue record);

    TDicValue selectByValue(String value);

    List<TDicValue> selectByDicCode(@Param("codes") List<String> code);

    List<TDicValue> selectByDicValue(@Param("typeCode") String typeCode);

    List<TDicValue> selectPage(TDicValue dicValue);

    List<TDicValue> selectAll();

    int deleteBatch(@Param("ids") List<Integer> list);

}