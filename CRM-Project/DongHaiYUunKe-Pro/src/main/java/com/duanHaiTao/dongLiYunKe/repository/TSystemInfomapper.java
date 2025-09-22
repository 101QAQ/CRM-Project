package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.model.TSystemInfo;

public interface TSystemInfomapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TSystemInfo record);

    int insertSelective(TSystemInfo record);

    TSystemInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TSystemInfo record);

    int updateByPrimaryKey(TSystemInfo record);
}