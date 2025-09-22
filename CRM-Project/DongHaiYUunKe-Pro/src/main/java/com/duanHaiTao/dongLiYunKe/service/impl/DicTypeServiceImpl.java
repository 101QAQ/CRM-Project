package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.model.TDicType;
import com.duanHaiTao.dongLiYunKe.repository.TDicTypemapper;
import com.duanHaiTao.dongLiYunKe.service.DicTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DicTypeServiceImpl implements DicTypeService {



    @Autowired
    private TDicTypemapper dicTypeMapper;


    @Override
    public List<TDicType> getDicTypeList() {
        return dicTypeMapper.selectAll();
    }
}
